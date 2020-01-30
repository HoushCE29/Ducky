package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.MotherPlucker;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.PreBuildTask;
import com.houshce29.ducky.meta.logging.Logger;
import com.houshce29.ducky.meta.logging.LoggerFactory;

import java.util.Set;

/**
 * Task that moves dependencies requiring to be deferred from the
 * standard building set to the deferred building set.
 */
public final class PreBuildDependencyDeferrer extends PreBuildTask {
    private static final Logger LOGGER = LoggerFactory.get(PreBuildDependencyDeferrer.class);
    public static final String ID = "RESERVED$ducky.intrinsic.PreBuildDependencyDeferrer";
    public PreBuildDependencyDeferrer() {
        super(ID);
    }

    @Override
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        // First, move required deferred builds to the deferred set.
        platform.mutableDependencySet().stream()
                .filter(dep -> deferIfRequired(dep, platform))
                .forEach(platform.mutableDeferredDependencySet()::add);

        // Then, remove the deferred builds from the main build set.
        platform.mutableDependencySet().removeAll(platform.mutableDeferredDependencySet());
    }

    /**
     * Determines if the dependency should be deferred. This also provides
     * a means to determine the order to build the deferred dependencies by
     * populating the processing platform's priority build set.
     * @param dependency Dependency to determine if deferring is required for.
     * @param platform Processing data.
     * @return `true` if deferring is required.
     */
    private boolean deferIfRequired(Dependency<?> dependency, ProcessingPlatform platform) {
        // Flag to denote if the dependency's build should be deferred
        boolean defer = false;
        // Counter for the param, in case it needs to be examined
        int param = 0;
        for (Class<?> requirement : dependency.getRequirements()) {
            // If the requirement is a set of something
            if (Set.class.equals(requirement)) {
                LOGGER.debug("Deferring build of [%s] if not already deferred...",
                        dependency.getType());
                // Tell the task to defer this dependency's build.
                defer = true;
                Class<?> setType = MotherPlucker.pluckSetType(dependency.getConstructor(), param);
                LOGGER.debug("Pushing [%s] into the priority build set.", setType);
                platform.mutablePriorityBuildSet().add(setType);
            }
            ++param;
        }
        return defer;
    }
}
