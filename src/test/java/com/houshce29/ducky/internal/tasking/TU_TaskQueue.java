package com.houshce29.ducky.internal.tasking;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

public class TU_TaskQueue extends BaseUnitTest {
    private TaskQueue queue;

    @Before
    public void beforeEach() {
        queue = new TaskQueue();
    }

    @Test
    public void testRun() {
        TestPreBuildTask prebuild = new TestPreBuildTask();
        TestBuildTask build = new TestBuildTask();
        TestPostBuildTask postbuild = new TestPostBuildTask();

        // Schedule tasks by known time
        queue.schedulePreBuildTask(prebuild);
        queue.scheduleBuildTask(build);
        queue.schedulePostBuildTask(postbuild);

        // Schedule tasks generically
        queue.scheduleTask(prebuild);
        queue.scheduleTask(build);
        queue.scheduleTask(postbuild);

        ProcessingPlatform platform = ProcessingPlatform.create(new HashSet<>(), new HashSet<>());
        queue.run(platform, ModifiableEnvironment.empty());

        Map<String, Object> variables = platform.variables();
        Assert.assertEquals(prebuild.getId(), variables.get("1"));
        Assert.assertEquals(prebuild.getId(), variables.get("2"));
        Assert.assertEquals(build.getId(), variables.get("3"));
        Assert.assertEquals(build.getId(), variables.get("4"));
        Assert.assertEquals(postbuild.getId(), variables.get("5"));
        Assert.assertEquals(postbuild.getId(), variables.get("6"));
    }

    private static void markAsRun(ProcessingPlatform platform, String taskId) {
        int sum = (int) platform.variables().getOrDefault("test.running.sum", 1);
        platform.variables().put("" + sum, taskId);
        sum++;
        platform.variables().put("test.running.sum", sum);
    }

    // TEST TASKS:

    private static class TestPreBuildTask extends PreBuildTask {
        TestPreBuildTask() {
            super("test-prebuild");
        }

        @Override
        public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
            markAsRun(platform, getId());
        }
    }

    private static class TestBuildTask extends BuildTask {
        TestBuildTask() {
            super("test-build");
        }

        @Override
        public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
            markAsRun(platform, getId());
        }
    }

    private static class TestPostBuildTask extends PostBuildTask {
        TestPostBuildTask() {
            super("test-postbuild");
        }

        @Override
        public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
            markAsRun(platform, getId());
        }
    }
}
