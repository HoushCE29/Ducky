package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

/**
 * Task that runs priority deferred dependency build.
 */
public final class PriorityDeferredDependencyBuilder extends IntrinsicBuildTask {
    private static final Logger LOGGER = LoggerFactory.get(PriorityDeferredDependencyBuilder.class);
    public static final String ID = "RESERVED$ducky.intrinsic.PriorityDeferredDependencyBuilder";

    public PriorityDeferredDependencyBuilder() {
        super(ID);
    }

    @Override
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        // Keep running until the priority set is empty.
        while (!platform.mutablePriorityBuildSet().isEmpty()) {
            long iterationBuilds = platform.mutableDeferredDependencySet()
                    .stream()
                    .filter(dep -> build(dep, environment))
                    .count();

            // If no builds this iteration, then a dependency is missing or a circular
            // dependency is locking up the build process.
            if (iterationBuilds == 0L) {
                LOGGER.error("FAILED TO BUILD HIGH PRIORITY DEFERRED DEPENDENCIES.");
                throw new DependencyBuildException(platform.mutableDeferredDependencySet());
            }

            LOGGER.debug("Built [%d] objects this iteration.", iterationBuilds);

            // Remove the dependencies that have been built.

            platform.mutableDeferredDependencySet().stream()
                    .filter(Dependency::wasSuccessfullyBuilt)
                    .map(Dependency::getType)
                    .forEach(platform.mutablePriorityBuildSet()::remove);

            platform.mutableDeferredDependencySet()
                    .removeIf(Dependency::wasSuccessfullyBuilt);
        }
    }
}