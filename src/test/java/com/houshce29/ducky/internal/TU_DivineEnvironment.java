package com.houshce29.ducky.internal;

import com.houshce29.ducky.internal.tasking.intrinsic.DeferredDependencyBuilder;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildDependencyCreator;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildDependencyDeferrer;
import com.houshce29.ducky.internal.tasking.intrinsic.PreBuildObjectCollector;
import com.houshce29.ducky.internal.tasking.intrinsic.PriorityDeferredDependencyBuilder;
import com.houshce29.ducky.internal.tasking.intrinsic.StandardDependencyBuilder;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Test;

public class TU_DivineEnvironment extends BaseUnitTest {
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

        Assert.assertEquals(PreBuildObjectCollector.class, INSTANCE.getIntrinsicTasks().get(0).getClass());
        Assert.assertEquals(PreBuildDependencyCreator.class, INSTANCE.getIntrinsicTasks().get(1).getClass());
        Assert.assertEquals(PreBuildDependencyDeferrer.class, INSTANCE.getIntrinsicTasks().get(2).getClass());
        Assert.assertEquals(StandardDependencyBuilder.class, INSTANCE.getIntrinsicTasks().get(3).getClass());
        Assert.assertEquals(PriorityDeferredDependencyBuilder.class, INSTANCE.getIntrinsicTasks().get(4).getClass());
        Assert.assertEquals(DeferredDependencyBuilder.class, INSTANCE.getIntrinsicTasks().get(5).getClass());
    }
}
