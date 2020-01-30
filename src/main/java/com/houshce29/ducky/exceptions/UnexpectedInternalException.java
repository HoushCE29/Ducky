package com.houshce29.ducky.exceptions;

/**
 * Exception thrown if some unexpected issue arises.
 */
public class UnexpectedInternalException extends DuckyRuntimeException {
    private static final String MESSAGE = "An unexpected error has occurred internally.";

    public UnexpectedInternalException() {
        super(MESSAGE);
    }

    public UnexpectedInternalException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
