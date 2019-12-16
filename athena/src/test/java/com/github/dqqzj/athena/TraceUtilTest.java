package com.github.dqqzj.athena;

import org.junit.Assert;
import org.junit.Test;

public class TraceUtilTest {
    @Test
    public void getTraceId() {
        String traceId = TraceUtil.getTraceId();
        Assert.assertNotNull(traceId);
        System.out.println(traceId);
        long l = System.nanoTime();
        System.out.println(Long.toHexString(l));
        long t = System.currentTimeMillis();
        System.out.println(Long.toHexString(t));
        System.out.println(Long.toHexString(l + t));
    }
}
