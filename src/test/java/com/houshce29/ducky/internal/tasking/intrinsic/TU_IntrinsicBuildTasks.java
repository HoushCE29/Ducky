package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.intrinsic.testing.TestScenario;
import com.houshce29.ducky.internal.tasking.intrinsic.testing.TestScenarioFactory;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class TU_IntrinsicBuildTasks extends BaseUnitTest {

    @Test
    public void testDeferredDependencyBuilder() {
        runTestSuite(new DeferredDependencyBuilder());
    }

    @Test
    public void testPriorityDeferredDependencyBuilder() {
        runTestSuite(new PriorityDeferredDependencyBuilder());
    }

    @Test
    public void testStandardDependencyBuilder() {
        runTestSuite(new StandardDependencyBuilder());
    }

    private void runTestSuite(IntrinsicBuildTask task) {
        for (TestScenario scenario : TestScenarioFactory.getAllScenarios()) {
            testScenario(scenario, task);
        }
    }

    private void testScenario(TestScenario scenario, IntrinsicBuildTask task) {
        ModifiableEnvironment environment = ModifiableEnvironment.empty();
        ProcessingPlatform platform = ProcessingPlatform.create(new HashSet<>(), scenario.getBuildSet());

        scenario.initForBuild(platform, environment, !(task instanceof StandardDependencyBuilder));
        Class<?> expected = scenario.getExpectedException();
        boolean thrownIfExpecting = expected == null;

        try {
            task.run(platform, environment);
            scenario.validateBuild(environment);
            if (task instanceof StandardDependencyBuilder) {
                Assert.assertTrue(platform.mutableDependencySet().isEmpty());
                Assert.assertTrue(platform.mutablePriorityBuildSet().isEmpty());
            }
            else if (task instanceof PriorityDeferredDependencyBuilder) {
                Assert.assertTrue(platform.mutableDeferredDependencySet().isEmpty());
                Assert.assertTrue(platform.mutablePriorityBuildSet().isEmpty());
            }
            else if (task instanceof DeferredDependencyBuilder) {
                Assert.assertTrue(platform.mutableDeferredDependencySet().isEmpty());
            }
        }
        catch (Exception ex) {
            // If thrownIfExpecting is already true, then bubble..
            // this is unexpected
            if (thrownIfExpecting) {
                throw ex;
            }
            // Mark as thrown
            thrownIfExpecting = true;
            // Fail if not expected exception
            if (!expected.isInstance(ex)) {
                Assert.fail(String.format("[%s] expected [%s] to be thrown, but got [%s].",
                        scenario.getName(), expected, ex.getClass()));
            }
        }

        if (!thrownIfExpecting) {
            Assert.fail(String.format("[%s] expected [%s] to be thrown, but got nothing.",
                    scenario.getName(), expected));
        }
    }
}
