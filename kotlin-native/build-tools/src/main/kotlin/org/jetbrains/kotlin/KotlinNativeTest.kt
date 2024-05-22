/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */
package org.jetbrains.kotlin

import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.getByType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.process.ExecSpec
import org.jetbrains.kotlin.konan.exec.Command
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.LinkerOutputKind
import org.jetbrains.kotlin.konan.target.PlatformManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.regex.Pattern

abstract class KonanTest : DefaultTask(), KonanTestExecutable {
    enum class Logger {
        EMPTY,    // Built without test runner
        GTEST,    // Google test log output
        TEAMCITY, // TeamCity log output
        SIMPLE,   // Prints simple messages of passed/failed tests
        SILENT    // Prints no log of passed/failed tests
    }

    @get:Input
    var disabled: Boolean
        get() = !enabled
        set(value) {
            enabled = !value
        }

    /**
     * Test output directory. Used to store processed sources and binary artifacts.
     */
    @get:Internal
    abstract val outputDirectory: String

    /**
     * Test logger to be used for the test built with TestRunner (`-tr` option).
     */
    @get:Internal
    abstract var testLogger: Logger

    /**
     * Test executable arguments.
     */
    @get:Input
    var arguments = mutableListOf<String>()

    /**
     * Test executable.
     */
    @get:Input
    abstract override val executable: String

    /**
     * Test source.
     */
    @Internal
    lateinit var source: String

    /**
     * Sets test filtering to choose the exact test in the executable built with TestRunner.
     */
    @Input
    var useFilter = true

    /**
     * An action to be executed before the build.
     * As this run task comes after the build task all actions for doFirst
     * should be done before the build and not run.
     */
    @Internal
    override var doBeforeBuild: Action<in Task>? = null

    @Internal
    override var doBeforeRun: Action<in Task>? = null

    @get:Internal
    override val buildTasks: List<Task>
        get() = listOf(project.findKonanBuildTask(name, project.testTarget).get())

    @Suppress("UnstableApiUsage")
    override fun configure(config: Closure<*>): Task {
        super.configure(config)

        notCompatibleWithConfigurationCache("This task uses Task.project at execution time")
        // Set Gradle properties for the better navigation
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        description = "Kotlin/Native test infrastructure task"

        if (testLogger != Logger.EMPTY) {
            arguments.add("--ktest_logger=$testLogger")
        }
        if (useFilter && ::source.isInitialized) {
            arguments.add("--ktest_filter=${source.convertToPattern()}")
        }
        this.dependsOnDist()
        return this
    }

    @TaskAction
    open fun run() = project.executeAndCheck(project.file(executable).toPath(), arguments)

    // Converts to runner's pattern
    private fun String.convertToPattern() = this.removeSuffix(".kt").replace("/", ".") + ".*"

    internal fun ProcessOutput.print(prepend: String = "") {
        if (project.verboseTest)
            println(prepend + """
                |stdout:
                |$stdOut
                |stderr:
                |$stdErr
                |exit code: $exitCode
                """.trimMargin())
    }
}

/**
 * Create a test task of the given type. Supports configuration with Closure passed form build.gradle file.
 */
fun <T : KonanTestExecutable> Project.createTest(name: String, type: Class<T>, config: Closure<*>): T =
        project.tasks.create(name, type).apply {
            // Apply closure set in build.gradle to get all parameters.
            this.configure(config)
            if (enabled) {
                // If run task depends on something, build tasks should also depend on this.
                buildTasks.forEach { buildTask ->
                    buildTask.sameDependenciesAs(this)
                    // Run task should depend on compile task
                    this.dependsOn(buildTask)
                    doBeforeBuild?.let { buildTask.doFirst(it) }
                    buildTask.enabled = enabled
                }
            }
        }

/**
 * Executes a standalone tests provided with either @param executable or by the tasks @param name.
 * The executable itself should be built by the konan plugin.
 */
open class KonanStandaloneTest : KonanTest() {
    init {
        useFilter = false
    }

    override val outputDirectory: String
        get() = "${project.testOutputLocal}/$name"

    override var testLogger = Logger.EMPTY

    override val executable: String
        get() = "$outputDirectory/${project.testTarget.name}/$name.${project.testTarget.family.exeSuffix}"

    @Input
    @Optional
    var expectedExitStatus: Int? = null

    @Internal
    var expectedExitStatusChecker: (Int) -> Boolean = { it == (expectedExitStatus ?: 0) }

    /**
     * Should this test fail or not.
     */
    @Input
    var expectedFail = false

    /**
     * Used to validate output against the golden data.
     */
    @Input
    var useGoldenData: Boolean = false

    @get:InputFile
    @get:Optional
    open val goldenDataFile: File?
        get() {
            val goldenDataFile = computeGoldenDataFile()
            return if (useGoldenData) {
                check(goldenDataFile.isFile) { "Task $name. Golden data file does not exist: $goldenDataFile" }
                goldenDataFile
            } else {
                check(!goldenDataFile.exists()) { "Task $name. Golden data file should not exist: $goldenDataFile" }
                null
            }
        }

    protected open fun computeGoldenDataFile(): File {
        val sourceFile = project.file(source)
        return sourceFile.parentFile.resolve(sourceFile.nameWithoutExtension + ".out")
    }

    private val goldenData: String?
        get() = goldenDataFile?.readText(Charsets.UTF_8)

    /**
     * Checks test's output against gold value and returns true if the output matches the expectation.
     */
    @Internal
    var outputChecker: (String) -> Boolean = { output ->
        if (useGoldenData) goldenData == output else true
    }

    /**
     * Input test data to be passed to process stdin.
     */
    @Input
    var useTestData: Boolean = false

    @get:InputFile
    @get:Optional
    val testDataFile: File?
        get() {
            val sourceFile = project.file(source)
            val testDataFile = sourceFile.parentFile.resolve(sourceFile.nameWithoutExtension + ".in")
            return if (useTestData) {
                check(testDataFile.isFile) { "Task $name. Test data file does not exist: $testDataFile" }
                testDataFile
            } else {
                check(!testDataFile.exists()) { "Task $name. Test data file should not exist: $testDataFile" }
                null
            }
        }

    private val testData: String?
        get() = testDataFile?.readText(Charsets.UTF_8)

    /**
     * Should compiler message be read and validated with output checker or gold value.
     */
    @Input
    var compilerMessages = false

    @Input
    var multiRuns = false

    @Input
    @Optional
    var multiArguments: List<List<String>>? = null

    @Input
    var enableKonanAssertions = true

    @Input
    var verifyIr = true

    /**
     * Compiler flags used to build a test.
     */
    @Internal
    var flags: List<String> = listOf()
        get() {
            val result = field.toMutableList()
            if (enableKonanAssertions)
                result += "-ea"
            if (verifyIr)
                result += "-Xverify-ir=error"
            return result
        }

    @Internal
    fun getSources(): Provider<List<String>> = project.provider {
        val sources = buildCompileList(project.file(source).toPath(), outputDirectory)
        sources.forEach { it.writeTextToFile() }
        sources.map { it.path }
    }

    @TaskAction
    override fun run() {
        doBeforeRun?.execute(this)
        if (project.compileOnlyTests) {
            return
        }
        val times = if (multiRuns && multiArguments != null) multiArguments!!.size else 1
        var output = ProcessOutput("", "", 0)
        for (i in 1..times) {
            val args = arguments + (multiArguments?.get(i - 1) ?: emptyList())
            val testData = this.testData
            output += if (testData != null)
                runProcessWithInput({ project.executor.execute(it) }, executable, args, testData)
            else
                runProcess({ project.executor.execute(it) }, executable, args)
        }
        if (compilerMessages) {
            // TODO: as for now it captures output only in the driver task.
            // It should capture output from the build task using Gradle's LoggerManager and LoggerOutput
            val compilationLog = project.file("$executable.compilation.log").readText()
            output.stdOut = compilationLog + output.stdOut
        }
        output.check()
        output.print()
    }

    private operator fun ProcessOutput.plus(other: ProcessOutput) = ProcessOutput(
            stdOut + other.stdOut,
            stdErr + other.stdErr,
            exitCode + other.exitCode)

    private fun ProcessOutput.check() {
        val timeoutMessage = if (exitCode == -1) {
            "WARNING: probably a timeout\n"
        } else ""
        val exitCodeMismatch = !expectedExitStatusChecker(exitCode)
        if (exitCodeMismatch) {
            val message = if (expectedExitStatus != null)
                "Expected exit status: $expectedExitStatus, actual: $exitCode"
            else
                "Actual exit status doesn't match with exit status checker: $exitCode"
            check(expectedFail) {
                """
                    |${timeoutMessage}Test failed. $message
                    |stdout:
                    |$stdOut
                    |stderr:
                    |$stdErr
                    """.trimMargin()
            }
            println("Expected failure. $message")
        }

        val output = stdOut + stdErr
        val outputMismatch = !outputChecker(output.replace(System.lineSeparator(), "\n"))
        if (outputMismatch) {
            val message = goldenData?.let { goldenData -> "Expected output: $goldenData, actual output: $output" }
                    ?: "Actual output doesn't match with output checker: $output"

            check(expectedFail) { "${timeoutMessage}Test failed. $message" }
            println("Expected failure. $message")
        }

        check((exitCodeMismatch || outputMismatch) || !expectedFail) {
            """
            |Unexpected pass:
            | * exit code mismatch: $exitCodeMismatch
            | * gold value mismatch: $outputMismatch
            | * expected fail: $expectedFail
            """.trimMargin()
        }
    }
}
