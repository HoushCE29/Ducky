package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import org.junit.Assert;

import java.util.function.Consumer;

public class MissingDependencyScenario extends TestScenario {
    MissingDependencyScenario() {
        super("Missing Dependency Scenario",
                DependencyBuildException.class, DependentOnMissing.class);
    }

    @Override
    public void validateBuild(Environment result) {
        Assert.fail(String.format("[%s] should have thrown [%s].", getName(), getExpectedException()));
    }

    @Override
    public void initForBuild(ProcessingPlatform platform, ModifiableEnvironment environment, boolean deferred) {
        Consumer<Dependency<?>> consumer = deferred ?
                dep -> platform.mutableDeferredDependencySet().add(dep) :
                dep -> platform.mutableDependencySet().add(dep);

        platform.mutablePriorityBuildSet().add(DependentOnMissing.class);

        consumer.accept(getDependencyMap().get(DependentOnMissing.class));
    }

    public static class Missing {
    }

    public static class DependentOnMissing {
        public DependentOnMissing(Missing missing) {
        }
    }
}
