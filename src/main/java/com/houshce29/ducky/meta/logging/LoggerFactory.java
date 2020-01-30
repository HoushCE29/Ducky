package com.houshce29.ducky.meta.logging;

import com.houshce29.ducky.meta.Configuration;

/**
 * Provides loggers for logging purposes.
 */
public final class LoggerFactory {

    /**
     * returns a logger for the given class.
     * @param forClass Class to get logger for.
     * @return New logger instance.
     */
    public static Logger get(Class<?> forClass) {
        return Configuration.getLoggerProvider().apply(forClass);
    }
}
