package com.houshce29.ducky.exceptions;

/**
 * Abstract Ducky runtime exception to listen for.
 */
public abstract class DuckyRuntimeException extends RuntimeException {
    private String help = "If you believe this is a bug, please report it at: [https://github.com/HoushCE29/Ducky/issues].";

    DuckyRuntimeException(String message) {
        super(message);
    }

    DuckyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    DuckyRuntimeException(String message, String help) {
        this(message);
        this.help = help + "\n" + this.help;
    }

    DuckyRuntimeException(String message, String help, Throwable cause) {
        this(message, cause);
        this.help = help + "\n" + this.help;
    }

    public String getHelp() {
        return help;
    }
}
