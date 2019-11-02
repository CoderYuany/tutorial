package com.github.dqqzj.refresh.autoconfigure;

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
public class ConfigurationPropertiesBindingAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext context;

    public ConfigurationPropertiesBindingAutoConfiguration() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(
        search = SearchStrategy.CURRENT
    )
    public ConfigurationPropertiesBeans configurationPropertiesBeans() {
        ConfigurationBeanFactoryMetadata metaData = this.context.getBean(ConfigurationBeanFactoryMetadata.BEAN_NAME, ConfigurationBeanFactoryMetadata.class);
        ConfigurationPropertiesBeans beans = new ConfigurationPropertiesBeans();
        beans.setBeanMetaDataStore(metaData);
        return beans;
    }
}
