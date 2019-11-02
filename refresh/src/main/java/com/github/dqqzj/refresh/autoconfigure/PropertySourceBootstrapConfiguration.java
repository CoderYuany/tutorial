//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.autoconfigure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties({PropertySourceBootstrapProperties.class})
public class PropertySourceBootstrapConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    public static final String BOOTSTRAP_PROPERTY_SOURCE_NAME = "bootstrapProperties";
    private static Log logger = LogFactory.getLog(PropertySourceBootstrapConfiguration.class);
    private int order = -2147483638;
    @Autowired(
        required = false
    )
    private List<PropertySourceLocator> propertySourceLocators = new ArrayList();

    public PropertySourceBootstrapConfiguration() {
    }

    public int getOrder() {
        return this.order;
    }

    public void setPropertySourceLocators(Collection<PropertySourceLocator> propertySourceLocators) {
        this.propertySourceLocators = new ArrayList(propertySourceLocators);
    }

    public void initialize(ConfigurableApplicationContext applicationContext) {
        CompositePropertySource composite = new CompositePropertySource("bootstrapProperties");
        AnnotationAwareOrderComparator.sort(this.propertySourceLocators);
        boolean empty = true;
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Iterator var5 = this.propertySourceLocators.iterator();

        while(var5.hasNext()) {
            PropertySourceLocator locator = (PropertySourceLocator)var5.next();
            PropertySource<?> source = null;
            source = locator.locate(environment);
            if (source != null) {
                logger.info("Located property source: " + source);
                composite.addPropertySource(source);
                empty = false;
            }
        }

        if (!empty) {
            MutablePropertySources propertySources = environment.getPropertySources();
            String logConfig = environment.resolvePlaceholders("${logging.config:}");
            LogFile logFile = LogFile.get(environment);
            if (propertySources.contains("bootstrapProperties")) {
                propertySources.remove("bootstrapProperties");
            }

            this.insertPropertySources(propertySources, composite);
            this.reinitializeLoggingSystem(environment, logConfig, logFile);
            this.handleIncludedProfiles(environment);
        }

    }

    private void reinitializeLoggingSystem(ConfigurableEnvironment environment, String oldLogConfig, LogFile oldLogFile) {
        Map<String, Object> props = (Map)Binder.get(environment).bind("logging", Bindable.mapOf(String.class, Object.class)).orElseGet(Collections::emptyMap);
        if (!props.isEmpty()) {
            String logConfig = environment.resolvePlaceholders("${logging.config:}");
            LogFile logFile = LogFile.get(environment);
            LoggingSystem system = LoggingSystem.get(LoggingSystem.class.getClassLoader());

            try {
                ResourceUtils.getURL(logConfig).openStream().close();
                system.cleanUp();
                system.beforeInitialize();
                system.initialize(new LoggingInitializationContext(environment), logConfig, logFile);
            } catch (Exception var9) {
                logger.warn("Logging config file location '" + logConfig + "' cannot be opened and will be ignored");
            }
        }

    }

    private void insertPropertySources(MutablePropertySources propertySources, CompositePropertySource composite) {
        MutablePropertySources incoming = new MutablePropertySources();
        incoming.addFirst(composite);
        PropertySourceBootstrapProperties remoteProperties = new PropertySourceBootstrapProperties();
        Binder.get(this.environment(incoming)).bind("spring.cloud.config", Bindable.ofInstance(remoteProperties));
        if (remoteProperties.isAllowOverride() && (remoteProperties.isOverrideNone() || !remoteProperties.isOverrideSystemProperties())) {
            if (remoteProperties.isOverrideNone()) {
                propertySources.addLast(composite);
            } else {
                if (propertySources.contains("systemEnvironment")) {
                    if (!remoteProperties.isOverrideSystemProperties()) {
                        propertySources.addAfter("systemEnvironment", composite);
                    } else {
                        propertySources.addBefore("systemEnvironment", composite);
                    }
                } else {
                    propertySources.addLast(composite);
                }

            }
        } else {
            propertySources.addFirst(composite);
        }
    }

    private Environment environment(MutablePropertySources incoming) {
        StandardEnvironment environment = new StandardEnvironment();
        Iterator var3 = environment.getPropertySources().iterator();

        PropertySource source;
        while(var3.hasNext()) {
            source = (PropertySource)var3.next();
            environment.getPropertySources().remove(source.getName());
        }

        var3 = incoming.iterator();

        while(var3.hasNext()) {
            source = (PropertySource)var3.next();
            environment.getPropertySources().addLast(source);
        }

        return environment;
    }

    private void handleIncludedProfiles(ConfigurableEnvironment environment) {
        Set<String> includeProfiles = new TreeSet();
        Iterator var3 = environment.getPropertySources().iterator();

        while(var3.hasNext()) {
            PropertySource<?> propertySource = (PropertySource)var3.next();
            this.addIncludedProfilesTo(includeProfiles, propertySource);
        }

        List<String> activeProfiles = new ArrayList();
        Collections.addAll(activeProfiles, environment.getActiveProfiles());
        includeProfiles.removeAll(activeProfiles);
        if (!includeProfiles.isEmpty()) {
            Iterator var7 = includeProfiles.iterator();

            while(var7.hasNext()) {
                String profile = (String)var7.next();
                activeProfiles.add(0, profile);
            }

            environment.setActiveProfiles((String[])activeProfiles.toArray(new String[activeProfiles.size()]));
        }
    }

    private Set<String> addIncludedProfilesTo(Set<String> profiles, PropertySource<?> propertySource) {
        if (propertySource instanceof CompositePropertySource) {
            Iterator var3 = ((CompositePropertySource)propertySource).getPropertySources().iterator();

            while(var3.hasNext()) {
                PropertySource<?> nestedPropertySource = (PropertySource)var3.next();
                this.addIncludedProfilesTo(profiles, nestedPropertySource);
            }
        } else {
            Collections.addAll(profiles, this.getProfilesForValue(propertySource.getProperty("spring.profiles.include")));
        }

        return profiles;
    }

    private String[] getProfilesForValue(Object property) {
        String value = property == null ? null : property.toString();
        return StringUtils.commaDelimitedListToStringArray(value);
    }
}
