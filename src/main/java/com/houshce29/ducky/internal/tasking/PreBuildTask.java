package com.houshce29.ducky.internal.tasking;

/**
 * Task that runs before building.
 */
public abstract class PreBuildTask extends AbstractTask {
    public PreBuildTask(String id) {
        super(id);
    }

    @Override
    public final When getWhen() {
        return When.PRE_BUILD;
    }
}
