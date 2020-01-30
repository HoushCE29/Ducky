package com.houshce29.ducky.framework.core;

import java.util.Collections;
import java.util.Set;

/**
 * Non-modifiable environment.
 */
public class LockedEnvironment extends AbstractEnvironment {
    private LockedEnvironment(Set<Object> objects) {
        super(Collections.unmodifiableSet(objects));
    }

    /**
     * Returns a new locked, unmodifiable environment.
     * @param objects Objects of environment.
     * @return Locked/unmodifiable environment.
     */
    public static LockedEnvironment of(Set<Object> objects) {
        return new LockedEnvironment(objects);
    }
}
