package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import java.util.Arrays;
import java.util.List;

public class TestScenarioFactory {

    private TestScenarioFactory() {
    }

    public static TestScenario getHappyPathScenario() {
        return new HappyPathScenario();
    }

    public static TestScenario getMultipleInstanceInjectionScenario() {
        return new MultipleInstanceInjectionScenario();
    }

    public static TestScenario getInjectionTooGeneralScenario() {
        return new InjectionTooGeneralScenario();
    }

    public static TestScenario getCircularDependencyScenario() {
        return new CircularDependencyScenario();
    }

    public static TestScenario getMissingDependencyScenario() {
        return new MissingDependencyScenario();
    }

    public static List<TestScenario> getAllScenarios() {
        return Arrays.asList(getHappyPathScenario(),
                             getMultipleInstanceInjectionScenario(),
                             // getInjectionTooGeneralScenario(),
                             getCircularDependencyScenario(),
                             getMissingDependencyScenario());
    }
}
