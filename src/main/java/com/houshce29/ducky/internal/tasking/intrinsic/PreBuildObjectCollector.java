package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.PreBuildTask;

/**
 * Task for collecting pre-built objects.
 */
public final class PreBuildObjectCollector extends PreBuildTask {
    public static final String ID = "RESERVED$ducky.intrinsic.PreBuildObjectCollector";

    public PreBuildObjectCollector() {
        super(ID);
    }

    @Override
    public void run(ProcessingPlatform platform, ModifiableEnvironment environment) {
        platform.objects().forEach(environment::add);
    }
}
