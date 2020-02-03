package com.houshce29.ducky.internal;

import com.houshce29.ducky.exceptions.CircularDependencyException;
import com.houshce29.ducky.exceptions.MultipleQualifyingObjectsFoundException;
import com.houshce29.ducky.exceptions.QualifiedObjectsNotFoundException;
import com.houshce29.ducky.internal.processing.MotherPlucker;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service that links dependencies together to form a full dependency graph
 * used to schedule and determine when a dependency can be built.
 */
class DependencyLinker {
    private static final Logger LOGGER = LoggerFactory.get(DependencyLinker.class);

    /**
     * Performs the linking for all dependencies.
     * @param dependencies Dependencies to link together.
     * @param objects Pre-built objects that aid in determining if too many
     *                of a requested type exists, or none at all.
     */
    void link(Set<Dependency<?>> dependencies, Set<Object> objects) {
        for (Dependency<?> dependency : dependencies) {
            LOGGER.info("** Linking [%s] for build.", dependency);
            link(dependency, new HashSet<>(), dependencies, objects);
        }
    }

    /**
     * Recursive method that performs the linking of a dependency.
     * @param linker Dependency linking to its child dependencies.
     * @param trail Bread-crumb trail used to determine if a circular dependency is present.
     * @param dependencies Full set of dependencies.
     * @param objects Full set of pre-built objects.
     */
    private void link(Dependency<?> linker,
                      Set<Dependency<?>> trail,
                      Set<Dependency<?>> dependencies,
                      Set<Object> objects) {

        if (trail.contains(linker)) {
            LOGGER.error("CIRCULAR DEPENDENCY FOUND!");
            throw new CircularDependencyException(linker, trail);
        }
        if (!linker.getRequirements().isEmpty()) {
            trail.add(linker);
            LOGGER.debug("Trail: %s", trail);
            for (Dependency<?> child : getChildren(linker, dependencies, objects)) {
                linker.link(child);
                LOGGER.debug("Linked [%s] to [%s]", linker, child);
                link(child, trail, dependencies, objects);
            }
            trail.remove(linker);
        }
    }

    /**
     * Returns all the child dependencies for a given dependency.
     * Note that dependencies partially satisfied by an object
     * will NOT return the object in the form of another dependency.
     * @param dep Dependency to pull children from.
     * @param dependencies Full set of dependencies.
     * @param objects Full set of pre-built objects.
     * @return Set of child dependencies.
     */
    Set<Dependency<?>> getChildren(Dependency<?> dep,
                                   Set<Dependency<?>> dependencies,
                                   Set<Object> objects) {

        Set<Dependency<?>> children = new HashSet<>();
        int depsFound = 0;
        int param = 0;
        for (Class<?> baseType : dep.getRequirements()) {
            boolean collectionType = false;
            Class<?> type = baseType;

            // Special handling for Set. We want to flatten this structure.
            if (Set.class.equals(baseType)) {
                collectionType = true;
                type = MotherPlucker.pluckSetType(dep.getConstructor(), param);
            }

            int objectCount = getTotalBuiltObjects(type, objects);
            children.addAll(getDependencies(type, dependencies));
            // If not of a collection type
            if (!collectionType) {
                int found = (children.size() - depsFound) + objectCount;
                // No deps / objects found
                if (found == 0) {
                    LOGGER.error("Cannot find target object/dependency.");
                    throw new QualifiedObjectsNotFoundException(type);
                }
                // Too many objects or deps found
                if (found > 1) {
                    LOGGER.error("Multiple objects/dependencies found.");
                    throw new MultipleQualifyingObjectsFoundException(type);
                }
            }
            depsFound = children.size();
            param++;
        }
        return children;
    }

    /**
     * Returns the filtered set of dependencies of the given type.
     * @param type Type of dependency to include.
     * @param dependencies Full set of dependencies.
     * @return Filtered set of dependencies.
     */
    private Set<Dependency<?>> getDependencies(Class<?> type, Set<Dependency<?>> dependencies) {
        return dependencies.stream()
                .filter(dep -> type.isAssignableFrom(dep.getType()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns the number of objects of the given type.
     * @param type Type of object to count.
     * @param objects Full set of pre-built objects.
     * @return Number of objects of the given type.
     */
    private int getTotalBuiltObjects(Class<?> type, Set<Object> objects) {
        return (int) objects.stream()
                .filter(type::isInstance)
                .count();
    }
}
