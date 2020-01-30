package com.houshce29.ducky.meta.logging;

/**
 * Level of logging.
 */
public enum Level {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3);

    private int val;

    Level(int val) {
        this.val = val;
    }

    private int toInteger() {
        return val;
    }

    public boolean isWithinRangeOf(Level level) {
        return this.toInteger() >= level.toInteger();
    }
}
