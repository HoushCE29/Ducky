package com.houshce29.ducky.exceptions;

/**
 * Exception thrown if an invalid number of public constructors for a
 * dependency being built is found.
 */
public class PublicConstructorsException extends DuckyRuntimeException {
    private static final String MESSAGE
            = "Invalid amount of public constructors defined for [%s]: [%d]";
    private static final String HELP
            = "Define exactly 1 public constructor for registered classes.";

    public PublicConstructorsException(Class<?> type, int number) {
        super(String.format(MESSAGE, type.getCanonicalName(), number), HELP);
    }
}
