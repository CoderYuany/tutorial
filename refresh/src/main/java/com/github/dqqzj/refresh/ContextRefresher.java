package com.github.dqqzj.refresh;

import java.util.*;

import com.github.dqqzj.refresh.environment.EnvironmentChangeEvent;
import com.github.dqqzj.refresh.scope.RefreshScope;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class ContextRefresher {
    private static final List<String> DEFAULT_PROPERTY_SOURCES = Arrays.asList("commandLineArgs", "defaultProperties");
    private Set<String> standardSources = new HashSet(Arrays.asList("systemProperties", "systemEnvironment", "jndiProperties", "servletConfigInitParams", "servletContextInitParams", "configurationProperties"));
    private ConfigurableApplicationContext context;
    private RefreshScope scope;

    public ContextRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        this.context = context;
        this.scope = scope;
    }

    public synchronized Set<String> refresh() {
        Map<String, Object> before = this.extract(this.context.getEnvironment().getPropertySources());
        this.refreshEnvironment();
        Set<String> keys = this.changes(before, this.extract(this.context.getEnvironment().getPropertySources())).keySet();
        this.context.publishEvent(new EnvironmentChangeEvent(this.context, keys));
        this.scope.refreshAll();
        return keys;
    }

    private void refreshEnvironment() {
        StandardEnvironment environment = this.copyEnvironment(this.context.getEnvironment());
        SpringApplicationBuilder builder = new SpringApplicationBuilder(new Class[]{ContextRefresher.Empty.class})
                .bannerMode(Mode.OFF).web(WebApplicationType.NONE).environment(environment);
        builder.application().addListeners(new ConfigFileApplicationListener());
        builder.run();
        MutablePropertySources target = this.context.getEnvironment().getPropertySources();
        String targetName = null;
        Iterator var6 = environment.getPropertySources().iterator();
        while (var6.hasNext()) {
            PropertySource<?> source = (PropertySource) var6.next();
            String name = source.getName();
            if (target.contains(name)) {
                targetName = name;
            }
            if (!this.standardSources.contains(name)) {
                if (target.contains(name)) {
                    target.replace(name, source);
                } else if (targetName != null) {
                    target.addAfter(targetName, source);
                } else {
                    target.addFirst(source);
                    targetName = name;
                }
            }
        }
    }

    /**
     * @return org.springframework.core.env.StandardEnvironment
     * @author qinzhongjian
     * @date 2019/11/3
     * @param: configurableEnvironment
     * @description: 将当前运行环境参数拷贝到新建环境中
     */
    private StandardEnvironment copyEnvironment(ConfigurableEnvironment configurableEnvironment) {
        StandardEnvironment environment = new StandardEnvironment();
        MutablePropertySources capturedPropertySources = environment.getPropertySources();
        DEFAULT_PROPERTY_SOURCES.forEach(propertyName -> {
            if (configurableEnvironment.getPropertySources().contains(propertyName)) {
                if (capturedPropertySources.contains(propertyName)) {
                    capturedPropertySources.replace(propertyName, configurableEnvironment.getPropertySources().get(propertyName));
                } else {
                    capturedPropertySources.addLast(configurableEnvironment.getPropertySources().get(propertyName));
                }
            }
        });
        environment.setActiveProfiles(configurableEnvironment.getActiveProfiles());
        environment.setDefaultProfiles(configurableEnvironment.getDefaultProfiles());
        return environment;
    }

    private Map<String, Object> changes(Map<String, Object> before, Map<String, Object> after) {
        Map<String, Object> result = new HashMap();
        Iterator var4 = before.keySet().iterator();

        String key;
        while (var4.hasNext()) {
            key = (String) var4.next();
            if (!after.containsKey(key)) {
                result.put(key, (Object) null);
            } else if (!this.equal(before.get(key), after.get(key))) {
                result.put(key, after.get(key));
            }
        }

        var4 = after.keySet().iterator();

        while (var4.hasNext()) {
            key = (String) var4.next();
            if (!before.containsKey(key)) {
                result.put(key, after.get(key));
            }
        }

        return result;
    }

    private boolean equal(Object one, Object two) {
        if (one == null && two == null) {
            return true;
        } else {
            return one != null && two != null ? one.equals(two) : false;
        }
    }

    private Map<String, Object> extract(MutablePropertySources propertySources) {
        Map<String, Object> result = new HashMap();
        Iterator propertySourceIterator = propertySources.iterator();
        PropertySource source;
        while (propertySourceIterator.hasNext()) {
            source = (PropertySource) propertySourceIterator.next();
            if (!this.standardSources.contains(source.getName())) {
                this.extract(source, result);
            }
        }
        return result;
    }

    private void extract(PropertySource<?> parent, Map<String, Object> result) {
        if (parent instanceof CompositePropertySource) {
            try {
                Iterator sourceIterator = ((CompositePropertySource) parent).getPropertySources().iterator();
                PropertySource source;
                while (sourceIterator.hasNext()) {
                    source = (PropertySource) sourceIterator.next();
                    this.extract(source, result);
                }
            } catch (Exception var7) {
                return;
            }
        } else if (parent instanceof EnumerablePropertySource) {
            String[] propertyNames = ((EnumerablePropertySource) parent).getPropertyNames();
            int propertyNameNumbers = propertyNames.length;
            for (int i = 0; i < propertyNameNumbers; ++i) {
                String key = propertyNames[i];
                result.put(key, parent.getProperty(key));
            }
        }

    }

    @Configuration
    protected static class Empty {
        protected Empty() {
        }
    }
}
