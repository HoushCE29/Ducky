package com.houshce29.ducky.internal;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a dependency.
 */
public final class Dependency<T> {
    private final Class<T> type;
    private final Constructor<T> constructor;
    private final List<Class<?>> requirements;
    private Set<Dependency<?>> watchSet = new HashSet<>();
    private boolean built = false;

    public Dependency(Class<T> type, Constructor<T> constructor) {
        this.type = type;
        this.constructor = constructor;
        this.requirements = Collections.unmodifiableList(
                Arrays.asList(this.constructor.getParameterTypes()));
    }

    /**
     * @return Ordered list of requirements.
     */
    public List<Class<?>> getRequirements() {
        return requirements;
    }

    /**
     * @return Type of this dependency.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * @return Constructor for this dependency.
     */
    public Constructor<T> getConstructor() {
        return constructor;
    }

    /**
     * Attempts to build this dependency into an object.
     * If successful, the optional will have an object present,
     * otherwise the optional will be empty.
     * @param args Arguments needed to build this dependency.
     * @return Optional maybe containing a built object.
     */
    public Optional<Object> build(List<Object> args) {
        try {
            Object build = constructor.newInstance(args.toArray());
            built = true;
            return Optional.of(build);
        }
        catch (Exception ex) {
            built = false;
            return Optional.empty();
        }
    }

    /**
     * @return `true` if this dependency was previously successfully built.
     */
    public boolean isSuccessfullyBuilt() {
        return built;
    }

    /**
     * Collects the dependency to watch for when this dependency can be built.
     * @param dependency Dependency to watch to determine when to build
     *                   this dependency.
     */
    public void link(Dependency<?> dependency) {
        watchSet.add(dependency);
    }

    /**
     * @return `true` if this dependency can be built.
     */
    public boolean canBuild() {
        return watchSet.stream()
                .allMatch(Dependency::isSuccessfullyBuilt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dependency)) {
            return false;
        }
        return ((Dependency) obj).getType().equals(type);
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
