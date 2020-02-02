package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

/**
 * Task that runs the very last set of dependency builds.
 */
public final class DeferredDependencyBuilder extends IntrinsicBuildTask {
    private static final Logger LOGGER = LoggerFactory.get(DeferredDependencyBuilder.class);
    public static final String ID = "RESERVED$ducky.intrinsic.DeferredDependencyBuilder";

    public DeferredDependencyBuilder() {
        super(ID);
    }

    @Override
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        // Keep running until the dependency set is empty.
        // We will remove the dependencies from this set as they get
        // built and added to the environment.
        while (!platform.mutableDeferredDependencySet().isEmpty()) {
            long iterationBuilds = platform.mutableDeferredDependencySet()
                    .stream()
                    .filter(dep -> build(dep, environment))
                    .count();

            // If no builds this iteration, then a dependency is missing or a circular
            // dependency is locking up the build process.
            if (iterationBuilds == 0L) {
                LOGGER.error("FAILED TO BUILD DEFERRED DEPENDENCIES.");
                throw new DependencyBuildException(platform.mutableDeferredDependencySet());
            }

            LOGGER.debug("Built [%d] objects this iteration.", iterationBuilds);

            // Remove the dependencies that have been built.
            flushBuiltDependencies(platform.mutableDeferredDependencySet());
        }
    }
}
