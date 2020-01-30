package com.houshce29.ducky.internal.tasking;

/**
 * Task that runs during building / as a build task.
 */
public abstract class BuildTask extends AbstractTask {
    public BuildTask(String id) {
        super(id);
    }

    @Override
    public final When getWhen() {
        return When.BUILD;
    }
}
