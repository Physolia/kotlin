/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.jvm.lower

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.FlattenStringConcatenationLowering
import org.jetbrains.kotlin.backend.common.lower.flattenStringConcatenationPhase
import org.jetbrains.kotlin.backend.common.lower.loops.forLoopsPhase
import org.jetbrains.kotlin.backend.common.phaser.makeIrFilePhase
import org.jetbrains.kotlin.backend.jvm.JvmBackendContext
import org.jetbrains.kotlin.backend.jvm.ir.JvmIrBuilder
import org.jetbrains.kotlin.backend.jvm.ir.createJvmIrBuilder
import org.jetbrains.kotlin.backend.jvm.lower.inlineclasses.unboxInlineClass
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irImplicitCast
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStringConcatenation
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

internal val jvmStringConcatenationLowering = makeIrFilePhase(
    ::JvmStringConcatenationLowering,
    name = "StringConcatenation",
    description = "Replace IrStringConcatenation with string builders",
    // flattenStringConcatenationPhase consolidates string concatenation expressions.
    // forLoopsPhase may produce IrStringConcatenations.
    prerequisite = setOf(flattenStringConcatenationPhase, forLoopsPhase)
)

/**
 * This lowering pass replaces [IrStringConcatenation]s with StringBuilder appends.
 *
 * This pass is based on [StringConcatenationLowering] in backend.common. The main difference
 * is that this pass also handles JVM specific optimizations, such as calling stringPlus
 * for two arguments, and properly handles inline classes.
 */
private class JvmStringConcatenationLowering(val context: JvmBackendContext) : FileLoweringPass, IrElementTransformerVoidWithContext() {
    override fun lower(irFile: IrFile) = irFile.transformChildrenVoid()

    private val stringBuilder = context.ir.symbols.stringBuilder.owner

    private val constructor = stringBuilder.constructors.single {
        it.valueParameters.size == 0
    }

    private val IrClass.toStringFunction: IrSimpleFunction
        get() = functions.single {
            with(FlattenStringConcatenationLowering) { it.isToString }
        }

    private val toStringFunction = stringBuilder.toStringFunction

    private val defaultAppendFunction = stringBuilder.functions.single {
        it.name.asString() == "append" &&
                it.valueParameters.size == 1 &&
                it.valueParameters.single().type.isNullableAny()
    }

    private val appendFunctions: Map<IrType, IrSimpleFunction?> =
        (context.irBuiltIns.primitiveIrTypes + context.irBuiltIns.stringType).associateWith { type ->
            stringBuilder.functions.singleOrNull {
                it.name.asString() == "append" && it.valueParameters.singleOrNull()?.type == type
            }
        }

    private fun typeToAppendFunction(type: IrType): IrSimpleFunction =
        appendFunctions[type] ?: defaultAppendFunction

    // There is no special append or valueOf function for byte and short on the JVM.
    private fun IrBuilderWithScope.widenIntegerType(expression: IrExpression): IrExpression =
        if (expression.type.isByte() || expression.type.isShort()) {
            irImplicitCast(expression, context.irBuiltIns.intType)
        } else {
            expression
        }

    private fun JvmIrBuilder.callToString(expression: IrExpression): IrExpression {
        val argument = widenIntegerType(expression)
        val argumentType = if (argument.type.isPrimitiveType()) argument.type else context.irBuiltIns.anyNType

        return irCall(backendContext.ir.symbols.typeToStringValueOfFunction(argumentType)).apply {
            putValueArgument(0, argument)
        }
    }

    private fun JvmIrBuilder.lowerInlineClassArgument(expression: IrExpression): IrExpression {
        if (expression.type.unboxInlineClass() == expression.type)
            return expression

        val toStringFunction = expression.type.classOrNull?.owner?.toStringFunction
            ?: return expression

        val toStringReplacement = backendContext.inlineClassReplacements.getReplacementFunction(toStringFunction)
            ?: return expression

        return irCall(toStringReplacement).apply {
            putValueArgument(0, expression)
        }
    }

    override fun visitStringConcatenation(expression: IrStringConcatenation): IrExpression {
        expression.transformChildrenVoid(this)
        return context.createJvmIrBuilder(currentScope!!.scope.scopeOwnerSymbol, expression.startOffset, expression.endOffset).run {
            // When `String.plus(Any?)` is invoked with receiver of platform type String or String with enhanced nullability, this SHOULD
            // fail a nullability check (NullPointerException) on the receiver. However, the non-IR backend has a bug (KT-36625) where this
            // check is not inserted (see KT-36625). To maintain bug compatibility with the non-IR backend, we remove IMPLICIT_NOTNULL casts
            // (which generate the nullability checks in JvmArgumentNullabilityAssertionsLowering) from all arguments.

            fun IrExpression.unwrapImplicitNotNull() =
                if (this is IrTypeOperatorCall && operator == IrTypeOperator.IMPLICIT_NOTNULL)
                    argument
                else
                    this


            val arguments = expression.arguments.map { lowerInlineClassArgument(it) }

            when {
                arguments.isEmpty() ->
                    irString("")

                arguments.size == 1 ->
                    callToString(arguments.single().unwrapImplicitNotNull())

                arguments.size == 2 && arguments[0].type.isStringClassType() ->
                    irCall(backendContext.ir.symbols.intrinsicStringPlus).apply {
                        putValueArgument(0, arguments[0].unwrapImplicitNotNull())

                        // Unwrapping IMPLICIT_NOTNULL is not strictly necessary on 2nd argument (parameter type is `Any?`)
                        putValueArgument(1, arguments[1])
                    }

                else -> {
                    var stringBuilder = irCall(constructor)
                    for (arg in arguments) {
                        val argument = widenIntegerType(arg)
                        val appendFunction = typeToAppendFunction(argument.type)
                        stringBuilder = irCall(appendFunction).apply {
                            dispatchReceiver = stringBuilder

                            // Unwrapping IMPLICIT_NOTNULL is necessary for ALL arguments. There could be a call to `String.plus(Any?)`
                            // anywhere in the flattened IrStringConcatenation expression, e.g., `"foo" + (Java.platformString() + 123)`.
                            putValueArgument(0, argument.unwrapImplicitNotNull())
                        }
                    }
                    irCall(toStringFunction).apply {
                        dispatchReceiver = stringBuilder
                    }
                }
            }
        }
    }
}
