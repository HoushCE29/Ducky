package com.houshce29.ducky.internal.tasking;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Static tasking queue.
 */
public class TaskQueue {
    private static final Logger LOGGER = LoggerFactory.get(TaskQueue.class);
    private final List<Task> preBuildTaskQueue = new ArrayList<>();
    private final List<Task> buildTaskQueue = new ArrayList<>();
    private final List<Task> postBuildTaskQueue = new ArrayList<>();

    /**
     * Schedules a pre-build task.
     * @param task Pre build task.
     */
    public void schedulePreBuildTask(PreBuildTask task) {
        preBuildTaskQueue.add(task);
        LOGGER.info("Scheduled pre-build task [%s].", task.getId());
    }

    /**
     * Schedules a build task.
     * @param task Build task.
     */
    public void scheduleBuildTask(BuildTask task) {
        buildTaskQueue.add(task);
        LOGGER.info("Scheduled build task [%s].", task.getId());
    }

    /**
     * Schedules a post-build task.
     * @param task Post build task.
     */
    public void schedulePostBuildTask(PostBuildTask task) {
        postBuildTaskQueue.add(task);
        LOGGER.info("Scheduled post-build task [%s].", task.getId());
    }

    /**
     * Schedules a task.
     * @param task Task to be scheduled.
     */
    public void scheduleTask(Task task) {
        if (task.getWhen() == When.PRE_BUILD) {
            preBuildTaskQueue.add(task);
        }
        else if (task.getWhen() == When.BUILD) {
            buildTaskQueue.add(task);
        }
        else {
            postBuildTaskQueue.add(task);
        }
        LOGGER.info("Scheduled %s task [%s].", task.getWhen(), task.getId());
    }

    /**
     * Runs through the tasks within this queue.
     * @param platform Platform used for processing.
     * @param environment Environment to build.
     */
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        LOGGER.info("***** RUNNING TASK QUEUE *****");
        executeTasks(preBuildTaskQueue, platform, environment);
        executeTasks(buildTaskQueue, platform, environment);
        executeTasks(postBuildTaskQueue, platform, environment);
        LOGGER.info("***** TASK QUEUE COMPLETE *****");
    }

    /**
     * Runs through the list of tasks.
     * @param tasks Tasks to run.
     * @param platform Processing platform.
     * @param environment Environment to build or process.
     */
    private void executeTasks(List<Task> tasks,
                              ProcessingPlatform platform,
                              ModifiableEnvironment environment) {

        for (Task task : tasks) {
            LOGGER.info("Executing %s task [%s]...", task.getWhen(), task.getId());
            task.run(platform, environment);
            LOGGER.info("...Task [%s] complete.", task.getId());
        }
    }
}
