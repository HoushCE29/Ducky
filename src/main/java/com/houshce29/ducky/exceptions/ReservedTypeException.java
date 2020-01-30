package com.houshce29.ducky.exceptions;

/**
 * Exception thrown for misuse of reserved types.
 */
public class ReservedTypeException extends DuckyRuntimeException {
    private static final String MESSAGE = "[%s] is a reserved type.";
    private static final String HELP = "Usage: %s.";

    public ReservedTypeException(Class<?> type, String usage) {
        super(String.format(MESSAGE, type), String.format(HELP, usage));
    }

    public ReservedTypeException(Class<?> type, String usage, Throwable cause) {
        super(String.format(MESSAGE, type), String.format(HELP, usage), cause);
    }
}
