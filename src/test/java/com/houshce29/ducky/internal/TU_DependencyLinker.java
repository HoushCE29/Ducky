package com.houshce29.ducky.internal;

import com.google.common.collect.Sets;
import com.houshce29.ducky.exceptions.CircularDependencyException;
import com.houshce29.ducky.exceptions.MultipleQualifyingObjectsFoundException;
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
public class TU_DependencyLinker extends BaseUnitTest {

    private DependencyLinker linker;

    @Before
    public void beforeEach() {
        linker = new DependencyLinker();
    }

    @Test
    public void testLink() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class);
        linker.link(new HashSet<>(deps.values()), new HashSet<>());

        // Shouldn't be able to build now due to
        // being linked to A
        Assert.assertFalse(deps.get(B.class).canBuild());
    }

    @Test(expected = CircularDependencyException.class)
    public void testLinkCircularDependencyFound() {
        Map<Class<?>, Dependency<?>> deps = create(X.class, Y.class);
        linker.link(new HashSet<>(deps.values()), new HashSet<>());
    }

    @Test(expected = CircularDependencyException.class)
    public void testLinkCircularDependencyOnSelfFound() {
        Dependency<?> dep = createDep(Self.class);
        linker.link(Collections.singleton(dep), new HashSet<>());
    }

    @Test
    public void testGetChildren() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class);
        Set<Dependency<?>> child = linker.getChildren(deps.get(B.class),
                new HashSet<>(deps.values()), new HashSet<>());

        Assert.assertEquals(Collections.singleton(deps.get(A.class)), child);
    }

    @Test
    public void testGetChildrenWithSet() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class, Ab.class);
        Set<Dependency<?>> children = linker.getChildren(deps.get(Ab.class),
                new HashSet<>(deps.values()), new HashSet<>());

        Assert.assertEquals(Sets.newHashSet(deps.get(A.class), deps.get(B.class)), children);
    }

    @Test
    public void testGetChildrenWithSetNoneFound() {
        Map<Class<?>, Dependency<?>> deps = create(Ab.class);
        Set<Dependency<?>> children = linker.getChildren(deps.get(Ab.class),
                new HashSet<>(deps.values()), new HashSet<>());

        Assert.assertTrue(children.isEmpty());
    }

    @Test
    public void testGetChildrenObjectsAlreadyExist() {
        Dependency<?> dep = createDep(B.class);
        Set<Dependency<?>> children = linker.getChildren(dep,
                Collections.singleton(dep), Collections.singleton(new A()));

        Assert.assertTrue(children.isEmpty());
    }

    @Test(expected = QualifiedObjectsNotFoundException.class)
    public void testGetChildrenMissingDependencyOrObject() {
        Dependency<?> dep = createDep(B.class);
        linker.getChildren(dep, Collections.singleton(dep), new HashSet<>());
    }

    @Test(expected = MultipleQualifyingObjectsFoundException.class)
    public void testGetChildrenTooManyQualifiedDependencies() {
        Map<Class<?>, Dependency<?>> deps = create(A.class, B.class, AbExclusive.class);
        linker.getChildren(deps.get(AbExclusive.class), new HashSet<>(deps.values()), new HashSet<>());
    }

    @Test(expected = MultipleQualifyingObjectsFoundException.class)
    public void testGetChildrenTooManyQualifiedObjects() {
        Dependency<?> dep = createDep(AbExclusive.class);
        linker.getChildren(dep, Collections.singleton(dep),
                Sets.newHashSet(new A(), new B(new A())));
    }

    private Map<Class<?>, Dependency<?>> create(Class<?>... types) {
        return Arrays.stream(types)
                .map(this::createDep)
                .collect(Collectors.toMap(Dependency::getType, Function.identity()));
    }

    private Dependency<?> createDep(Class<?> type) {
        return (Dependency<?>) new Dependency(type, type.getConstructors()[0]);
    }

    interface Letter {
    }

    public static class A implements Letter {
    }

    public static class B implements Letter {
        public B(A a) {
        }
    }

    public static class Ab {
        public Ab(Set<Letter> ab) {
        }
    }

    public static class AbExclusive {
        public AbExclusive(Letter aOrB) {
        }
    }

    public static class X {
        public X(Y y) {
        }
    }

    public static class Y {
        public Y(X x) {
        }
    }

    public static class Self {
        public Self(Self self) {
        }
    }
}
