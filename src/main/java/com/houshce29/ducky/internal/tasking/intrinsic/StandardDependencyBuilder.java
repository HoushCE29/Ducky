package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

/**
 * Task that runs the standard build of dependencies.
 */
public final class StandardDependencyBuilder extends IntrinsicBuildTask {
    private static final Logger LOGGER = LoggerFactory.get(StandardDependencyBuilder.class);
    public static final String ID = "RESERVED$ducky.intrinsic.StandardDependencyBuilder";

    public StandardDependencyBuilder() {
        super(ID);
    }

    @Override
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        // Keep running until the dependency set is empty.
        // We will remove the dependencies from this set as they get
        // built and added to the environment.
        while (!platform.mutableDependencySet().isEmpty()) {
            long iterationBuilds = platform.mutableDependencySet().stream()
                    .filter(dep -> build(dep, environment))
                    .count();

            // If no builds this iteration, then a dependency is missing or a circular
            // dependency is locking up the build process.
            if (iterationBuilds == 0L) {
                LOGGER.error("FAILED TO BUILD DEPENDENCIES.");
                throw new DependencyBuildException(platform.mutableDependencySet());
            }

            LOGGER.debug("Built [%d] objects this iteration.", iterationBuilds);

            // Remove the dependencies that have been built.

            platform.mutableDependencySet().stream()
                    .filter(Dependency::wasSuccessfullyBuilt)
                    .map(Dependency::getType)
                    .forEach(platform.mutablePriorityBuildSet()::remove);

            platform.mutableDependencySet().removeIf(Dependency::wasSuccessfullyBuilt);
        }
    }
}
