package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.framework.core.Environment;
import org.junit.Assert;

public class MissingDependencyScenario extends TestScenario {
    MissingDependencyScenario() {
        super("Missing Dependency Scenario",
                DependencyBuildException.class, DependentOnMissing.class);
    }

    @Override
    public void validateBuild(Environment result) {
        Assert.fail(String.format("[%s] should have thrown [%s].", getName(), getExpectedException()));
    }

    public static class Missing {
    }

    public static class DependentOnMissing {
        public DependentOnMissing(Missing missing) {
        }
    }
}
