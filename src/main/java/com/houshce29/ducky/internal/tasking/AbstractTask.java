package com.houshce29.ducky.internal.tasking;

import java.util.Objects;

/**
 * Abstraction of a task.
 */
public abstract class AbstractTask implements Task {
    private final String id;

    public AbstractTask(String id) {
        this.id = id;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getWhen());
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        }
        Task otherTask = (Task) obj;
        return id.equals(otherTask.getId())
                && getWhen() == otherTask.getWhen();
    }
}
