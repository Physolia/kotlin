/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.JetTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/resolve")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class ResolveTestGenerated extends AbstractResolveTest {
    public void testAllFilesPresentInResolve() throws Exception {
        JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve"), Pattern.compile("^(.+)\\.resolve$"), true);
    }

    @TestMetadata("Basic.resolve")
    public void testBasic() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/Basic.resolve");
        doTest(fileName);
    }

    @TestMetadata("ClassObjects.resolve")
    public void testClassObjects() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ClassObjects.resolve");
        doTest(fileName);
    }

    @TestMetadata("Classifiers.resolve")
    public void testClassifiers() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/Classifiers.resolve");
        doTest(fileName);
    }

    @TestMetadata("DefaultParamsOfLocalFunctions.resolve")
    public void testDefaultParamsOfLocalFunctions() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/DefaultParamsOfLocalFunctions.resolve");
        doTest(fileName);
    }

    @TestMetadata("ErrorSupertype.resolve")
    public void testErrorSupertype() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ErrorSupertype.resolve");
        doTest(fileName);
    }

    @TestMetadata("ExtensionFunctions.resolve")
    public void testExtensionFunctions() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ExtensionFunctions.resolve");
        doTest(fileName);
    }

    @TestMetadata("FunctionVariable.resolve")
    public void testFunctionVariable() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/FunctionVariable.resolve");
        doTest(fileName);
    }

    @TestMetadata("ImportFromRootScope.resolve")
    public void testImportFromRootScope() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ImportFromRootScope.resolve");
        doTest(fileName);
    }

    @TestMetadata("ImportingRootScopeWhenProcessingImports.resolve")
    public void testImportingRootScopeWhenProcessingImports() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ImportingRootScopeWhenProcessingImports.resolve");
        doTest(fileName);
    }

    @TestMetadata("kt304.resolve")
    public void testKt304() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/kt304.resolve");
        doTest(fileName);
    }

    @TestMetadata("LocalObjects.resolve")
    public void testLocalObjects() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/LocalObjects.resolve");
        doTest(fileName);
    }

    @TestMetadata("NestedObjects.resolve")
    public void testNestedObjects() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/NestedObjects.resolve");
        doTest(fileName);
    }

    @TestMetadata("NoReferenceForErrorAnnotation.resolve")
    public void testNoReferenceForErrorAnnotation() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/NoReferenceForErrorAnnotation.resolve");
        doTest(fileName);
    }

    @TestMetadata("Objects.resolve")
    public void testObjects() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/Objects.resolve");
        doTest(fileName);
    }

    @TestMetadata("Packages.resolve")
    public void testPackages() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/Packages.resolve");
        doTest(fileName);
    }

    @TestMetadata("PrimaryConstructorParameters.resolve")
    public void testPrimaryConstructorParameters() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/PrimaryConstructorParameters.resolve");
        doTest(fileName);
    }

    @TestMetadata("PrimaryConstructors.resolve")
    public void testPrimaryConstructors() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/PrimaryConstructors.resolve");
        doTest(fileName);
    }

    @TestMetadata("Projections.resolve")
    public void testProjections() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/Projections.resolve");
        doTest(fileName);
    }

    @TestMetadata("PropertyAndFunctionNameClash.resolve")
    public void testPropertyAndFunctionNameClash() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/PropertyAndFunctionNameClash.resolve");
        doTest(fileName);
    }

    @TestMetadata("ResolveOfInfixExpressions.resolve")
    public void testResolveOfInfixExpressions() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ResolveOfInfixExpressions.resolve");
        doTest(fileName);
    }

    @TestMetadata("ResolveToJava.resolve")
    public void testResolveToJava() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ResolveToJava.resolve");
        doTest(fileName);
    }

    @TestMetadata("ResolveToJava2.resolve")
    public void testResolveToJava2() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ResolveToJava2.resolve");
        doTest(fileName);
    }

    @TestMetadata("ResolveToJava3.resolve")
    public void testResolveToJava3() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ResolveToJava3.resolve");
        doTest(fileName);
    }

    @TestMetadata("ResolveToJavaTypeTransform.resolve")
    public void testResolveToJavaTypeTransform() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ResolveToJavaTypeTransform.resolve");
        doTest(fileName);
    }

    @TestMetadata("ScopeInteraction.resolve")
    public void testScopeInteraction() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/ScopeInteraction.resolve");
        doTest(fileName);
    }

    @TestMetadata("StringTemplates.resolve")
    public void testStringTemplates() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/StringTemplates.resolve");
        doTest(fileName);
    }

    @TestMetadata("Super.resolve")
    public void testSuper() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/Super.resolve");
        doTest(fileName);
    }

    @TestMetadata("TryCatch.resolve")
    public void testTryCatch() throws Exception {
        String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/TryCatch.resolve");
        doTest(fileName);
    }

    @TestMetadata("compiler/testData/resolve/candidatesPriority")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CandidatesPriority extends AbstractResolveTest {
        public void testAllFilesPresentInCandidatesPriority() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve/candidatesPriority"), Pattern.compile("^(.+)\\.resolve$"), true);
        }

        @TestMetadata("classObjectOuterResolve.resolve")
        public void testClassObjectOuterResolve() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/classObjectOuterResolve.resolve");
            doTest(fileName);
        }

        @TestMetadata("closerReceiver1.resolve")
        public void testCloserReceiver1() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/closerReceiver1.resolve");
            doTest(fileName);
        }

        @TestMetadata("closerReceiver2.resolve")
        public void testCloserReceiver2() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/closerReceiver2.resolve");
            doTest(fileName);
        }

        @TestMetadata("closerReceiver3.resolve")
        public void testCloserReceiver3() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/closerReceiver3.resolve");
            doTest(fileName);
        }

        @TestMetadata("closerScope.resolve")
        public void testCloserScope() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/closerScope.resolve");
            doTest(fileName);
        }

        @TestMetadata("dispatchReceiverVsExtensionReceiver.resolve")
        public void testDispatchReceiverVsExtensionReceiver() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/dispatchReceiverVsExtensionReceiver.resolve");
            doTest(fileName);
        }

        @TestMetadata("dispatchReceiverVsExtensionReceiver2.resolve")
        public void testDispatchReceiverVsExtensionReceiver2() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/dispatchReceiverVsExtensionReceiver2.resolve");
            doTest(fileName);
        }

        @TestMetadata("extensionToCloserReceiverVsMember.resolve")
        public void testExtensionToCloserReceiverVsMember() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/extensionToCloserReceiverVsMember.resolve");
            doTest(fileName);
        }

        @TestMetadata("implicitThisVsNoReceiver.resolve")
        public void testImplicitThisVsNoReceiver() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/implicitThisVsNoReceiver.resolve");
            doTest(fileName);
        }

        @TestMetadata("implicitThisVsNoReceiver2.resolve")
        public void testImplicitThisVsNoReceiver2() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/implicitThisVsNoReceiver2.resolve");
            doTest(fileName);
        }

        @TestMetadata("localVsImplicitThis.resolve")
        public void testLocalVsImplicitThis() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/localVsImplicitThis.resolve");
            doTest(fileName);
        }

        @TestMetadata("memberVsExtension1.resolve")
        public void testMemberVsExtension1() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/memberVsExtension1.resolve");
            doTest(fileName);
        }

        @TestMetadata("memberVsExtension2.resolve")
        public void testMemberVsExtension2() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/memberVsExtension2.resolve");
            doTest(fileName);
        }

        @TestMetadata("memberVsExtension3.resolve")
        public void testMemberVsExtension3() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/memberVsExtension3.resolve");
            doTest(fileName);
        }

        @TestMetadata("memberVsLocalExtension.resolve")
        public void testMemberVsLocalExtension() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/memberVsLocalExtension.resolve");
            doTest(fileName);
        }

        @TestMetadata("memberWithInvokeVsNonLocal.resolve")
        public void testMemberWithInvokeVsNonLocal() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/memberWithInvokeVsNonLocal.resolve");
            doTest(fileName);
        }

        @TestMetadata("wrongReceiverVsOtherError.resolve")
        public void testWrongReceiverVsOtherError() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/candidatesPriority/wrongReceiverVsOtherError.resolve");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/resolve/delegatedProperty")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class DelegatedProperty extends AbstractResolveTest {
        public void testAllFilesPresentInDelegatedProperty() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve/delegatedProperty"), Pattern.compile("^(.+)\\.resolve$"), true);
        }

        @TestMetadata("delegationByCall.resolve")
        public void testDelegationByCall() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/delegationByCall.resolve");
            doTest(fileName);
        }

        @TestMetadata("delegationByConstructor.resolve")
        public void testDelegationByConstructor() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/delegationByConstructor.resolve");
            doTest(fileName);
        }

        @TestMetadata("delegationByFun.resolve")
        public void testDelegationByFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/delegationByFun.resolve");
            doTest(fileName);
        }

        @TestMetadata("delegationByObject.resolve")
        public void testDelegationByObject() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/delegationByObject.resolve");
            doTest(fileName);
        }

        @TestMetadata("delegationByProperty.resolve")
        public void testDelegationByProperty() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/delegationByProperty.resolve");
            doTest(fileName);
        }

        @TestMetadata("delegationInClass.resolve")
        public void testDelegationInClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/delegationInClass.resolve");
            doTest(fileName);
        }

        @TestMetadata("localDelegation.resolve")
        public void testLocalDelegation() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/delegatedProperty/localDelegation.resolve");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/resolve/imports")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Imports extends AbstractResolveTest {
        public void testAllFilesPresentInImports() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve/imports"), Pattern.compile("^(.+)\\.resolve$"), true);
        }

        @TestMetadata("ImportConflictAllPackage.resolve")
        public void testImportConflictAllPackage() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictAllPackage.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictBetweenImportedAndRootPackage.resolve")
        public void testImportConflictBetweenImportedAndRootPackage() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictBetweenImportedAndRootPackage.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictBetweenImportedAndSamePackage.resolve")
        public void testImportConflictBetweenImportedAndSamePackage() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictBetweenImportedAndSamePackage.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictForFunctions.resolve")
        public void testImportConflictForFunctions() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictForFunctions.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictPackageAndClass.resolve")
        public void testImportConflictPackageAndClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictPackageAndClass.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictSameNameClass.resolve")
        public void testImportConflictSameNameClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictSameNameClass.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictWithClassObject.resolve")
        public void testImportConflictWithClassObject() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictWithClassObject.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictWithInFileClass.resolve")
        public void testImportConflictWithInFileClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictWithInFileClass.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictWithInnerClass.resolve")
        public void testImportConflictWithInnerClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictWithInnerClass.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportConflictsWithMappedToJava.resolve")
        public void testImportConflictsWithMappedToJava() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportConflictsWithMappedToJava.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportNonBlockingAnalysis.resolve")
        public void testImportNonBlockingAnalysis() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportNonBlockingAnalysis.resolve");
            doTest(fileName);
        }

        @TestMetadata("ImportResolveOrderStable.resolve")
        public void testImportResolveOrderStable() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/imports/ImportResolveOrderStable.resolve");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/resolve/labels")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Labels extends AbstractResolveTest {
        public void testAllFilesPresentInLabels() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve/labels"), Pattern.compile("^(.+)\\.resolve$"), true);
        }

        @TestMetadata("labelForPropertyInGetter.resolve")
        public void testLabelForPropertyInGetter() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/labels/labelForPropertyInGetter.resolve");
            doTest(fileName);
        }

        @TestMetadata("labelForPropertyInSetter.resolve")
        public void testLabelForPropertyInSetter() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/labels/labelForPropertyInSetter.resolve");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/resolve/regressions")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Regressions extends AbstractResolveTest {
        public void testAllFilesPresentInRegressions() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve/regressions"), Pattern.compile("^(.+)\\.resolve$"), true);
        }

        @TestMetadata("kt300.resolve")
        public void testKt300() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/regressions/kt300.resolve");
            doTest(fileName);
        }

        @TestMetadata("objectInsideFun.resolve")
        public void testObjectInsideFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/regressions/objectInsideFun.resolve");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/resolve/varargs")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Varargs extends AbstractResolveTest {
        public void testAllFilesPresentInVarargs() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/resolve/varargs"), Pattern.compile("^(.+)\\.resolve$"), true);
        }

        @TestMetadata("MoreSpecificVarargsOfEqualLength.resolve")
        public void testMoreSpecificVarargsOfEqualLength() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/varargs/MoreSpecificVarargsOfEqualLength.resolve");
            doTest(fileName);
        }

        @TestMetadata("NilaryVsVararg.resolve")
        public void testNilaryVsVararg() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/varargs/NilaryVsVararg.resolve");
            doTest(fileName);
        }

        @TestMetadata("UnaryVsVararg.resolve")
        public void testUnaryVsVararg() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/resolve/varargs/UnaryVsVararg.resolve");
            doTest(fileName);
        }
    }
}
