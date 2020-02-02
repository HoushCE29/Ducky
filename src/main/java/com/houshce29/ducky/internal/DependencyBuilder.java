package com.houshce29.ducky.internal;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.exceptions.QualifiedObjectsNotFoundException;
import com.houshce29.ducky.internal.processing.MotherPlucker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service that builds dependencies into objects.
 */
class DependencyBuilder {

    /**
     * Builds dependencies into objects.
     * @param dependencies Dependencies to build into objects.
     * @param objects Pre-built objects.
     * @return Set of built objects.
     */
    Set<Object> build(Set<Dependency<?>> dependencies, Set<Object> objects) {
        Set<Object> build = new HashSet<>(objects);
        Set<Dependency<?>> remaining = new HashSet<>(dependencies);

        while(!remaining.isEmpty()) {
            long iterationBuilds = remaining.stream()
                    .filter(Dependency::canBuild)
                    .filter(dep -> build(dep, objects))
                    .count();

            if (iterationBuilds == 0L) {
                throw new DependencyBuildException(remaining);
            }

            remaining.removeIf(Dependency::isSuccessfullyBuilt);
        }

        return build;
    }

    /**
     * Builds the given dependency.
     * @param dep Dependency to build.
     * @param objects All pre-built and built objects.
     * @return `true` if the build was a success.
     */
    private boolean build(Dependency<?> dep, Set<Object> objects) {
        List<Object> args = getRequiredObjects(dep, objects);
        Optional<Object> build = dep.build(args);
        if (!build.isPresent()) {
            return false;
        }
        objects.add(build.get());
        return true;
    }

    /**
     * Returns ordered list of required objects.
     * @param dep Dependency to get required objects for.
     * @param objects Built objects that can be used to build dependency.
     * @return Required objects to build dependency.
     */
    private List<Object> getRequiredObjects(Dependency<?> dep,
                                            Set<Object> objects) {

        List<Object> required = new ArrayList<>();
        int param = 0;
        // Iterate in order through the requirements
        for (Class<?> requirement : dep.getRequirements()) {
            Object found;
            // If a Set, bounce back a Set of all available objects in the environment
            if (Set.class.equals(requirement)) {
                Class<?> filter = MotherPlucker.pluckSetType(dep.getConstructor(), param);
                found = objects.stream()
                        .filter(filter::isInstance)
                        .collect(Collectors.toSet());
            }
            else {
                found = objects.stream()
                        .filter(requirement::isInstance)
                        .findFirst()
                        .orElseThrow(() -> new QualifiedObjectsNotFoundException(requirement));
            }
            required.add(found);
            ++param;
        }
        return required;
    }
}
