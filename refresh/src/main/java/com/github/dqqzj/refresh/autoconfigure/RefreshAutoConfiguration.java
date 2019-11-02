//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.autoconfigure;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;

import com.github.dqqzj.refresh.ContextRefresher;
import com.github.dqqzj.refresh.event.RefreshEventListener;
import com.github.dqqzj.refresh.scope.RefreshScope;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.stereotype.Component;

@Configuration
@ConditionalOnClass({RefreshScope.class})
public class RefreshAutoConfiguration {
    public RefreshAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({RefreshScope.class})
    public static RefreshScope refreshScope() {
        return new RefreshScope();
    }


    @Bean
    @ConditionalOnMissingBean
    public ContextRefresher contextRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        return new ContextRefresher(context, scope);
    }

    @Bean
    public RefreshEventListener refreshEventListener(ContextRefresher contextRefresher) {
        return new RefreshEventListener(contextRefresher);
    }

    @Component
    protected static class RefreshScopeBeanDefinitionEnhancer implements BeanDefinitionRegistryPostProcessor {
        private Set<String> refreshables = new HashSet(Arrays.asList("com.zaxxer.hikari.HikariDataSource"));
        private Environment environment;

        protected RefreshScopeBeanDefinitionEnhancer() {
        }

        public Set<String> getRefreshable() {
            return this.refreshables;
        }

        public void setRefreshable(Set<String> refreshables) {
            if (this.refreshables != refreshables) {
                this.refreshables.clear();
                this.refreshables.addAll(refreshables);
            }

        }

        public void setExtraRefreshable(Set<String> refreshables) {
            this.refreshables.addAll(refreshables);
        }

        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        }

        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            String[] var2 = registry.getBeanDefinitionNames();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String name = var2[var4];
                BeanDefinition definition = registry.getBeanDefinition(name);
                if (this.isApplicable(registry, name, definition)) {
                    BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, name);
                    BeanDefinitionHolder proxy = ScopedProxyUtils.createScopedProxy(holder, registry, true);
                    definition.setScope("refresh");
                    registry.registerBeanDefinition(proxy.getBeanName(), proxy.getBeanDefinition());
                }
            }

        }

        private boolean isApplicable(BeanDefinitionRegistry registry, String name, BeanDefinition definition) {
            String scope = definition.getScope();
            if ("refresh".equals(scope)) {
                return false;
            } else {
                String type = definition.getBeanClassName();
                if (registry instanceof BeanFactory) {
                    Class<?> cls = ((BeanFactory)registry).getType(name);
                    if (cls != null) {
                        type = cls.getName();
                    }
                }

                if (type != null) {
                    if (this.environment == null && registry instanceof BeanFactory) {
                        this.environment = (Environment)((BeanFactory)registry).getBean(Environment.class);
                    }

                    if (this.environment == null) {
                        this.environment = new StandardEnvironment();
                    }

                    Binder.get(this.environment).bind("spring.cloud.refresh", Bindable.ofInstance(this));
                    return this.refreshables.contains(type);
                } else {
                    return false;
                }
            }
        }
    }

    @Configuration
    @ConditionalOnClass(
        name = {"javax.persistence.EntityManagerFactory"}
    )
    protected static class JpaInvokerConfiguration implements LoadTimeWeaverAware {
        @Autowired
        private ListableBeanFactory beanFactory;

        protected JpaInvokerConfiguration() {
        }

        @PostConstruct
        public void init() {
            String cls = "org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker";
            if (this.beanFactory.containsBean(cls)) {
                this.beanFactory.getBean(cls);
            }

        }

        public void setLoadTimeWeaver(LoadTimeWeaver ltw) {
        }
    }
}
