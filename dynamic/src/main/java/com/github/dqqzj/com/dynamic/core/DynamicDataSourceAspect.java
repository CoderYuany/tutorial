package com.github.dqqzj.com.dynamic.core;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 20:58
 * @description: TODD
 * @since JDK 1.8
 */
@Slf4j
@Aspect
@Order(1)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DynamicDataSourceAspect {

    int db = 0;
    @Autowired
    DynamicDataSourceProperties dynamicDatasourceProperties;

    /**
     * @author qinzhongjian
     * @date 2019-07-04
     * @param: joinPoint
     * @return void
     * @description: 此处是否考虑添加用户的数据源
     * 类似与securityContext之类缓存已登陆的用户信息，从中获取db
     */

    @Before(value = "@within(com.example.demo.annotation.DS)")
    public void before(JoinPoint joinPoint) {
        log.debug("DynamicDataSourceAspect#before joinPoint:[{}] ====>set datasource", joinPoint.getTarget());
        if (db == 0) {
            db++;
            DynamicDataSourceContextHolder.set("default");
        } else {
            DynamicDataSourceContextHolder.set("slave");
        }
    }

    @After("@within(com.example.demo.annotation.DS)")
    public void after(JoinPoint joinPoint) {
        log.debug("DynamicDataSourceAspect#after joinPoint:[{}] ====>clear datasource", joinPoint);
        DynamicDataSourceContextHolder.clear();
    }

}
