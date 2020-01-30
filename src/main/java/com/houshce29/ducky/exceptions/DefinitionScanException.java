package com.houshce29.ducky.exceptions;

/**
 * Exception thrown if definition can't be scanned.
 */
public class DefinitionScanException extends DuckyRuntimeException {
    private static final String MESSAGE = "Failed to scan definition [%s].";
    private static final String HELP = "Be sure that there's a default accessible constructor.";

    public DefinitionScanException(Class<?> definitionType, Throwable cause) {
        super(String.format(MESSAGE, definitionType), HELP, cause);
    }
}
