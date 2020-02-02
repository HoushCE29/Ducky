package com.houshce29.ducky.internal.tasking.intrinsic;

import com.google.common.collect.Sets;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class TU_PreBuildObjectCollector extends BaseUnitTest {
    private PreBuildObjectCollector collector = new PreBuildObjectCollector();

    @Test
    public void test() {
        Set<Object> objects = Sets.newHashSet("String", 10, 'a');
        ProcessingPlatform platform = ProcessingPlatform.create(objects, null);
        ModifiableEnvironment environment = ModifiableEnvironment.empty();

        collector.run(platform, environment);

        Assert.assertEquals(objects, environment.getAll(Object.class));
    }
}
