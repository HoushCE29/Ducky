package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.framework.core.Environment;
import com.houshce29.ducky.framework.core.ModifiableEnvironment;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import org.junit.Assert;

import java.util.function.Consumer;

public class InjectionTooGeneralScenario extends TestScenario {
    InjectionTooGeneralScenario() {
        super("Injection Too General Scenario", DependencyBuildException.class,
                GeneralOne.class, GeneralTwo.class, Target.class);
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

        platform.mutablePriorityBuildSet().add(Target.class);
        consumer.accept(getDependencyMap().get(Target.class));

        environment.add(new GeneralOne());
        environment.add(new GeneralTwo());
    }

    public interface General {
    }

    public static class GeneralOne implements General {
    }

    public static class GeneralTwo implements General {
    }

    public static class Target {
        public Target(General tooGeneral) {
        }
    }
}
