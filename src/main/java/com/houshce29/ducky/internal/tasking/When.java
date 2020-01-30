package com.houshce29.ducky.internal.tasking;

/**
 * Specifies when to run a task.
 */
public enum When {
    /**
     * Runs before build.
     */
    PRE_BUILD,

    /**
     * Runs during/as build.
     */
    BUILD,

    /**
     * Runs after build.
     */
    POST_BUILD
}
