package com.houshce29.ducky.framework.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Quick environment which is modifiable.
 */
public class ModifiableEnvironment extends AbstractEnvironment {
    private ModifiableEnvironment(Set<Object> objects) {
        super(objects);
    }

    /**
     * Adds an object to the environment.
     * @param object Object to be added.
     */
    public void add(Object object) {
        toSet().add(object);
    }

    /**
     * Removes an object from the environment.
     * @param object Object to be removed.
     */
    public void remove(Object object) {
        toSet().remove(object);
    }

    /**
     * @return A new locked down environment.
     */
    public LockedEnvironment toLockedEnvironment() {
        return LockedEnvironment.of(toSet());
    }

    /**
     * Returns a new environment.
     * @param objects Base objects of the environment.
     * @return New modifiable environment.
     */
    public static ModifiableEnvironment of(Set<Object> objects) {
        // Use HashSet to ensure modifiability.
        return new ModifiableEnvironment(new HashSet<>(objects));
    }

    /**
     * @return A brand new, empty modifiable environment.
     */
    public static ModifiableEnvironment empty() {
        return new ModifiableEnvironment(new HashSet<>());
    }
}
