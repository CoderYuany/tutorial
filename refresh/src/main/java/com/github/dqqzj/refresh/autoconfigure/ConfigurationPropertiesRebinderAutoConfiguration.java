//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.autoconfigure;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean({ConfigurationPropertiesBindingPostProcessor.class})
public class ConfigurationPropertiesRebinderAutoConfiguration implements ApplicationContextAware, SmartInitializingSingleton {
    private ApplicationContext context;

    public ConfigurationPropertiesRebinderAutoConfiguration() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(
        search = SearchStrategy.CURRENT
    )
    public ConfigurationPropertiesBeans configurationPropertiesBeans() {
        ConfigurationBeanFactoryMetadata metaData = (ConfigurationBeanFactoryMetadata)this.context.getBean(ConfigurationBeanFactoryMetadata.BEAN_NAME, ConfigurationBeanFactoryMetadata.class);
        ConfigurationPropertiesBeans beans = new ConfigurationPropertiesBeans();
        beans.setBeanMetaDataStore(metaData);
        return beans;
    }

    @Bean
    @ConditionalOnMissingBean(
        search = SearchStrategy.CURRENT
    )
    public ConfigurationPropertiesRebinder configurationPropertiesRebinder(ConfigurationPropertiesBeans beans) {
        ConfigurationPropertiesRebinder rebinder = new ConfigurationPropertiesRebinder(beans);
        return rebinder;
    }

    public void afterSingletonsInstantiated() {
        if (this.context.getParent() != null) {
            ConfigurationPropertiesRebinder rebinder = (ConfigurationPropertiesRebinder)this.context.getBean(ConfigurationPropertiesRebinder.class);
            String[] var2 = this.context.getParent().getBeanDefinitionNames();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String name = var2[var4];
                rebinder.rebind(name);
            }
        }

    }
}
