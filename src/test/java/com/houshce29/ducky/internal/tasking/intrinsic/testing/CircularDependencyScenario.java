package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.exceptions.DependencyBuildException;
import com.houshce29.ducky.framework.core.Environment;
import org.junit.Assert;

public class CircularDependencyScenario extends TestScenario {
    CircularDependencyScenario() {
        super("Circular Dependency Scenario", DependencyBuildException.class,
                OneToTwo.class, TwoToOne.class);
    }

    @Override
    public void validateBuild(Environment result) {
        Assert.fail(String.format("[%s] should have thrown [%s]", getName(), getExpectedException()));
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
