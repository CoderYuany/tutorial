package com.github.dqqzj.spike.aop;

import java.lang.annotation.*;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface LimitResource {
	/**
	 * 描述
	 */
	String description()  default "";

	/**
	 * key
	 */
	String value() default "";

	/**
	 * 类型
	 */
	LimitType limitType() default LimitType.CUSTOMER;

	enum LimitType {
		/**
		 * 自定义key
		 */
		CUSTOMER,
		/**
		 * 根据请求者IP
		 */
		IP
	}
}
