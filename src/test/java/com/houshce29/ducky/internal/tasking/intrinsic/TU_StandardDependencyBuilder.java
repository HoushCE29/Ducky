package com.houshce29.ducky.internal.tasking.intrinsic;

import com.houshce29.ducky.internal.tasking.intrinsic.testing.BaseIntrinsicBuildTaskTestUnit;

public class TU_StandardDependencyBuilder extends BaseIntrinsicBuildTaskTestUnit {

    @Override
    public TestConfig config() {
        return define(new StandardDependencyBuilder())
                .checkPriorityDeferredSet()
                .checkStandardDependencySet()
                .build();
    }
}
