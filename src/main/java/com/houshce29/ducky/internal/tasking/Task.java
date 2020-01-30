package com.houshce29.ducky.internal.tasking;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;

/**
 * Represents a task to be run by the builder.
 */
public interface Task {

    /**
     * Runs the task.
     * @param platform Input and processing platform useful for reading and pushing data for later tasks.
     * @param environment Modifiable environment being built.
     */
    void run(ProcessingPlatform platform, ModifiableEnvironment environment);

    /**
     * @return UNIQUE ID of the task.
     */
    String getId();

    /**
     * @return When the task should run.
     */
    When getWhen();
}
