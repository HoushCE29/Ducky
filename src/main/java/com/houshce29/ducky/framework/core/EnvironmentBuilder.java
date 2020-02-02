package com.houshce29.ducky.framework.core;

import com.houshce29.ducky.exceptions.DefinitionScanException;
import com.houshce29.ducky.internal.Builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder to build an Environment instance.
 */
public class EnvironmentBuilder {
    // Set of already built objects.
    private final Set<Object> included = new HashSet<>();

    // Set of injected types that need to be built into objects.
    private final Set<Class<?>> injections = new HashSet<>();

    EnvironmentBuilder() {
    }

    /**
     * Defines classes that should be built into objects
     * for the environment. These can have constructors that
     * inject other included objects or built injections. In
     * turn, the injected classes here can be used as dependencies.
     * @param injections Classes to be build into objects.
     * @return This builder.
     */
    public EnvironmentBuilder inject(Class<?>... injections) {
        this.injections.addAll(Arrays.asList(injections));
        return this;
    }

    /**
     * Defines objects that are already built to be used in
     * the environment. These can be injected into injections.
     * @param included Pre-built objects.
     * @return This builder.
     */
    public EnvironmentBuilder include(Object... included) {
        this.included.addAll(Arrays.asList(included));
        return this;
    }

    /**
     * Convenience definition of included objects. This is used to tidy
     * up the build statements by pushing a bunch of included objects into
     * a different class. The objects are read by creating an instance of
     * the definition then pulling the result of the `get()` method.
     * @param definition External definition of objects to include.
     * @return This builder.
     */
    public EnvironmentBuilder includeFrom(Class<? extends IncludeDefinition> definition) {
        IncludeDefinition instance = buildOrThrowException(definition);
        this.included.addAll(instance.get());
        return this;
    }

    /**
     * Convenience definition of injections. This is used to tidy
     * up the build statements by pushing a bunch of injections into
     * a different class. The injections are read by creating an instance of
     * the definition then pulling the result of the `get()` method.
     * @param definition External definition of injections.
     * @return This builder.
     */
    public EnvironmentBuilder injectFrom(Class<? extends InjectionDefinition> definition) {
        InjectionDefinition instance = buildOrThrowException(definition);
        this.injections.addAll(instance.get());
        return this;
    }

    /**
     * @return A new environment instance.
     */
    public Environment build() {
        return Builder.newInstance().build(injections, included);
    }

    private <T> T buildOrThrowException(Class<T> type) {
        try {
            return type.newInstance();
        }
        catch (Exception ex) {
            throw new DefinitionScanException(type, ex);
        }
    }
}
