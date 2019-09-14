package com.github.dqqzj.springboot.target;

import org.springframework.aop.TargetSource;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinzhongjian
 * @date created in 2019-08-25 12:43
 * @description: TODO
 * @since JDK 1.8.0_212-b10z
 */
public class DqqzjTargetSource implements TargetSource {
    private final AtomicInteger idx = new AtomicInteger();
    private final Object[] target;;
    public DqqzjTargetSource(Object[]  target) {
        Assert.notNull(target, "Target object must not be null");
        this.target = target;
    }
    @Override
    public Class<?> getTargetClass() {
        return target.getClass();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public Object getTarget() throws Exception {
        return this.target[this.idx.getAndIncrement() & this.target.length - 1];
    }

    @Override
    public void releaseTarget(Object target) throws Exception {

    }
}
