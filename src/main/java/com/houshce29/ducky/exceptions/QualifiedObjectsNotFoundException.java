package com.houshce29.ducky.exceptions;

/**
 * Exception thrown if no objects were registered for the target.
 */
public class QualifiedObjectsNotFoundException extends DuckyRuntimeException {
    private static final String MESSAGE = "No qualified objects found for: [%s].";
    private static final String HELP = "Is this class registered?";

    public QualifiedObjectsNotFoundException(Class<?> target) {
        super(String.format(MESSAGE, target.getCanonicalName()), HELP);
    }
}
