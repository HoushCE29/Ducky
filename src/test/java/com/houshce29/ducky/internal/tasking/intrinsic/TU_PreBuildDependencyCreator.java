package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.exceptions.PublicConstructorsException;
import com.houshce29.ducky.internal.processing.Dependency;
import com.houshce29.ducky.internal.processing.ProcessingPlatform;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TU_PreBuildDependencyCreator extends BaseUnitTest {
    private PreBuildDependencyCreator creator;

    @Before
    public void beforeEach() {
        creator = new PreBuildDependencyCreator();
    }

    @Test
    public void testRunForDefaultConstructor() {
        test(SimpleObject.class, Collections.emptyList());
    }

    @Test
    public void testRunForDeclaredDefaultConstructor() {
        test(OtherSimpleObject.class, Collections.emptyList());
    }

    @Test
    public void testRunForSingularParamConstructor() {
        test(ComplexObject.class, Collections.singletonList(SimpleObject.class));
    }

    @Test
    public void testRunForComplexParamsConstructor() {
        test(VeryComplexObject.class,
                Arrays.asList(ComplexObject.class, OtherSimpleObject.class));
    }

    @Test(expected = PublicConstructorsException.class)
    public void testRunForProtectedConstructor() {
        test(ProtectedConst.class, null);
    }

    @Test(expected = PublicConstructorsException.class)
    public void testRunForPrivateConstructor() {
        test(PrivateConst.class, null);
    }

    @Test(expected = PublicConstructorsException.class)
    public void testRunTooManyPublicConstructors() {
        test(MultiplePublicConst.class, null);
    }

    private void test(Class<?> testClass, List<Class<?>> expectedReqs) {
        ProcessingPlatform platform =
                ProcessingPlatform.create(new HashSet<>(), Collections.singleton(testClass));

        creator.run(platform, null);

        Set<Dependency<?>> deps = platform.mutableDependencySet();
        Assert.assertEquals(1, deps.size());

        Dependency<?> dep = deps.stream().findFirst().get();
        Assert.assertEquals(testClass, dep.getType());
        Assert.assertEquals(expectedReqs, dep.getRequirements());
    }

    // ==== GOOD:

    public static class SimpleObject {
    }

    public static class OtherSimpleObject {
        public OtherSimpleObject() {
        }
    }

    public static class ComplexObject {
        public ComplexObject(SimpleObject obj) {
        }

        private ComplexObject(String irrelevant) {
        }
    }

    public static class VeryComplexObject {
        public VeryComplexObject(ComplexObject obj, OtherSimpleObject other) {
        }

        protected VeryComplexObject(String irrelevant) {
        }

        private VeryComplexObject() {
        }
    }

    // ==== BAD:

    public static class ProtectedConst {
        protected ProtectedConst() {
        }
    }

    public static class PrivateConst {
        private PrivateConst(String derp) {
        }
    }

    public static class MultiplePublicConst {
        public MultiplePublicConst() {
        }

        public MultiplePublicConst(String derp) {
        }

        public MultiplePublicConst(String derp, Object obj) {
        }
    }
}
