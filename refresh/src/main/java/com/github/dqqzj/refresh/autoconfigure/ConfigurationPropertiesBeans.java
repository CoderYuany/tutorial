//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.autoconfigure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.dqqzj.refresh.scope.RefreshScope;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationPropertiesBeans implements BeanPostProcessor, ApplicationContextAware {
    private ConfigurationBeanFactoryMetadata metaData;
    private Map<String, Object> beans = new HashMap();
    private ConfigurableListableBeanFactory beanFactory;
    private String refreshScope;
    private boolean refreshScopeInitialized;
    private ConfigurationPropertiesBeans parent;

    public ConfigurationPropertiesBeans() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext.getAutowireCapableBeanFactory() instanceof ConfigurableListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        }

        if (applicationContext.getParent() != null && applicationContext.getParent().getAutowireCapableBeanFactory() instanceof ConfigurableListableBeanFactory) {
            ConfigurableListableBeanFactory listable = (ConfigurableListableBeanFactory)applicationContext.getParent().getAutowireCapableBeanFactory();
            String[] names = listable.getBeanNamesForType(ConfigurationPropertiesBeans.class);
            if (names.length == 1) {
                this.parent = (ConfigurationPropertiesBeans)listable.getBean(names[0]);
                this.beans.putAll(this.parent.beans);
            }
        }

    }

    public void setBeanMetaDataStore(ConfigurationBeanFactoryMetadata beans) {
        this.metaData = beans;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (this.isRefreshScoped(beanName)) {
            return bean;
        } else {
            ConfigurationProperties annotation = (ConfigurationProperties)AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);
            if (annotation != null) {
                this.beans.put(beanName, bean);
            } else if (this.metaData != null) {
                annotation = (ConfigurationProperties)this.metaData.findFactoryAnnotation(beanName, ConfigurationProperties.class);
                if (annotation != null) {
                    this.beans.put(beanName, bean);
                }
            }

            return bean;
        }
    }

    private boolean isRefreshScoped(String beanName) {
        if (this.refreshScope == null && !this.refreshScopeInitialized) {
            this.refreshScopeInitialized = true;
            String[] var2 = this.beanFactory.getRegisteredScopeNames();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String scope = var2[var4];
                if (this.beanFactory.getRegisteredScope(scope) instanceof RefreshScope) {
                    this.refreshScope = scope;
                    break;
                }
            }
        }

        if (beanName != null && this.refreshScope != null) {
            return this.beanFactory.containsBeanDefinition(beanName) && this.refreshScope.equals(this.beanFactory.getBeanDefinition(beanName).getScope());
        } else {
            return false;
        }
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Set<String> getBeanNames() {
        return new HashSet(this.beans.keySet());
    }
}
