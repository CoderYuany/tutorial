package com.github.dqqzj.spike.aop;

import com.github.dqqzj.spike.exception.RrException;
import com.github.dqqzj.spike.utils.IpUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Aspect
@Configuration
public class LimitResourceAspect {

	/**
	 * 根据IP分不同的令牌桶, 每天自动清理缓存
	 */
	private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
			.maximumSize(1000)
			.expireAfterWrite(1, TimeUnit.DAYS)
			.build(new CacheLoader<String, RateLimiter>() {
				@Override
				public RateLimiter load(String key) {
					/**
					 * 新的IP初始化 每秒只发出5个令牌
					 */
					return RateLimiter.create(5);
				}
			});
	
	@Pointcut("@annotation(com.github.dqqzj.spike.aop.LimitResource)")
	public void limitResourceAspect() {
		
	}
	
    @Around("limitResourceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		LimitResource limitResource = method.getAnnotation(LimitResource.class);
		LimitResource.LimitType limitType = limitResource.limitType();
		String value = limitResource.value();
		Object obj;
		try {
			if(limitType.equals(LimitResource.LimitType.IP)){
				value = IpUtils.getIpAddr();
			}
			RateLimiter rateLimiter = caches.get(value);
			Boolean flag = rateLimiter.tryAcquire();
			if(flag){
				obj = joinPoint.proceed();
			}else{
				throw new RrException("亲，你访问的太频繁了");
			}
		} catch (Throwable e) {
			throw new RrException("亲，你访问的太频繁了");
		}
		return obj;
    } 
}
