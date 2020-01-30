package com.houshce29.ducky.meta.logging;

import com.houshce29.ducky.meta.Configuration;

/**
 * Wrapper that supports any logger framework.
 * The intent is to not force the consumer into
 * any particular framework or version of framework.
 */
public abstract class Logger {
    private final Class<?> forClass;
    public Logger(Class<?> forClass) {
        this.forClass = forClass;
    }

    public Class<?> getForClass() {
        return forClass;
    }

    public void debug(String format, Object... args) {
        if (shouldLog(Level.DEBUG)) {
            log(Level.DEBUG, String.format(format, args));
        }
    }

    public void info(String format, Object... args) {
        if (shouldLog(Level.INFO)) {
            log(Level.INFO, String.format(format, args));
        }
    }

    public void warn(String format, Object... args) {
        if (shouldLog(Level.WARN)) {
            log(Level.WARN, String.format(format, args));
        }
    }

    public void error(String format, Object... args) {
        if (shouldLog(Level.ERROR)) {
            log(Level.ERROR, String.format(format, args));
        }
    }

    public abstract void log(Level level, String message);

    private static boolean shouldLog(Level level) {
        return level.isWithinRangeOf(Configuration.getLoggingLevel());
    }
}
