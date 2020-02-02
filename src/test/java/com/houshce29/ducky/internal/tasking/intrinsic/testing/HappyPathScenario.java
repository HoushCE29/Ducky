package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import org.junit.Assert;

import java.util.function.Consumer;

public class HappyPathScenario extends TestScenario {
    HappyPathScenario() {
        super("Happy Path Scenario", null,
                HappyPathOne.class, HappyPathTwo.class, HappyPathThree.class);
    }

    @Override
    public void validateBuild(Environment result) {
        HappyPathOne one = result.get(HappyPathOne.class);
        HappyPathTwo two = result.get(HappyPathTwo.class);
        HappyPathThree three = result.get(HappyPathThree.class);
        Assert.assertNotNull(one);
        Assert.assertNotNull(two);
        Assert.assertNotNull(three);
        Assert.assertSame(one, two.getOne());
        Assert.assertSame(one, three.getOne());
        Assert.assertSame(two, three.getTwo());
    }

    @Override
    public void initForBuild(ProcessingPlatform platform, ModifiableEnvironment environment, boolean deferred) {
        Consumer<Dependency<?>> consumer = deferred ?
                dep -> platform.mutableDeferredDependencySet().add(dep) :
                dep -> platform.mutableDependencySet().add(dep);

        platform.mutablePriorityBuildSet().addAll(getBuildSet());

        consumer.accept(getDependencyMap().get(HappyPathOne.class));
        consumer.accept(getDependencyMap().get(HappyPathTwo.class));
        consumer.accept(getDependencyMap().get(HappyPathThree.class));
    }

    public static class HappyPathOne {
    }

    public static class HappyPathTwo {
        private HappyPathOne one;
        public HappyPathTwo(HappyPathOne one) {
            this.one = one;
        }

        public HappyPathOne getOne() {
            return one;
        }
    }

    public static class HappyPathThree {
        private HappyPathOne one;
        private HappyPathTwo two;
        public HappyPathThree(HappyPathOne one, HappyPathTwo two) {
            this.one = one;
            this.two = two;
        }

        public HappyPathOne getOne() {
            return one;
        }

        public HappyPathTwo getTwo() {
            return two;
        }
    }
}
