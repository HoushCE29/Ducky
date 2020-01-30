package com.houshce29.ducky.internal;

import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.TaskQueue;

import java.util.Set;

/**
 * Service that builds up dependencies.
 */
public class Builder {
    private final TaskQueue taskQueue;

    Builder(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    /**
     * Builds the environment with the given included objects and injectable types.
     * @param included Pre-built set of objects.
     * @param injections Injectable types that need to be built into objects.
     * @return New environment.
     */
    public Environment build(Set<Object> included, Set<Class<?>> injections) {
        ProcessingPlatform platform = ProcessingPlatform.create(included, injections);
        ModifiableEnvironment environment = ModifiableEnvironment.empty();
        taskQueue.run(platform, environment);
        return environment.toLockedEnvironment();
    }
}
