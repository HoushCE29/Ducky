package com.houshce29.ducky.internal.processing;

import com.google.common.collect.Sets;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class TU_ProcessingPlatform extends BaseUnitTest {

    @Test
    public void testCreate() {
        Set<Object> objects = Sets.newHashSet("foo", 19, true);
        Set<Class<?>> types = Sets.newHashSet(String.class, Void.class, Math.class);

        ProcessingPlatform platform = ProcessingPlatform.create(objects, types);

        Assert.assertEquals(objects, platform.objects());
        Assert.assertEquals(types, platform.buildSet());
    }

    @Test
    public void testPlatformIsMutable() {
        Object object = "STRING";
        Dependency<Object> dep = new Dependency<>(Object.class,
                (Constructor<Object>) Object.class.getConstructors()[0]);

        ProcessingPlatform platform = ProcessingPlatform.create(new HashSet<>(), new HashSet<>());
        platform.objects().add(object);
        platform.buildSet().add(ThreadDeath.class);
        platform.variables().put("object", object);
        platform.mutableDependencySet().add(dep);
        platform.mutablePriorityBuildSet().add(ThaiBuddhistChronology.class);
        platform.mutableDeferredDependencySet().add(dep);

        Assert.assertEquals(Sets.newHashSet(object), platform.objects());
        Assert.assertEquals(Sets.newHashSet(ThreadDeath.class), platform.buildSet());
        Assert.assertEquals(object, platform.variables().get("object"));
        Set<Dependency<?>> depSet = Sets.newHashSet(dep);
        Assert.assertEquals(depSet, platform.mutableDependencySet());
        Assert.assertEquals(Sets.newHashSet(ThaiBuddhistChronology.class), platform.mutablePriorityBuildSet());
        Assert.assertEquals(depSet, platform.mutableDeferredDependencySet());
    }
}
