package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.intrinsic.IntrinsicBuildTask;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

/**
 * Mini testing harness framework ONLY FOR INTRINSIC BUILD TASKS.
 */
public abstract class BaseIntrinsicBuildTaskTestUnit extends BaseUnitTest {

    public abstract TestConfig config();

    @Test
    public final void testIntrinsicBuildTask() {
        TestConfig config = config();
        for (TestScenario scenario : TestScenarioFactory.getAllScenarios()) {
            ModifiableEnvironment environment = ModifiableEnvironment.empty();
            ProcessingPlatform platform = newPlatform(scenario, config);
            boolean exceptionCase = false;

            try {
                config.task.run(platform, environment);
                scenario.validateBuild(environment);
                validatePlatform(config, platform);
            }
            catch(Exception ex) {
                validateErrorCase(scenario.getName(), scenario.getExpectedException(), ex);
                exceptionCase = true;
            }
            if (!exceptionCase && scenario.getExpectedException() != null) {
                Assert.fail(String.format("[%s] was supposed to fail by [%s] but didn't.",
                        scenario.getName(), scenario.getExpectedException()));
            }
        }
    }

    private void validatePlatform(TestConfig config, ProcessingPlatform platform) {
        if (config.checkStandard) {
            Assert.assertTrue(platform.mutableDependencySet().isEmpty());
        }
        if (config.checkPriority) {
            Assert.assertTrue(platform.mutablePriorityBuildSet().isEmpty());
        }
        if (config.checkDeferred) {
            Assert.assertTrue(platform.mutableDeferredDependencySet().isEmpty());
        }
    }

    private void validateErrorCase(String scenario, Class<?> expected, Exception actual) {
        if (expected == null) {
            throw new RuntimeException(String.format("[%s] failed due to exception.", scenario), actual);
        }
        if (!expected.isInstance(actual)) {
            Assert.fail(String.format("[%s] expected [%s] but got [%s]",
                    scenario, expected, actual.getClass()));
        }
    }

    private ProcessingPlatform newPlatform(TestScenario scenario, TestConfig config) {
        ProcessingPlatform platform = ProcessingPlatform.create(new HashSet<>(), scenario.getBuildSet());
        if (config.checkStandard) {
            platform.mutableDependencySet().addAll(scenario.getDependencies());
        }
        if (config.checkDeferred) {
            platform.mutableDeferredDependencySet().addAll(scenario.getDependencies());
        }
        if (config.checkPriority) {
            platform.mutablePriorityBuildSet().addAll(scenario.getBuildSet());
        }
        return platform;
    }

    public static TestConfigBuilder define(IntrinsicBuildTask task) {
        return new TestConfigBuilder(task);
    }

    public static class TestConfigBuilder {
        private IntrinsicBuildTask task;
        private boolean checkDeferred = false;
        private boolean checkStandard = false;
        private boolean checkPriority = false;

        private TestConfigBuilder(IntrinsicBuildTask task) {
            this.task = task;
        }

        public TestConfigBuilder checkDeferredDependencySet() {
            this.checkDeferred = true;
            return this;
        }

        public TestConfigBuilder checkStandardDependencySet() {
            this.checkStandard = true;
            return this;
        }

        public TestConfigBuilder checkPriorityDeferredSet() {
            this.checkPriority = true;
            return this;
        }

        public TestConfig build() {
            return new TestConfig(task, checkDeferred, checkStandard, checkPriority);
        }
    }

    public static class TestConfig {
        IntrinsicBuildTask task;
        boolean checkDeferred;
        boolean checkStandard;
        boolean checkPriority;

        private TestConfig(IntrinsicBuildTask task,
                           boolean checkDeferred,
                           boolean checkStandard,
                           boolean checkPriority) {
            this.task = task;
            this.checkDeferred = checkDeferred;
            this.checkPriority = checkPriority;
            this.checkStandard = checkStandard;
        }
    }



}
