package com.houshce29.ducky.internal.processing;

import com.houshce29.ducky.exceptions.ReservedTypeException;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

public class TU_MotherPlucker {
    private static final Constructor<?> CONSTRUCTOR = Foo.class.getConstructors()[0];

    @Test
    public void testPluck() {
        Class<?> clazz = MotherPlucker.pluckSetType(CONSTRUCTOR, 1);
        Assert.assertEquals(String.class, clazz);
    }

    @Test(expected = ReservedTypeException.class)
    public void testPluckWrongParameter() {
        MotherPlucker.pluckSetType(CONSTRUCTOR, 0);
    }

    @Test(expected = ReservedTypeException.class)
    public void testPluckParameterOutOfBounds() {
        MotherPlucker.pluckSetType(CONSTRUCTOR, CONSTRUCTOR.getParameterCount() + 999);
    }

    private static final class Foo {
        public Foo(int i, Set<String> set, String s) {
        }
    }
}
