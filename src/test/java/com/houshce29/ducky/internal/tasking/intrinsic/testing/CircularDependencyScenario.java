package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import org.junit.Assert;

import java.util.function.Consumer;

public class CircularDependencyScenario extends TestScenario {
    CircularDependencyScenario() {
        super("Circular Dependency Scenario", DependencyBuildException.class,
                OneToTwo.class, TwoToOne.class);
    }

    @Override
    public void validateBuild(Environment result) {
        Assert.fail(String.format("[%s] should have thrown [%s]", getName(), getExpectedException()));
    }

    @Override
    public void initForBuild(ProcessingPlatform platform, ModifiableEnvironment environment, boolean deferred) {
        Consumer<Dependency<?>> consumer = deferred ?
                dep -> platform.mutableDeferredDependencySet().add(dep) :
                dep -> platform.mutableDependencySet().add(dep);

        platform.mutablePriorityBuildSet().addAll(getBuildSet());

        consumer.accept(getDependencyMap().get(OneToTwo.class));
        consumer.accept(getDependencyMap().get(TwoToOne.class));
    }

    public static class OneToTwo {
        public OneToTwo(TwoToOne param) {
        }
    }

    public static class TwoToOne {
        public TwoToOne(OneToTwo param) {
        }
    }
}
