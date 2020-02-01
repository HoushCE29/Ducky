package com.houshce29.ducky.internal.processing;

import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class TU_Dependency extends BaseUnitTest {
    private static final Object ARG = "VALUE";
    private static final Class<TestObject> CLAZZ = TestObject.class;
    private static final Constructor<TestObject> CONSTRUCTOR =
            (Constructor<TestObject>) CLAZZ.getConstructors()[0];

    private Dependency<TestObject> dependency;

    @Before
    public void beforeEach() {
        dependency = new Dependency<>(CLAZZ, CONSTRUCTOR);
    }

    @Test
    public void testSuccessfulBuild() {
        Optional<Object> build = dependency.build(Collections.singletonList(ARG));
        Assert.assertTrue(build.isPresent());
        Assert.assertTrue(dependency.wasSuccessfullyBuilt());

        TestObject object = (TestObject) build.get();
        Assert.assertEquals(ARG, object.getValue());
    }

    @Test
    public void testFailBuild() {
        Optional<Object> build = dependency.build(Collections.emptyList());
        Assert.assertFalse(build.isPresent());
        Assert.assertFalse(dependency.wasSuccessfullyBuilt());
    }

    @Test
    public void testSequenceOfBuilds() {
        // Fail build
        Optional<Object> build = dependency.build(Collections.emptyList());
        Assert.assertFalse(build.isPresent());
        Assert.assertFalse(dependency.wasSuccessfullyBuilt());

        // Pass build
        build = dependency.build(Collections.singletonList(ARG));
        Assert.assertTrue(build.isPresent());
        Assert.assertTrue(dependency.wasSuccessfullyBuilt());

        TestObject object = (TestObject) build.get();
        Assert.assertEquals(ARG, object.getValue());

        // Fail build
        build = dependency.build(Collections.singletonList(ARG));
        Assert.assertTrue(build.isPresent());
        Assert.assertTrue(dependency.wasSuccessfullyBuilt());

        object = (TestObject) build.get();
        Assert.assertEquals(ARG, object.getValue());
    }

    private static final class TestObject {
        private final String value;
        public TestObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
