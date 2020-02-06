package com.houshce29.ducky.exceptions;

import com.houshce29.ducky.internal.Dependency;

import java.util.Set;

/**
 * Exception thrown for detected circular dependencies.
 */
public class CircularDependencyException extends DuckyRuntimeException {
    private static final String MESSAGE = "Circular dependency found on [%s] through trail: %s";

    public CircularDependencyException(Dependency<?> circular, Set<Dependency<?>> trail) {
        super(String.format(MESSAGE, circular, trail));
    }
}
