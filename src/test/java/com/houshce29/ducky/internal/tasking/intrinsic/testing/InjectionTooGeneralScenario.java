package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.framework.core.Environment;
import org.junit.Assert;

public class InjectionTooGeneralScenario extends TestScenario {
    InjectionTooGeneralScenario() {
        super("Injection Too General Scenario", DependencyBuildException.class,
                GeneralOne.class, GeneralTwo.class, Target.class);
    }

    @Override
    public void validateBuild(Environment result) {
        Assert.fail(String.format("[%s] should have thrown [%s].", getName(), getExpectedException()));
    }

    public interface General {
    }

    public static class GeneralOne implements General {
    }

    public static class GeneralTwo implements General {
    }

    public static class Target {
        General tooGeneral;
        public Target(General tooGeneral) {
            this.tooGeneral = tooGeneral;
        }
    }
}
