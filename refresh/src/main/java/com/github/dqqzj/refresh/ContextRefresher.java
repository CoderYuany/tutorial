package com.github.dqqzj.refresh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class ContextRefresher {
    private static final String[] DEFAULT_PROPERTY_SOURCES = new String[]{"commandLineArgs", "defaultProperties"};
    private Set<String> standardSources = new HashSet(Arrays.asList("systemProperties", "systemEnvironment", "jndiProperties", "servletConfigInitParams", "servletContextInitParams", "configurationProperties"));
    private ConfigurableApplicationContext context;
    private RefreshScope scope;

    public ContextRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        this.context = context;
        this.scope = scope;
    }

    public synchronized Set<String> refresh() {
        Map<String, Object> before = this.extract(this.context.getEnvironment().getPropertySources());
        this.addConfigFilesToEnvironment();
        Set<String> keys = this.changes(before, this.extract(this.context.getEnvironment().getPropertySources())).keySet();
        this.context.publishEvent(new EnvironmentChangeEvent(this.context, keys));
        this.scope.refreshAll();
        return keys;
    }

    ConfigurableApplicationContext addConfigFilesToEnvironment() {
        ConfigurableApplicationContext capture = null;
        boolean var15 = false;

        try {
            var15 = true;
            StandardEnvironment environment = this.copyEnvironment(this.context.getEnvironment());
            SpringApplicationBuilder builder = (new SpringApplicationBuilder(new Class[]{ContextRefresher.Empty.class})).bannerMode(Mode.OFF).web(WebApplicationType.NONE).environment(environment);
            builder.application().setListeners(Arrays.asList(new ConfigFileApplicationListener()));
            capture = builder.run(new String[0]);
            if (environment.getPropertySources().contains("refreshArgs")) {
                environment.getPropertySources().remove("refreshArgs");
            }

            MutablePropertySources target = this.context.getEnvironment().getPropertySources();
            String targetName = null;
            Iterator var6 = environment.getPropertySources().iterator();

            while(var6.hasNext()) {
                PropertySource<?> source = (PropertySource)var6.next();
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

            var15 = false;
        } finally {
            if (var15) {
                for(ConfigurableApplicationContext closeable = capture; closeable != null; closeable = (ConfigurableApplicationContext)closeable.getParent()) {
                    try {
                        closeable.close();
                    } catch (Exception var16) {
                    }

                    if (!(closeable.getParent() instanceof ConfigurableApplicationContext)) {
                        break;
                    }
                }

            }
        }

        for(ConfigurableApplicationContext closeable = capture; closeable != null; closeable = (ConfigurableApplicationContext)closeable.getParent()) {
            try {
                closeable.close();
            } catch (Exception var17) {
            }

            if (!(closeable.getParent() instanceof ConfigurableApplicationContext)) {
                break;
            }
        }

        return capture;
    }

    private StandardEnvironment copyEnvironment(ConfigurableEnvironment input) {
        StandardEnvironment environment = new StandardEnvironment();
        MutablePropertySources capturedPropertySources = environment.getPropertySources();
        String[] var4 = DEFAULT_PROPERTY_SOURCES;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String name = var4[var6];
            if (input.getPropertySources().contains(name)) {
                if (capturedPropertySources.contains(name)) {
                    capturedPropertySources.replace(name, input.getPropertySources().get(name));
                } else {
                    capturedPropertySources.addLast(input.getPropertySources().get(name));
                }
            }
        }

        environment.setActiveProfiles(input.getActiveProfiles());
        environment.setDefaultProfiles(input.getDefaultProfiles());
        Map<String, Object> map = new HashMap();
        map.put("spring.jmx.enabled", false);
        map.put("spring.main.sources", "");
        capturedPropertySources.addFirst(new MapPropertySource("refreshArgs", map));
        return environment;
    }

    private Map<String, Object> changes(Map<String, Object> before, Map<String, Object> after) {
        Map<String, Object> result = new HashMap();
        Iterator var4 = before.keySet().iterator();

        String key;
        while(var4.hasNext()) {
            key = (String)var4.next();
            if (!after.containsKey(key)) {
                result.put(key, (Object)null);
            } else if (!this.equal(before.get(key), after.get(key))) {
                result.put(key, after.get(key));
            }
        }

        var4 = after.keySet().iterator();

        while(var4.hasNext()) {
            key = (String)var4.next();
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
            if (!this.standardSources.contains(source.getName())) {
                this.extract(source, result);
            }
        }

        return result;
    }

    private void extract(PropertySource<?> parent, Map<String, Object> result) {
        if (parent instanceof CompositePropertySource) {
            try {
                List<PropertySource<?>> sources = new ArrayList();
                Iterator var4 = ((CompositePropertySource)parent).getPropertySources().iterator();

                PropertySource source;
                while(var4.hasNext()) {
                    source = (PropertySource)var4.next();
                    sources.add(0, source);
                }

                var4 = sources.iterator();

                while(var4.hasNext()) {
                    source = (PropertySource)var4.next();
                    this.extract(source, result);
                }
            } catch (Exception var7) {
                return;
            }
        } else if (parent instanceof EnumerablePropertySource) {
            String[] var8 = ((EnumerablePropertySource)parent).getPropertyNames();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                String key = var8[var10];
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
