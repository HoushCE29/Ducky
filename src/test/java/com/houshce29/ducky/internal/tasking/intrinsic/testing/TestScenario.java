package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.google.common.collect.ImmutableSet;
import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class TestScenario {
    private final String name;
    private Class<? extends Throwable> expected;
    private Set<Class<?>> buildSet;
    private Map<Class<?>, Dependency<?>> dependencyMap;
    private Set<Dependency<?>> dependencies;

    TestScenario(String name, Class<? extends Throwable> expected, Class<?>... buildSet) {
        this.buildSet = ImmutableSet.copyOf(buildSet);
        this.dependencyMap = this.buildSet.stream()
                .map(clazz -> new Dependency(clazz, clazz.getConstructors()[0]))
                .map(dep -> (Dependency<?>) dep)
                .collect(Collectors.toMap(Dependency::getType, Function.identity()));

        this.dependencies = ImmutableSet.copyOf(this.dependencyMap.values());
        this.name = name;
        this.expected = expected;
    }

    public final Set<Class<?>> getBuildSet() {
        return buildSet;
    }

    public final Set<Dependency<?>> getDependencies() {
        return dependencies;
    }

    public final Map<Class<?>, Dependency<?>> getDependencyMap() {
        return dependencyMap;
    }

    public final String getName() {
        return name;
    }

    public final Class<? extends Throwable> getExpectedException() {
        return expected;
    }

    public abstract void initForBuild(ProcessingPlatform platform,
                                      ModifiableEnvironment environment,
                                      boolean deferred);

    public abstract void validateBuild(Environment result);
}
