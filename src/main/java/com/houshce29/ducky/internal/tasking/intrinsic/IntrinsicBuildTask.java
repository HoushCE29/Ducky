package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.exceptions.DuckyRuntimeException;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.MotherPlucker;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.BuildTask;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains a common build method used internally in the intrinsic package.
 */
public abstract class IntrinsicBuildTask extends BuildTask {
    private static final Logger LOGGER = LoggerFactory.get(IntrinsicBuildTask.class);

    IntrinsicBuildTask(String id) {
        super(id);
    }

    /**
     * Attempts to build the dependency and adds it to the environment if successful.
     * @param dep Dependency to maybe build.
     * @param environment AbstractEnvironment to add built object to.
     * @return `true` if the build was successful.
     */
    boolean build(Dependency<?> dep, ModifiableEnvironment environment) {
        Optional<List<Object>> args = maybeGetRequiredObjects(dep, environment);
        // If no required args, no need to bother trying to build...
        if (!args.isPresent()) {
            LOGGER.debug("Did NOT get all required objects to build [%s]. Aborting.", dep.getType());
            return false;
        }

        Optional<Object> build = dep.build(args.get());
        // If the build failed, return false.
        if (!build.isPresent()) {
            LOGGER.warn("Failed to build [%s].", dep.getType());
            return false;
        }

        environment.add(build.get());
        LOGGER.debug("Added new instance of [%s].", dep.getType());
        return true;
    }

    /**
     * Flushes out the built dependencies out of the given set.
     * @param dependencies Dependencies to flush.
     */
    void flushBuiltDependencies(Set<Dependency<?>> dependencies) {
        dependencies.removeIf(Dependency::wasSuccessfullyBuilt);
    }

    /**
     * Flushes built dependencies AND the priority set as they're built.
     * @param platform Platform to flush in.
     * @param fullScan Flag to denote the scope of the priority set flush. If `true`,
     *                 this will flush the priority set based on both the regular and
     *                 deferred dependency sets. If `false`, this will only flush based
     *                 on the deferred dependency set.
     */
    void flushBuiltDependenciesAndPriority(ProcessingPlatform platform, boolean fullScan) {
        flushBuiltDependencies(platform.mutableDeferredDependencySet());
        // Create a retention set
        Set<Class<?>> retain = platform.mutableDeferredDependencySet().stream()
                .map(Dependency::getType)
                .collect(Collectors.toSet());

        if (fullScan) {
            flushBuiltDependencies(platform.mutableDependencySet());
            platform.mutableDependencySet().stream()
                    .map(Dependency::getType)
                    .forEach(retain::add);
        }

        platform.mutablePriorityBuildSet().removeIf(type ->
                // Only remove if this priority item is completely satisfied.
                retain.stream().noneMatch(type::isAssignableFrom));
    }

    /**
     * Returns an optional of the ordered list of requirements to build a dependency.
     * @param dep Dependency to get required objects for.
     * @param environment Environment hosting built objects.
     * @return Optional maybe containing the list of requirements.
     */
    private Optional<List<Object>> maybeGetRequiredObjects(Dependency<?> dep,
                                                           Environment environment) {
        LOGGER.debug("Attempting to harvest required objects for [%s].", dep.getType());
        try {
            List<Object> required = new ArrayList<>();
            int param = 0;
            // Iterate in order through the requirements
            for (Class<?> requirement : dep.getRequirements()) {
                Object found;
                // If a Set, bounce back a Set of all available objects in the environment
                if (Set.class.equals(requirement)) {
                    found = environment.getAll(MotherPlucker.pluckSetType(dep.getConstructor(), param));
                    LOGGER.debug("Found collection type at param index [%d].", param);
                }
                else {
                    found = environment.get(requirement);
                    LOGGER.debug("Found object of type [%s].", found.getClass());
                }
                required.add(found);
                ++param;
            }
            LOGGER.debug("Found all required objects for [%s].", dep.getType());
            return Optional.of(required);
        }
        catch (DuckyRuntimeException ex) {
            // If some object is not found, or something else internally trips up,
            // then return nothing.
            return Optional.empty();
        }
    }
}
