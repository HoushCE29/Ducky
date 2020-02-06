package com.houshce29.ducky.internal;

import com.google.common.collect.Sets;
import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.exceptions.QualifiedObjectsNotFoundException;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class TU_DependencyBuilder extends BaseUnitTest {

    private DependencyBuilder builder;

    @Before
    public void beforeEach() {
        builder = new DependencyBuilder();
    }

    @Test
    public void testBuild() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class);
        link(deps, B.class, A.class);
        Set<Object> objects = builder.build(new HashSet<>(deps.values()), new HashSet<>());
        Assert.assertEquals(Sets.newHashSet(A.class, B.class),
                objects.stream().map(Object::getClass).collect(Collectors.toSet()));
    }

    @Test
    public void testBuildWithSet() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class, SharedObjects.class);
        link(deps, B.class, A.class);
        link(deps, SharedObjects.class, B.class);
        link(deps, SharedObjects.class, A.class);
        Set<Object> objects = builder.build(new HashSet<>(deps.values()), new HashSet<>());
        Assert.assertEquals(Sets.newHashSet(A.class, B.class, SharedObjects.class),
                objects.stream().map(Object::getClass).collect(Collectors.toSet()));
        Assert.assertEquals(objects.stream()
                        .filter(Share.class::isInstance)
                        .collect(Collectors.toSet()),
                objects.stream()
                        .filter(SharedObjects.class::isInstance)
                        .map(SharedObjects.class::cast)
                        .findFirst()
                        .get().set);
    }

    @Test
    public void testBuildWithObject() {
        Dependency<?> dep = createDep(B.class);
        Set<Object> objects = builder.build(Collections.singleton(dep), Collections.singleton(new A()));
        Assert.assertEquals(Sets.newHashSet(A.class, B.class),
                objects.stream().map(Object::getClass).collect(Collectors.toSet()));
    }

    @Test
    public void testBuildWithSetEmpty() {
        Dependency<?> dep = createDep(SharedObjects.class);
        Set<Object> objects = builder.build(Collections.singleton(dep), new HashSet<>());
        Assert.assertTrue(objects.stream()
                .filter(SharedObjects.class::isInstance)
                .map(SharedObjects.class::cast)
                .findFirst()
                .get()
                .set
                .isEmpty());
    }

    @Test(expected = DependencyBuildException.class)
    public void testBuildIterationFailed() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class);
        // Deadlock these.
        link(deps, B.class, A.class);
        link(deps, A.class, B.class);
        builder.build(new HashSet<>(deps.values()), new HashSet<>());
    }

    @Test(expected = QualifiedObjectsNotFoundException.class)
    public void testBuildMissingObject() {
        Dependency<?> dep = createDep(B.class);
        builder.build(Collections.singleton(dep), new HashSet<>());
    }

    private Map<Class<?>, Dependency<?>> create(Class<?>... types) {
        return Arrays.stream(types)
                .map(this::createDep)
                .collect(Collectors.toMap(Dependency::getType, Function.identity()));
    }

    private Dependency<?> createDep(Class<?> type) {
        return (Dependency<?>) new Dependency(type, type.getConstructors()[0]);
    }

    private void link(Map<Class<?>, Dependency<?>> deps, Class<?> from, Class<?> to) {
        deps.get(from).link(deps.get(to));
    }

    interface Share {
    }

    public static class A implements Share {
    }

    public static class B implements Share {
        public B(A a) {
        }
    }

    public static class SharedObjects {
        Set<Share> set;
        public SharedObjects(Set<Share> shared) {
            set = shared;
        }
    }
}
