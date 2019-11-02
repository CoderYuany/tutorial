package com.github.dqqzj.refresh.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class RefreshScope extends GenericScope implements ApplicationContextAware, Ordered {
    private ApplicationContext context;
    private BeanDefinitionRegistry registry;
    private boolean eager = true;
    private int order = 2147483547;

    public RefreshScope() {
        super.setName("refresh");
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setEager(boolean eager) {
        this.eager = eager;
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        super.postProcessBeanDefinitionRegistry(registry);
    }

    @ManagedOperation(
        description = "Dispose of the current instance of bean name provided and force a refresh on next method execution."
    )
    public boolean refresh(String name) {
        if (!name.startsWith("scopedTarget.")) {
            name = "scopedTarget." + name;
        }

        if (super.destroy(name)) {
            this.context.publishEvent(new RefreshedEvent(name));
            return true;
        } else {
            return false;
        }
    }

    @ManagedOperation(
        description = "Dispose of the current instance of all beans in this scope and force a refresh on next method execution."
    )
    public void refreshAll() {
        super.destroy();
        this.context.publishEvent(new RefreshedEvent());
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
