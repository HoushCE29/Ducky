package com.houshce29.ducky.meta;

import com.houshce29.ducky.meta.logging.Level;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.impl.DefaultLogger;
import com.houshce29.ducky.meta.logging.impl.NoopLogger;

import java.util.function.Function;

/**
 * Static/global configurations.
 */
public class Configuration {
    private static Function<Class<?>, ? extends Logger> loggerProvider;
    private static Level loggingLevel;

    static {
        reset();
    }

    // Prevent instantiation
    private Configuration() {
    }

    /**
     * Sets the logger provider for classes.
     * @param provider Logger provider.
     */
    public static void setLoggerProvider(Function<Class<?>, ? extends Logger> provider) {
        loggerProvider = provider;
    }

    public static Function<Class<?>, ? extends Logger> getLoggerProvider() {
        return loggerProvider;
    }

    /**
     * Sets the max logging level.
     *   1. ERROR
     *   2. WARN
     *   3. INFO
     *   4. DEBUG
     * @param level Max level of logging
     */
    public static void setLoggingLevel(Level level) {
        loggingLevel = level;
    }

    public static Level getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * Disables the logger. This can be undone
     * by either resetting the config or by setting
     * a new log provider.
     */
    public static void disableLogger() {
        loggerProvider = NoopLogger::new;
    }

    public static void reset() {
        loggerProvider = DefaultLogger::new;
        loggingLevel = Level.INFO;
    }
}
