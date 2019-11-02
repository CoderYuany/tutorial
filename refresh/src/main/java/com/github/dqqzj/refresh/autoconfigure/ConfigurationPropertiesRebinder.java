package com.github.dqqzj.refresh.autoconfigure;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.dqqzj.refresh.environment.EnvironmentChangeEvent;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationPropertiesRebinder implements ApplicationContextAware, ApplicationListener<EnvironmentChangeEvent> {
    private ConfigurationPropertiesBeans beans;
    private ApplicationContext applicationContext;
    private Map<String, Exception> errors = new ConcurrentHashMap();

    public ConfigurationPropertiesRebinder(ConfigurationPropertiesBeans beans) {
        this.beans = beans;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, Exception> getErrors() {
        return this.errors;
    }

    public void rebind() {
        this.errors.clear();
        Iterator var1 = this.beans.getBeanNames().iterator();

        while(var1.hasNext()) {
            String name = (String)var1.next();
            this.rebind(name);
        }

    }

    public boolean rebind(String name) {
        if (!this.beans.getBeanNames().contains(name)) {
            return false;
        } else {
            if (this.applicationContext != null) {
                try {
                    Object bean = this.applicationContext.getBean(name);
                    if (AopUtils.isAopProxy(bean)) {
                        bean = getTargetObject(bean);
                    }

                    if (bean != null) {
                        this.applicationContext.getAutowireCapableBeanFactory().destroyBean(bean);
                        this.applicationContext.getAutowireCapableBeanFactory().initializeBean(bean, name);
                        return true;
                    }
                } catch (RuntimeException var3) {
                    this.errors.put(name, var3);
                    throw var3;
                } catch (Exception var4) {
                    this.errors.put(name, var4);
                    throw new IllegalStateException("Cannot rebind to " + name, var4);
                }
            }

            return false;
        }
    }

    private static Object getTargetObject(Object candidate) {
        try {
            return AopUtils.isAopProxy(candidate) && candidate instanceof Advised ? ((Advised)candidate).getTargetSource().getTarget() : candidate;
        } catch (Exception var2) {
            throw new IllegalStateException("Failed to unwrap proxied object", var2);
        }
    }

    public Set<String> getBeanNames() {
        return new HashSet(this.beans.getBeanNames());
    }

    public void onApplicationEvent(EnvironmentChangeEvent event) {
        if (this.applicationContext.equals(event.getSource()) || event.getKeys().equals(event.getSource())) {
            this.rebind();
        }

    }
}
