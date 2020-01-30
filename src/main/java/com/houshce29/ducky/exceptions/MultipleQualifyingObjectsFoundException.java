package com.houshce29.ducky.exceptions;

/**
 * Exception thrown if multiple qualifying objects are found for a given class
 * when only a single object was requested.
 */
public class MultipleQualifyingObjectsFoundException extends DuckyRuntimeException {
    private static final String MESSAGE = "Multiple objects registered as [%s].";
    private static final String HELP =
            "Try requesting a more specific type, or create an interface to request by instead.";

    public MultipleQualifyingObjectsFoundException(Class<?> target) {
        super(String.format(MESSAGE, target.getCanonicalName()), HELP);
    }
}
