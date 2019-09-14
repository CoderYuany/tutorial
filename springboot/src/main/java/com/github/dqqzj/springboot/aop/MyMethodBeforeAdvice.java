package com.github.dqqzj.springboot.aop;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author qinzhongjian
 * @date created in 2019-08-24 11:05
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Component
public class MyMethodBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (!method.getName().equals("toString")) {
            System.out.println(target.getClass().getName() + "#" + method.getName());
        }
    }
    /**
     * 代理的目标对象  效果同setTargetSource(@Nullable TargetSource targetSource)
     * TargetSource targetSource = new SingletonTargetSource(aopService);
     * 可以从容器获取，也可以类似下面这样直接new，使用区别需要熟悉spring机制。
     * factoryBean.setTarget(new AopService());
     *
     * 设置需要被代理的接口  效果同factoryBean.setProxyInterfaces(new Class[]{AopService.class});
     * 若没有实现接口，那就会采用cglib去代理
     * 如果有接口不指定的话会代理所有的接口，否则代理指定的接口
     *
     *  setInterceptorNames方法源代码中有这样的一句话：Set the list of Advice/Advisor bean names. This must always be set
     *  to use this factory bean in a bean factory.
     */
    @Bean
    public ProxyFactoryBean proxyFactoryBean(AopService aopService) {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(aopService);
        //factoryBean.setInterfaces(AopService.class);

        factoryBean.setInterceptorNames("myMethodBeforeAdvice");
        //是否强制使用cglib，默认是false的
        //factoryBean.setProxyTargetClass(true);
        return factoryBean;
    }

}
