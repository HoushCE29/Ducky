package com.houshce29.ducky.meta.logging.impl;

import com.houshce29.ducky.meta.logging.Level;
import com.houshce29.ducky.meta.logging.Logger;

/**
 * Silenced logger for those who wish to live
 * in quiet peace.
 */
public class NoopLogger extends Logger {
    public NoopLogger(Class<?> ignoreThisIsForConvenience) {
        super(null);
    }

    @Override
    public void debug(String format, Object... args) {
        // Noop
    }

    @Override
    public void info(String format, Object... args) {
        // Noop
    }

    @Override
    public void warn(String format, Object... args) {
        // Noop
    }

    @Override
    public void error(String format, Object... args) {
        // Noop
    }

    @Override
    public void log(Level level, String message) {
        // Noop
    }
}
