package com.houshce29.ducky.internal;

import com.google.common.collect.ImmutableList;
import com.houshce29.ducky.framework.core.AbstractEnvironment;
import com.houshce29.ducky.internal.tasking.Task;
import com.houshce29.ducky.internal.tasking.TaskQueue;
import com.houshce29.ducky.internal.tasking.intrinsic.DeferredDependencyBuilder;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildDependencyCreator;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildDependencyDeferrer;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildObjectCollector;
import com.houshce29.ducky.internal.tasking.intrinsic.PriorityDeferredDependencyBuilder;
import com.houshce29.ducky.internal.tasking.intrinsic.StandardDependencyBuilder;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Static, godly environment that rules them all.
 * Much like God, this environment is used to build mortal
 * environments in His image.
 */
public class DivineEnvironment extends AbstractEnvironment {
    private static final DivineEnvironment INSTANCE;

    private static final Logger LOGGER = LoggerFactory.get(DivineEnvironment.class);

    private static final TaskQueue TASK_QUEUE;
    private static final Builder BUILDER;
    private static final List<Task> TASKS;
    private static final Set<Object> OBJECTS;

    static {
        LOGGER.info("Initializing the Divine environment...");
        TASKS = ImmutableList.of(
                new PreBuildObjectCollector(),
                new PreBuildDependencyCreator(),
                new PreBuildDependencyDeferrer(),
                new StandardDependencyBuilder(),
                new PriorityDeferredDependencyBuilder(),
                new DeferredDependencyBuilder());

        TASK_QUEUE = new TaskQueue();
        TASKS.forEach(TASK_QUEUE::scheduleTask);
        BUILDER = new Builder(TASK_QUEUE);

        OBJECTS = new HashSet<>();
        OBJECTS.addAll(TASKS);
        OBJECTS.add(TASK_QUEUE);
        OBJECTS.add(BUILDER);

        INSTANCE = new DivineEnvironment();
    }

    private DivineEnvironment() {
        super(OBJECTS);
    }

    /**
     * @return Builder service.
     */
    public Builder getBuilder() {
        return BUILDER;
    }

    /**
     * @return Task queue.
     */
    public TaskQueue getTaskQueue() {
        return TASK_QUEUE;
    }

    /**
     * @return Immutable list of intrinsic tasks.
     */
    public List<Task> getIntrinsicTasks() {
        return TASKS;
    }

    /**
     * @return Single God instance.
     */
    public static DivineEnvironment getInstance() {
        return INSTANCE;
    }
}
