package com.github.dqqzj.refresh.scope;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RefreshScope extends GenericScope implements ApplicationContextAware {
    private ApplicationContext context;
    private BeanDefinitionRegistry registry;

    public RefreshScope() {
        super.setName("refresh");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        super.postProcessBeanDefinitionRegistry(registry);
    }

    public boolean refresh(String name) {
        if (!ScopedProxyUtils.isScopedTarget(name)){
            name = ScopedProxyUtils.getOriginalBeanName(name);
        }
        if (super.destroy(name)) {
            this.context.publishEvent(new RefreshedEvent(name));
            return true;
        } else {
            return false;
        }
    }

    public void refreshAll() {
        super.destroy();
        this.context.publishEvent(new RefreshedEvent());
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
