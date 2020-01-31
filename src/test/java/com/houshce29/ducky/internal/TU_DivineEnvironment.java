package com.houshce29.ducky.internal;

import com.houshce29.ducky.internal.tasking.intrinsic.DeferredDependencyBuilder;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildDependencyCreator;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildDependencyDeferrer;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildObjectCollector;
import com.houshce29.ducky.internal.tasking.intrinsic.PriorityDeferredDependencyBuilder;
import com.houshce29.ducky.internal.tasking.intrinsic.StandardDependencyBuilder;
import org.junit.Assert;
import org.junit.Test;

public class TU_DivineEnvironment {
    private static final DivineEnvironment INSTANCE = DivineEnvironment.getInstance();

    @Test
    public void testContainsCoreServices() {
        Assert.assertNotNull(INSTANCE.getBuilder());
        Assert.assertNotNull(INSTANCE.getTaskQueue());
    }

    @Test
    public void testContainsIntrinsicTasks() {
        Assert.assertNotNull(INSTANCE.get(PreBuildObjectCollector.class));
        Assert.assertNotNull(INSTANCE.get(PreBuildDependencyCreator.class));
        Assert.assertNotNull(INSTANCE.get(PreBuildDependencyDeferrer.class));
        Assert.assertNotNull(INSTANCE.get(StandardDependencyBuilder.class));
        Assert.assertNotNull(INSTANCE.get(PriorityDeferredDependencyBuilder.class));
        Assert.assertNotNull(INSTANCE.get(DeferredDependencyBuilder.class));
    }
}
