package com.houshce29.ducky.internal;

import com.houshce29.ducky.internal.tasking.TaskQueue;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;

public class TU_Builder {

    @Test
    public void testBuildCallsTaskQueue() {
        TaskQueue queue = Mockito.mock(TaskQueue.class);
        Builder builder = new Builder(queue);
        builder.build(new HashSet<>(), new HashSet<>());

        Mockito.verify(queue).run(Mockito.any(), Mockito.any());
    }
}
