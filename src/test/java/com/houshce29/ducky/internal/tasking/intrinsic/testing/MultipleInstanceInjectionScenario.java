package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.framework.core.Environment;
import org.junit.Assert;

import java.util.Set;

public class MultipleInstanceInjectionScenario extends TestScenario {

    MultipleInstanceInjectionScenario() {
        super("Multiple Instance Injection Scenario", null,
                GenericOne.class, GenericTwo.class, Extra.class, TargetConsumer.class);
    }

    @Override
    public void validateBuild(Environment result) {
        Set<Generic> generics = result.getAll(Generic.class);
        Extra extra = result.get(Extra.class);
        TargetConsumer consumer = result.get(TargetConsumer.class);

        Assert.assertFalse(generics.isEmpty());
        Assert.assertNotNull(extra);
        Assert.assertNotNull(consumer);

        Assert.assertEquals(generics, consumer.getGenerics());
        Assert.assertSame(extra, consumer.getExtra());
    }

    public interface Generic {
    }

    public static class GenericOne implements Generic {
    }

    public static class GenericTwo implements Generic {
    }

    public static class Extra {
    }

    public static class TargetConsumer {
        private final Extra extra;
        private final Set<Generic> generics;
        public TargetConsumer(Extra extra, Set<Generic> generics) {
            this.extra = extra;
            this.generics = generics;
        }

        public Extra getExtra() {
            return extra;
        }

        public Set<Generic> getGenerics() {
            return generics;
        }
    }
}
