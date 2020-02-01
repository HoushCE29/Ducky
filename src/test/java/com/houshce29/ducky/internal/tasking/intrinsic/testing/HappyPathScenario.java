package com.houshce29.ducky.internal.tasking.intrinsic.testing;

import com.houshce29.ducky.framework.core.Environment;
import org.junit.Assert;

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
