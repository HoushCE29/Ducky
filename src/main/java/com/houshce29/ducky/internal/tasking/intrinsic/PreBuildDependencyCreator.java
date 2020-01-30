package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.exceptions.PublicConstructorsException;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.PreBuildTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task for creating dependency metadata.
 */
@SuppressWarnings("unchecked")
public final class PreBuildDependencyCreator extends PreBuildTask {
    public static final String ID = "RESERVED$ducky.intrinsic.PreBuildDependencyCreator";

    public PreBuildDependencyCreator() {
        super(ID);
    }

    @Override
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        platform.buildSet().stream()
                .map(type -> new Dependency(type, getRequirements(type)))
                .forEach(platform.mutableDependencySet()::add);
    }

    private <T> Constructor<T> getRequirements(Class<T> type) {
        // Determine how many public constructors there are.
        List<Constructor<T>> constructors = Arrays.stream(type.getConstructors())
                // derp
                .map(c -> (Constructor<T>) c)
                .filter(c -> c.getModifiers() == Modifier.PUBLIC)
                .collect(Collectors.toList());

        // If this is the case, we have no idea how to proceed.
        if (constructors.size() != 1) {
            throw new PublicConstructorsException(type, constructors.size());
        }

        return constructors.get(0);
    }
}
