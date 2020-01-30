package com.houshce29.ducky.internal.tasking;

/**
 * Task that runs after build.
 */
public abstract class PostBuildTask extends AbstractTask {
    public PostBuildTask(String id) {
        super(id);
    }

    @Override
    public final When getWhen() {
        return When.POST_BUILD;
    }
}
