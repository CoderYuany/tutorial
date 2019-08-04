package com.github.dqqzj.spike.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: 同步锁 AOP
 * @transaction 中  order 大小的说明:
 * @see {https://docs.spring.io/spring/docs/4.3.14.RELEASE/spring-framework-reference/htmlsingle/#transaction-declarative-annotations}
 * @see {https://docs.spring.io/spring/docs/4.3.14.RELEASE/javadoc-api/}
 * order越小越是最先执行，但更重要的是最先执行的最后结束。order默认值是2147483647
 * @since JDK 1.8.0_212-b10
 */
@Component
@Scope
@Aspect
@Order(1)
public class SpikeLockAspect {

	private static  Lock lock = new ReentrantLock(true);

	/**
	 * 用于记录错误日志
	 */
	@Pointcut("@annotation(com.github.dqqzj.spike.aop.SpikeLock)")
	public void lockAspect() {
		
	}
	
    @Around("lockAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
    	lock.lock();
    	Object obj;
		try {
			obj = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();       
		} finally{
			lock.unlock();
		}
    	return obj;
    } 
}
