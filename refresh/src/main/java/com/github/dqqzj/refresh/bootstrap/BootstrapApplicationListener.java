//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.bootstrap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.core.env.PropertySource.StubPropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public class BootstrapApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
    public static final String BOOTSTRAP_PROPERTY_SOURCE_NAME = "bootstrap";
    public static final int DEFAULT_ORDER = -2147483643;
    public static final String DEFAULT_PROPERTIES = "defaultProperties";
    private int order = -2147483643;

    public BootstrapApplicationListener() {
    }

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        if ((Boolean)environment.getProperty("spring.cloud.bootstrap.enabled", Boolean.class, true)) {
            if (!environment.getPropertySources().contains("bootstrap")) {
                ConfigurableApplicationContext context = null;
                String configName = environment.resolvePlaceholders("${spring.cloud.bootstrap.name:bootstrap}");
                Iterator var5 = event.getSpringApplication().getInitializers().iterator();

                while(var5.hasNext()) {
                    ApplicationContextInitializer<?> initializer = (ApplicationContextInitializer)var5.next();
                    if (initializer instanceof ParentContextApplicationContextInitializer) {
                        context = this.findBootstrapContext((ParentContextApplicationContextInitializer)initializer, configName);
                    }
                }

                if (context == null) {
                    context = this.bootstrapServiceContext(environment, event.getSpringApplication(), configName);
                }

                this.apply(context, event.getSpringApplication(), environment);
            }
        }
    }

    private ConfigurableApplicationContext findBootstrapContext(ParentContextApplicationContextInitializer initializer, String configName) {
        Field field = ReflectionUtils.findField(ParentContextApplicationContextInitializer.class, "parent");
        ReflectionUtils.makeAccessible(field);
        ConfigurableApplicationContext parent = (ConfigurableApplicationContext)this.safeCast(ConfigurableApplicationContext.class, ReflectionUtils.getField(field, initializer));
        if (parent != null && !configName.equals(parent.getId())) {
            parent = (ConfigurableApplicationContext)this.safeCast(ConfigurableApplicationContext.class, parent.getParent());
        }

        return parent;
    }

    private <T> T safeCast(Class<T> type, Object object) {
        try {
            return type.cast(object);
        } catch (ClassCastException var4) {
            return null;
        }
    }

    private ConfigurableApplicationContext bootstrapServiceContext(ConfigurableEnvironment environment, final SpringApplication application, String configName) {
        StandardEnvironment bootstrapEnvironment = new StandardEnvironment();
        MutablePropertySources bootstrapProperties = bootstrapEnvironment.getPropertySources();
        Iterator var6 = bootstrapProperties.iterator();

        while(var6.hasNext()) {
            PropertySource<?> source = (PropertySource)var6.next();
            bootstrapProperties.remove(source.getName());
        }

        String configLocation = environment.resolvePlaceholders("${spring.cloud.bootstrap.location:}");
        Map<String, Object> bootstrapMap = new HashMap();
        bootstrapMap.put("spring.config.name", configName);
        bootstrapMap.put("spring.main.web-application-type", "none");
        if (StringUtils.hasText(configLocation)) {
            bootstrapMap.put("spring.config.location", configLocation);
        }

        bootstrapProperties.addFirst(new MapPropertySource("bootstrap", bootstrapMap));
        Iterator var8 = environment.getPropertySources().iterator();

        while(var8.hasNext()) {
            PropertySource<?> source = (PropertySource)var8.next();
            if (!(source instanceof StubPropertySource)) {
                bootstrapProperties.addLast(source);
            }
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> names = new ArrayList(SpringFactoriesLoader.loadFactoryNames(BootstrapConfiguration.class, classLoader));
        String[] var10 = StringUtils.commaDelimitedListToStringArray(environment.getProperty("spring.cloud.bootstrap.sources", ""));
        int var11 = var10.length;

        String name;
        for(int var12 = 0; var12 < var11; ++var12) {
            name = var10[var12];
            names.add(name);
        }

        SpringApplicationBuilder builder = (new SpringApplicationBuilder(new Class[0])).profiles(environment.getActiveProfiles()).bannerMode(Mode.OFF).environment(bootstrapEnvironment).registerShutdownHook(false).logStartupInfo(false).web(WebApplicationType.NONE);
        if (environment.getPropertySources().contains("refreshArgs")) {
            builder.application().setListeners(this.filterListeners(builder.application().getListeners()));
        }

        List<Class<?>> sources = new ArrayList();
        Iterator var23 = names.iterator();

        while(var23.hasNext()) {
            name = (String)var23.next();
            Class cls = ClassUtils.resolveClassName(name, (ClassLoader)null);

            try {
                cls.getDeclaredAnnotations();
            } catch (Exception var16) {
                continue;
            }

            sources.add(cls);
        }

        AnnotationAwareOrderComparator.sort(sources);
        builder.sources((Class[])sources.toArray(new Class[sources.size()]));
        ConfigurableApplicationContext context = builder.run(new String[0]);
        context.setId("bootstrap");
        this.addAncestorInitializer(application, context);
        bootstrapProperties.remove("bootstrap");
        this.mergeDefaultProperties(environment.getPropertySources(), bootstrapProperties);
        return context;
    }

    private Collection<? extends ApplicationListener<?>> filterListeners(Set<ApplicationListener<?>> listeners) {
        Set<ApplicationListener<?>> result = new LinkedHashSet();
        Iterator var3 = listeners.iterator();

        while(var3.hasNext()) {
            ApplicationListener<?> listener = (ApplicationListener)var3.next();
            if (!(listener instanceof LoggingApplicationListener)) {
                result.add(listener);
            }
        }

        return result;
    }

    private void mergeDefaultProperties(MutablePropertySources environment, MutablePropertySources bootstrap) {
        String name = "defaultProperties";
        if (bootstrap.contains(name)) {
            PropertySource<?> source = bootstrap.get(name);
            if (!environment.contains(name)) {
                environment.addLast(source);
            } else {
                PropertySource<?> target = environment.get(name);
                if (target instanceof MapPropertySource) {
                    Map<String, Object> targetMap = (Map)((MapPropertySource)target).getSource();
                    if (target != source && source instanceof MapPropertySource) {
                        Map<String, Object> map = (Map)((MapPropertySource)source).getSource();
                        Iterator var8 = map.keySet().iterator();

                        while(var8.hasNext()) {
                            String key = (String)var8.next();
                            if (!target.containsProperty(key)) {
                                targetMap.put(key, map.get(key));
                            }
                        }
                    }
                }
            }
        }

        this.mergeAdditionalPropertySources(environment, bootstrap);
    }

    private void mergeAdditionalPropertySources(MutablePropertySources environment, MutablePropertySources bootstrap) {
        PropertySource<?> defaultProperties = environment.get("defaultProperties");
        BootstrapApplicationListener.ExtendedDefaultPropertySource result = defaultProperties instanceof BootstrapApplicationListener.ExtendedDefaultPropertySource ? (BootstrapApplicationListener.ExtendedDefaultPropertySource)defaultProperties : new BootstrapApplicationListener.ExtendedDefaultPropertySource("defaultProperties", defaultProperties);
        Iterator var5 = bootstrap.iterator();

        while(var5.hasNext()) {
            PropertySource<?> source = (PropertySource)var5.next();
            if (!environment.contains(source.getName())) {
                result.add(source);
            }
        }

        var5 = result.getPropertySourceNames().iterator();

        while(var5.hasNext()) {
            String name = (String)var5.next();
            bootstrap.remove(name);
        }

        this.addOrReplace(environment, result);
        this.addOrReplace(bootstrap, result);
    }

    private void addOrReplace(MutablePropertySources environment, PropertySource<?> result) {
        if (environment.contains(result.getName())) {
            environment.replace(result.getName(), result);
        } else {
            environment.addLast(result);
        }

    }

    private void addAncestorInitializer(SpringApplication application, ConfigurableApplicationContext context) {
        boolean installed = false;
        Iterator var4 = application.getInitializers().iterator();

        while(var4.hasNext()) {
            ApplicationContextInitializer<?> initializer = (ApplicationContextInitializer)var4.next();
            if (initializer instanceof BootstrapApplicationListener.AncestorInitializer) {
                installed = true;
                ((BootstrapApplicationListener.AncestorInitializer)initializer).setParent(context);
            }
        }

        if (!installed) {
            application.addInitializers(new ApplicationContextInitializer[]{new BootstrapApplicationListener.AncestorInitializer(context)});
        }

    }

    private void apply(ConfigurableApplicationContext context, SpringApplication application, ConfigurableEnvironment environment) {
        List<ApplicationContextInitializer> initializers = this.getOrderedBeansOfType(context, ApplicationContextInitializer.class);
        application.addInitializers((ApplicationContextInitializer[])initializers.toArray(new ApplicationContextInitializer[initializers.size()]));
        this.addBootstrapDecryptInitializer(application);
    }

    private void addBootstrapDecryptInitializer(SpringApplication application) {
        BootstrapApplicationListener.DelegatingEnvironmentDecryptApplicationInitializer decrypter = null;
        Iterator var3 = application.getInitializers().iterator();

        while(var3.hasNext()) {
            ApplicationContextInitializer<ConfigurableApplicationContext> initializer = (ApplicationContextInitializer)var3.next();
            if (initializer instanceof EnvironmentDecryptApplicationInitializer) {
                decrypter = new BootstrapApplicationListener.DelegatingEnvironmentDecryptApplicationInitializer(initializer);
            }
        }

        if (decrypter != null) {
            application.addInitializers(new ApplicationContextInitializer[]{decrypter});
        }

    }

    private <T> List<T> getOrderedBeansOfType(ListableBeanFactory context, Class<T> type) {
        List<T> result = new ArrayList();
        String[] var4 = context.getBeanNamesForType(type);
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String name = var4[var6];
            result.add(context.getBean(name, type));
        }

        AnnotationAwareOrderComparator.sort(result);
        return result;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    private static class ExtendedDefaultPropertySource extends SystemEnvironmentPropertySource {
        private final CompositePropertySource sources;
        private final List<String> names = new ArrayList();

        public ExtendedDefaultPropertySource(String name, PropertySource<?> propertySource) {
            super(name, findMap(propertySource));
            this.sources = new CompositePropertySource(name);
        }

        public CompositePropertySource getPropertySources() {
            return this.sources;
        }

        public List<String> getPropertySourceNames() {
            return this.names;
        }

        public void add(PropertySource<?> source) {
            if (source instanceof EnumerablePropertySource && !this.names.contains(source.getName())) {
                this.sources.addPropertySource(source);
                this.names.add(source.getName());
            }

        }

        public Object getProperty(String name) {
            return this.sources.containsProperty(name) ? this.sources.getProperty(name) : super.getProperty(name);
        }

        public boolean containsProperty(String name) {
            return this.sources.containsProperty(name) ? true : super.containsProperty(name);
        }

        public String[] getPropertyNames() {
            List<String> names = new ArrayList();
            names.addAll(Arrays.asList(this.sources.getPropertyNames()));
            names.addAll(Arrays.asList(super.getPropertyNames()));
            return (String[])names.toArray(new String[0]);
        }

        private static Map<String, Object> findMap(PropertySource<?> propertySource) {
            return (Map)(propertySource instanceof MapPropertySource ? (Map)propertySource.getSource() : new LinkedHashMap());
        }
    }

    @Order(-2147483639)
    private static class DelegatingEnvironmentDecryptApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private ApplicationContextInitializer<ConfigurableApplicationContext> delegate;

        public DelegatingEnvironmentDecryptApplicationInitializer(ApplicationContextInitializer<ConfigurableApplicationContext> delegate) {
            this.delegate = delegate;
        }

        public void initialize(ConfigurableApplicationContext applicationContext) {
            this.delegate.initialize(applicationContext);
        }
    }

    private static class AncestorInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
        private ConfigurableApplicationContext parent;

        public AncestorInitializer(ConfigurableApplicationContext parent) {
            this.parent = parent;
        }

        public void setParent(ConfigurableApplicationContext parent) {
            this.parent = parent;
        }

        public int getOrder() {
            return -2147483643;
        }

        public void initialize(ConfigurableApplicationContext context) {
            while(context.getParent() != null && context.getParent() != context) {
                context = (ConfigurableApplicationContext)context.getParent();
            }

            this.reorderSources(context.getEnvironment());
            (new ParentContextApplicationContextInitializer(this.parent)).initialize(context);
        }

        private void reorderSources(ConfigurableEnvironment environment) {
            PropertySource<?> removed = environment.getPropertySources().remove("defaultProperties");
            if (removed instanceof BootstrapApplicationListener.ExtendedDefaultPropertySource) {
                BootstrapApplicationListener.ExtendedDefaultPropertySource defaultProperties = (BootstrapApplicationListener.ExtendedDefaultPropertySource)removed;
                environment.getPropertySources().addLast(new MapPropertySource("defaultProperties", (Map)defaultProperties.getSource()));
                Iterator var4 = defaultProperties.getPropertySources().getPropertySources().iterator();

                while(var4.hasNext()) {
                    PropertySource<?> source = (PropertySource)var4.next();
                    if (!environment.getPropertySources().contains(source.getName())) {
                        environment.getPropertySources().addBefore("defaultProperties", source);
                    }
                }
            }

        }
    }
}
