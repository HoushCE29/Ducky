package com.houshce29.ducky.internal;

import com.houshce29.ducky.exceptions.PublicConstructorsException;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service that creates dependencies.
 */
@SuppressWarnings("unchecked")
class DependencyCreator {
    private static final Logger LOGGER = LoggerFactory.get(DependencyCreator.class);

    /**
     * Creates a dependency set from the given build set.
     * @param buildSet Set of types to be built.
     * @return Set of dependencies.
     */
    Set<Dependency<?>> create(Set<Class<?>> buildSet) {
        return buildSet.stream()
                .map(type -> new Dependency(type, getRequirements(type)))
                // derp
                .map(dep -> (Dependency<?>) dep)
                .peek(dep -> LOGGER.info("* Created dependency [%s].", dep))
                .collect(Collectors.toSet());
    }

    /**
     * Gets the requirements for building the given type.
     * @param type Type to build.
     * @param <T> Type to build.
     * @return Constructor, bearing the object requirements.
     */
    private <T> Constructor<T> getRequirements(Class<T> type) {
        // Determine how many public constructors there are.
        List<Constructor<T>> constructors = Arrays.stream(type.getConstructors())
                // derp
                .map(c -> (Constructor<T>) c)
                .filter(c -> c.getModifiers() == Modifier.PUBLIC)
                .collect(Collectors.toList());

        // If this is the case, we have no idea how to proceed.
        if (constructors.size() != 1) {
            LOGGER.error("Cannot proceed with the incorrect number of public constructors.");
            throw new PublicConstructorsException(type, constructors.size());
        }

        return constructors.get(0);
    }
}
