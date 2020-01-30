package com.houshce29.ducky.meta.logging.impl;

import com.houshce29.ducky.meta.logging.Level;
import com.houshce29.ducky.meta.logging.Logger;

import java.util.Date;

/**
 * Default logger.
 */
public class DefaultLogger extends Logger {
    private static final String FORMAT = "%s  %s  %s:\t%s";

    public DefaultLogger(Class<?> forClass) {
        super(forClass);
    }

    @Override
    public void log(Level level, String message) {
        System.out.println(String.format(FORMAT,
                new Date(), level, getForClass(), message));
    }
}
