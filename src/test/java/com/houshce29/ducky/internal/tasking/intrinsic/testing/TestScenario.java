package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.internal.processing.Dependency;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class TestScenario {
    private final String name;
    private Class<? extends Throwable> expected;
    private Set<Class<?>> buildSet;
    private Set<Dependency<Class<?>>> dependencies;

    TestScenario(String name, Class<? extends Throwable> expected, Class<?>... buildSet) {
        this.buildSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(buildSet)));
        this.dependencies = Collections.unmodifiableSet(
                this.buildSet.stream()
                        .map(clazz -> new Dependency(clazz, clazz.getConstructors()[0]))
                        .map(dep -> (Dependency<Class<?>>) dep)
                        .collect(Collectors.toSet()));
        this.name = name;
        this.expected = expected;
    }

    public final Set<Class<?>> getBuildSet() {
        return buildSet;
    }

    public final Set<Dependency<Class<?>>> getDependencies() {
        return dependencies;
    }

    public final String getName() {
        return name;
    }

    public final Class<? extends Throwable> getExpectedException() {
        return expected;
    }

    public abstract void validateBuild(Environment result);
}
