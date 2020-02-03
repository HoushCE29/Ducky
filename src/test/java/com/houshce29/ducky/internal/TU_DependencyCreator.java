package com.houshce29.ducky.internal;

import com.houshce29.ducky.exceptions.PublicConstructorsException;
import com.houshce29.ducky.testing.BaseUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TU_DependencyCreator extends BaseUnitTest {

    private DependencyCreator creator;

    @Before
    public void beforeEach() {
        creator = new DependencyCreator();
    }

    @Test
    public void testCreateWithDefaultConstructor() {
        test(SimpleObject.class, Collections.emptyList());
    }

    @Test
    public void testCreateWithDeclaredDefaultConstructor() {
        test(OtherSimpleObject.class, Collections.emptyList());
    }

    @Test
    public void testCreateWithSingleParamConstructor() {
        test(ComplexObject.class, Collections.singletonList(SimpleObject.class));
    }

    @Test
    public void testCreateWithMultipleParamConstructor() {
        test(VeryComplexObject.class,
                Arrays.asList(ComplexObject.class, OtherSimpleObject.class));
    }

    @Test(expected = PublicConstructorsException.class)
    public void testCreateWithPrivateConstructor() {
        test(PrivateConst.class, null);
    }

    @Test(expected = PublicConstructorsException.class)
    public void testCreateWithProtectedConstructor() {
        test(ProtectedConst.class, null);
    }

    @Test(expected = PublicConstructorsException.class)
    public void testCreateMultiplePublicConstructors() {
        test(MultiplePublicConst.class, null);
    }


    private void test(Class<?> target, List<Class<?>> expectedReq) {
        Set<Dependency<?>> deps = creator.create(Collections.singleton(target));
        Dependency<?> dep = deps.stream()
                .findFirst()
                .get();

        Assert.assertEquals(target, dep.getType());
        Assert.assertEquals(expectedReq, dep.getRequirements());
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
