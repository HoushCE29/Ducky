package com.houshce29.ducky.framework.core;

import com.google.common.collect.Sets;
import com.houshce29.ducky.exceptions.MultipleQualifyingObjectsFoundException;
import com.houshce29.ducky.exceptions.QualifiedObjectsNotFoundException;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TU_Environment extends BaseUnitTest {
    private static final Object DOG = new Dog();
    private static final Object MONKEY = new Monkey();
    private static final Object CHICKEN = new Chicken();
    private static final Object FISH = new Fish();
    private static final Object EXTRA = "EXTRA-OBJECT";
    private static final Environment environment = createEnvironment();

    @Test
    public void testGetAll() {
        // Get absolutely everything
        Assert.assertEquals(Sets.newHashSet(DOG, MONKEY, CHICKEN, FISH, EXTRA),
                environment.getAll(Object.class));

        // Get animals
        Assert.assertEquals(Sets.newHashSet(DOG, MONKEY, CHICKEN, FISH),
                environment.getAll(Animal.class));

        // Get mammals
        Assert.assertEquals(Sets.newHashSet(DOG, MONKEY),
                environment.getAll(Mammal.class));
    }

    @Test
    public void testGetAllNone() {
        Assert.assertTrue(environment.getAll(Plant.class).isEmpty());
    }

    @Test(expected = QualifiedObjectsNotFoundException.class)
    public void testGetAllErrorIfNone() {
        environment.getAll(Plant.class, true);
    }

    @Test
    public void testGet() {
        Assert.assertSame(DOG, environment.get(Dog.class));
    }

    @Test(expected = QualifiedObjectsNotFoundException.class)
    public void testGetNone() {
        environment.get(Plant.class);
    }

    @Test(expected = MultipleQualifyingObjectsFoundException.class)
    public void testGetMultipleError() {
        environment.get(Animal.class);
    }

    @Test
    public void testGetMultiple() {
        Assert.assertEquals(DOG, environment.get(Mammal.class, false));
    }

    @Test
    public void testCount() {
        Assert.assertEquals(5, environment.count(Object.class));
        Assert.assertEquals(4, environment.count(Animal.class));
        Assert.assertEquals(2, environment.count(Mammal.class));
    }

    @Test
    public void testContains() {
        Assert.assertTrue(environment.contains(Object.class));
        Assert.assertTrue(environment.contains(Animal.class));
        Assert.assertTrue(environment.contains(Mammal.class));
        Assert.assertFalse(environment.contains(Plant.class));
    }

    private static Environment createEnvironment() {
        Set<Object> objects = new HashSet<>();
        objects.add(DOG);
        objects.add(MONKEY);
        objects.add(CHICKEN);
        objects.add(FISH);
        objects.add(EXTRA);
        return ModifiableEnvironment.of(objects);
    }

    public interface Animal {
    }

    public interface Plant {
    }

    public static abstract class Mammal implements Animal {
    }

    public static class Dog extends Mammal {
    }

    public static class Monkey extends Mammal {
    }

    public static class Chicken implements Animal {
    }

    public static class Fish implements Animal {
    }
}
