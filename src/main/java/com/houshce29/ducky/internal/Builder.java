package com.houshce29.ducky.internal;

import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.LockedEnvironment;

import java.util.Set;

/**
 * Service that builds up dependencies into an environment.
 */
public class Builder {
    private final DependencyCreator creator;
    private final DependencyLinker linker;
    private final DependencyBuilder builder;

    private Builder() {
        creator = new DependencyCreator();
        linker = new DependencyLinker();
        builder = new DependencyBuilder();
    }

    /**
     * Builds the environment from the build set and included, pre-built objects.
     * @param buildSet Set of types to be built into objects.
     * @param objects Pre-built objects.
     * @return New environment.
     */
    public Environment build(Set<Class<?>> buildSet, Set<Object> objects) {
        Set<Dependency<?>> dependencies = creator.create(buildSet);
        linker.link(dependencies, objects);
        Set<Object> built = builder.build(dependencies, objects);
        return LockedEnvironment.of(built);
    }

    /**
     * @return New service instance.
     */
    public static Builder newInstance() {
        return new Builder();
    }

}
