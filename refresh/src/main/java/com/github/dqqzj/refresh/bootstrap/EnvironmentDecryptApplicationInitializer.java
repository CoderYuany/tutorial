//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.bootstrap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.dqqzj.refresh.environment.EnvironmentChangeEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.SystemEnvironmentPropertySource;

public class EnvironmentDecryptApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    public static final String DECRYPTED_PROPERTY_SOURCE_NAME = "decrypted";
    public static final String DECRYPTED_BOOTSTRAP_PROPERTY_SOURCE_NAME = "decryptedBootstrap";
    private int order = -2147483633;
    private static Log logger = LogFactory.getLog(EnvironmentDecryptApplicationInitializer.class);
    //private TextEncryptor encryptor;
    private boolean failOnError = true;
    private static final Pattern COLLECTION_PROPERTY = Pattern.compile("(\\S+)?\\[(\\d+)\\](\\.\\S+)?");

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

   /* public EnvironmentDecryptApplicationInitializer(TextEncryptor encryptor) {
        this.encryptor = encryptor;
    }*/

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        Set<String> found = new LinkedHashSet();
        Map<String, Object> map = this.decrypt((PropertySources)propertySources);
        if (!map.isEmpty()) {
            found.addAll(map.keySet());
            this.insert((ApplicationContext)applicationContext, new SystemEnvironmentPropertySource("decrypted", map));
        }

        PropertySource<?> bootstrap = propertySources.get("bootstrap");
        if (bootstrap != null) {
            map = this.decrypt(bootstrap);
            if (!map.isEmpty()) {
                found.addAll(map.keySet());
                this.insert((ApplicationContext)applicationContext, new SystemEnvironmentPropertySource("decryptedBootstrap", map));
            }
        }

        if (!found.isEmpty()) {
            ApplicationContext parent = applicationContext.getParent();
            if (parent != null) {
                parent.publishEvent(new EnvironmentChangeEvent(parent, found));
            }
        }

    }

    private void insert(ApplicationContext applicationContext, PropertySource<?> propertySource) {
        for(ApplicationContext parent = applicationContext; parent != null; parent = parent.getParent()) {
            if (parent.getEnvironment() instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment mutable = (ConfigurableEnvironment)parent.getEnvironment();
                this.insert(mutable.getPropertySources(), propertySource);
            }
        }

    }

    private void insert(MutablePropertySources propertySources, PropertySource<?> propertySource) {
        if (propertySources.contains("bootstrap")) {
            if ("decryptedBootstrap".equals(propertySource.getName())) {
                propertySources.addBefore("bootstrap", propertySource);
            } else {
                propertySources.addAfter("bootstrap", propertySource);
            }
        } else {
            propertySources.addFirst(propertySource);
        }

    }

    public Map<String, Object> decrypt(PropertySources propertySources) {
        Map<String, Object> overrides = new LinkedHashMap();
        List<PropertySource<?>> sources = new ArrayList();
        Iterator var4 = propertySources.iterator();

        PropertySource source;
        while(var4.hasNext()) {
            source = (PropertySource)var4.next();
            sources.add(0, source);
        }

        var4 = sources.iterator();

        while(var4.hasNext()) {
            source = (PropertySource)var4.next();
            this.decrypt(source, overrides);
        }

        return overrides;
    }

    private Map<String, Object> decrypt(PropertySource<?> source) {
        Map<String, Object> overrides = new LinkedHashMap();
        this.decrypt(source, overrides);
        return overrides;
    }

    private void decrypt(PropertySource<?> source, Map<String, Object> overrides) {
        if (!(source instanceof EnumerablePropertySource)) {
            if (source instanceof CompositePropertySource) {
                Iterator var15 = ((CompositePropertySource)source).getPropertySources().iterator();

                while(var15.hasNext()) {
                    PropertySource<?> nested = (PropertySource)var15.next();
                    this.decrypt(nested, overrides);
                }
            }
        } else {
            Map<String, Object> otherCollectionProperties = new LinkedHashMap();
            boolean sourceHasDecryptedCollection = false;
            EnumerablePropertySource<?> enumerable = (EnumerablePropertySource)source;
            String[] var6 = enumerable.getPropertyNames();
            int var7 = var6.length;
            int var8 = 0;

            while(true) {
                if (var8 >= var7) {
                    if (sourceHasDecryptedCollection && !otherCollectionProperties.isEmpty()) {
                        overrides.putAll(otherCollectionProperties);
                    }
                    break;
                }

                String key = var6[var8];
                Object property = source.getProperty(key);
                if (property != null) {
                    String value = property.toString();
                    if (value.startsWith("{cipher}")) {
                        value = value.substring("{cipher}".length());

//                        try {
//                            value = this.encryptor.decrypt(value);
//                            if (logger.isDebugEnabled()) {
//                                logger.debug("Decrypted: key=" + key);
//                            }
//                        } catch (Exception var14) {
//                            String message = "Cannot decrypt: key=" + key;
//                            if (this.failOnError) {
//                                throw new IllegalStateException(message, var14);
//                            }
//
//                            if (logger.isDebugEnabled()) {
//                                logger.warn(message, var14);
//                            } else {
//                                logger.warn(message);
//                            }
//
//                            value = "";
//                        }

                        overrides.put(key, value);
                        if (COLLECTION_PROPERTY.matcher(key).matches()) {
                            sourceHasDecryptedCollection = true;
                        }
                    } else if (COLLECTION_PROPERTY.matcher(key).matches()) {
                        otherCollectionProperties.put(key, value);
                    }
                }

                ++var8;
            }
        }

    }
}
