package com.houshce29.ducky.internal.processing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Modifiable data transfer object containing various contextual data.
 */
public final class ProcessingPlatform {
    private Set<Object> objects;
    private Set<Class<?>> buildSet;
    private Set<Dependency<?>> dependencySet = new HashSet<>();
    private Set<Dependency<?>> deferredDependencySet = new HashSet<>();
    private Set<Class<?>> priorityBuildSet = new HashSet<>();
    private Map<String, Object> variables = new HashMap<>();

    private ProcessingPlatform(Set<Object> objects, Set<Class<?>> buildSet) {
        this.objects = objects;
        this.buildSet = buildSet;
    }

    /**
     * @return Set of pre-built objects.
     */
    public Set<Object> objects() {
        return objects;
    }

    /**
     * @return Set of types to be built.
     */
    public Set<Class<?>> buildSet() {
        return buildSet;
    }

    /**
     * @return Set of dependencies to be built. This set will grow pre-build,
     *         and shrink to nothing by post-build.
     */
    public Set<Dependency<?>> mutableDependencySet() {
        return dependencySet;
    }

    /**
     * @return Set of deferred dependencies to be built last. This set will grow
     *         pre-build, and shrink to nothing by post-build.
     */
    public Set<Dependency<?>> mutableDeferredDependencySet() {
        return deferredDependencySet;
    }

    /**
     * @return Set of types that are priority to be built.
     *         This set can contain generic types (e.g. abstract classes, interfaces).
     *         In reality, this set is only _read_ when building deferred dependencies to determine
     *         which ones to build first.
     */
    public Set<Class<?>> mutablePriorityBuildSet() {
        return priorityBuildSet;
    }

    /**
     * @return Misc. data.
     */
    public Map<String, Object> variables() {
        return variables;
    }

    /**
     * Creates a new processing platform.
     * @param objects Pre-built objects.
     * @param buildSet Set of types to be built.
     * @return New processing platform.
     */
    public static ProcessingPlatform create(Set<Object> objects, Set<Class<?>> buildSet) {
        return new ProcessingPlatform(objects, buildSet);
    }
}
