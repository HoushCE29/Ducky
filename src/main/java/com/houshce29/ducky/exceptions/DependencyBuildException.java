package com.houshce29.ducky.exceptions;

import java.util.Collection;

/**
 * Exception thrown if an issue is detected during dependency building, namely
 * missing objects or circular paths.
 */
public class DependencyBuildException extends DuckyRuntimeException {
    private static final String MESSAGE = "Could not build this environment for remaining dependencies: %s";
    private static final String HELP = "Is this a circular or missing dependency?";

    public DependencyBuildException(Collection<?> remaining) {
        super(String.format(MESSAGE, remaining.toString()), HELP);
    }
}
