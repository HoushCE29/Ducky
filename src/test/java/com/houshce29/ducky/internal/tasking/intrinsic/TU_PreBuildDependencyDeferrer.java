package com.houshce29.ducky.internal.tasking.intrinsic;

import com.google.common.collect.Sets;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.internal.tasking.intrinsic.testing.MultipleInstanceInjectionScenario;
import com.houshce29.ducky.internal.tasking.intrinsic.testing.TestScenario;
import com.houshce29.ducky.internal.tasking.intrinsic.testing.TestScenarioFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class TU_PreBuildDependencyDeferrer {
    private PreBuildDependencyDeferrer deferrer;
    private ProcessingPlatform platform;

    @Before
    public void beforeEach() {
        deferrer = new PreBuildDependencyDeferrer();
    }

    @Test
    public void testRunDeferRequired() {
        TestScenario scenario = TestScenarioFactory.getMultipleInstanceInjectionScenario();
        platform = ProcessingPlatform.create(new HashSet<>(), scenario.getBuildSet());
        platform.mutableDependencySet().addAll(scenario.getDependencies());

        deferrer.run(platform, null);

        // Check deferred set
        Assert.assertEquals(1, platform.mutableDeferredDependencySet().size());
        Assert.assertEquals(MultipleInstanceInjectionScenario.TargetConsumer.class,
                platform.mutableDeferredDependencySet().stream()
                        .findFirst()
                        .get()
                        .getType());

        // Check regular set
        Assert.assertEquals(Sets.newHashSet(MultipleInstanceInjectionScenario.Extra.class,
                                            MultipleInstanceInjectionScenario.GenericOne.class,
                                            MultipleInstanceInjectionScenario.GenericTwo.class),
                platform.mutableDependencySet().stream()
                        .map(Dependency::getType)
                        .collect(Collectors.toSet()));

        // Check priority set
        Assert.assertEquals(Sets.newHashSet(MultipleInstanceInjectionScenario.Generic.class),
                            platform.mutablePriorityBuildSet());
    }

    @Test
    public void testRunDeferNotRequired() {
        TestScenario scenario = TestScenarioFactory.getCircularDependencyScenario();
        platform = ProcessingPlatform.create(new HashSet<>(), scenario.getBuildSet());
        platform.mutableDependencySet().addAll(scenario.getDependencies());

        deferrer.run(platform, null);

        // Check deferred set
        Assert.assertTrue(platform.mutableDeferredDependencySet().isEmpty());

        // Check regular set
        Assert.assertEquals(scenario.getDependencies(), platform.mutableDependencySet());

        // Check priority set
        Assert.assertTrue(platform.mutablePriorityBuildSet().isEmpty());
    }
}
