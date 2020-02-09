package com.houshce29.ducky.testing;

import com.houshce29.ducky.meta.Configuration;
import com.houshce29.ducky.meta.logging.Level;
import com.houshce29.ducky.meta.logging.impl.DefaultLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class BaseUnitTest {

    @BeforeClass
    public static void beforeAll() {
        Configuration.setLoggerProvider(DefaultLogger::new);
        Configuration.setLoggingLevel(Level.DEBUG);
    }

    @AfterClass
    public static void afterAll() {
        Configuration.reset();
    }
}
