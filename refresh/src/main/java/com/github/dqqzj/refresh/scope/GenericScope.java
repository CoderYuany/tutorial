package com.github.dqqzj.refresh.scope;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public class GenericScope implements Scope, BeanFactoryPostProcessor, BeanDefinitionRegistryPostProcessor, DisposableBean {
    private static final Log logger = LogFactory.getLog(GenericScope.class);
    public static final String SCOPED_TARGET_PREFIX = "scopedTarget.";
    private GenericScope.BeanLifecycleWrapperCache cache = new GenericScope.BeanLifecycleWrapperCache(new StandardScopeCache());
    private String name = "generic";
    private ConfigurableListableBeanFactory beanFactory;
    private StandardEvaluationContext evaluationContext;
    private String id;
    private Map<String, Exception> errors = new ConcurrentHashMap();
    private ConcurrentMap<String, ReadWriteLock> locks = new ConcurrentHashMap();

    public GenericScope() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScopeCache(ScopeCache cache) {
        this.cache = new GenericScope.BeanLifecycleWrapperCache(cache);
    }

    public Map<String, Exception> getErrors() {
        return this.errors;
    }

    public void destroy() {
        List<Throwable> errors = new ArrayList();
        Collection<GenericScope.BeanLifecycleWrapper> wrappers = this.cache.clear();
        Iterator var3 = wrappers.iterator();

        while(var3.hasNext()) {
            GenericScope.BeanLifecycleWrapper wrapper = (GenericScope.BeanLifecycleWrapper)var3.next();

            try {
                Lock lock = (this.locks.get(wrapper.getName())).writeLock();
                lock.lock();

                try {
                    wrapper.destroy();
                } finally {
                    lock.unlock();
                }
            } catch (RuntimeException var10) {
                errors.add(var10);
            }
        }

        if (!errors.isEmpty()) {
            throw wrapIfNecessary(errors.get(0));
        } else {
            this.errors.clear();
        }
    }

    protected boolean destroy(String name) {
        GenericScope.BeanLifecycleWrapper wrapper = this.cache.remove(name);
        if (wrapper != null) {
            Lock lock = (this.locks.get(wrapper.getName())).writeLock();
            lock.lock();

            try {
                wrapper.destroy();
            } finally {
                lock.unlock();
            }
            this.errors.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public Object get(String name, ObjectFactory<?> objectFactory) {
        GenericScope.BeanLifecycleWrapper value = this.cache.put(name, new GenericScope.BeanLifecycleWrapper(name, objectFactory));
        this.locks.putIfAbsent(name, new ReentrantReadWriteLock());

        try {
            return value.getBean();
        } catch (RuntimeException var5) {
            this.errors.put(name, var5);
            throw var5;
        }
    }

    public String getConversationId() {
        return this.name;
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        GenericScope.BeanLifecycleWrapper value = this.cache.get(name);
        if (value != null) {
            value.setDestroyCallback(callback);
        }
    }

    public Object remove(String name) {
        GenericScope.BeanLifecycleWrapper value = this.cache.remove(name);
        return value == null ? null : value.getBean();
    }

    public Object resolveContextualObject(String key) {
        Expression expression = this.parseExpression(key);
        return expression.getValue(this.evaluationContext, this.beanFactory);
    }

    private Expression parseExpression(String input) {
        if (StringUtils.hasText(input)) {
            SpelExpressionParser parser = new SpelExpressionParser();

            try {
                return parser.parseExpression(input);
            } catch (ParseException var4) {
                throw new IllegalArgumentException("Cannot parse expression: " + input, var4);
            }
        } else {
            return null;
        }
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        beanFactory.registerScope(this.name, this);
        this.setSerializationId(beanFactory);
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] var2 = registry.getBeanDefinitionNames();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String name = var2[var4];
            BeanDefinition definition = registry.getBeanDefinition(name);
            if (definition instanceof RootBeanDefinition) {
                RootBeanDefinition root = (RootBeanDefinition)definition;
                if (root.getDecoratedDefinition() != null && root.hasBeanClass() && root.getBeanClass() == ScopedProxyFactoryBean.class && this.getName().equals(root.getDecoratedDefinition().getBeanDefinition().getScope())) {
                    root.setBeanClass(GenericScope.LockedScopedProxyFactoryBean.class);
                    root.getConstructorArgumentValues().addGenericArgumentValue(this);
                    root.setSynthetic(true);
                }
            }
        }

    }

    private void setSerializationId(ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            String id = this.id;
            if (id == null) {
                List<String> list = new ArrayList(Arrays.asList(beanFactory.getBeanDefinitionNames()));
                Collections.sort(list);
                String names = list.toString();
                logger.debug("Generating bean factory id from names: " + names);
                id = UUID.nameUUIDFromBytes(names.getBytes()).toString();
            }

            logger.info("BeanFactory id=" + id);
            ((DefaultListableBeanFactory)beanFactory).setSerializationId(id);
        } else {
            logger.warn("BeanFactory was not a DefaultListableBeanFactory, scoped proxy beans cannot be serialized.");
        }

    }

    static RuntimeException wrapIfNecessary(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException)throwable;
        } else if (throwable instanceof Error) {
            throw (Error)throwable;
        } else {
            return new IllegalStateException(throwable);
        }
    }

    protected String getName() {
        return this.name;
    }

    protected ReadWriteLock getLock(String beanName) {
        return this.locks.get(beanName);
    }

    public static class LockedScopedProxyFactoryBean<S extends GenericScope> extends ScopedProxyFactoryBean implements MethodInterceptor {
        private final S scope;
        private String targetBeanName;

        public LockedScopedProxyFactoryBean(S scope) {
            this.scope = scope;
        }

        public void setBeanFactory(BeanFactory beanFactory) {
            super.setBeanFactory(beanFactory);
            Object proxy = this.getObject();
            if (proxy instanceof Advised) {
                Advised advised = (Advised)proxy;
                advised.addAdvice(0, this);
            }

        }

        public void setTargetBeanName(String targetBeanName) {
            super.setTargetBeanName(targetBeanName);
            this.targetBeanName = targetBeanName;
        }

        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            if (!AopUtils.isEqualsMethod(method) && !AopUtils.isToStringMethod(method) && !AopUtils.isHashCodeMethod(method) && !this.isScopedObjectGetTargetObject(method)) {
                Object proxy = this.getObject();
                ReadWriteLock readWriteLock = this.scope.getLock(this.targetBeanName);
                if (readWriteLock == null) {
                    if (GenericScope.logger.isDebugEnabled()) {
                        GenericScope.logger.debug("For bean with name [" + this.targetBeanName + "] there is no read write lock. Will create a new one to avoid NPE");
                    }

                    readWriteLock = new ReentrantReadWriteLock();
                }

                Lock lock = (readWriteLock).readLock();
                lock.lock();

                Object var7;
                try {
                    if (!(proxy instanceof Advised)) {
                        Object var13 = invocation.proceed();
                        return var13;
                    }

                    Advised advised = (Advised)proxy;
                    ReflectionUtils.makeAccessible(method);
                    var7 = ReflectionUtils.invokeMethod(method, advised.getTargetSource().getTarget(), invocation.getArguments());
                } catch (UndeclaredThrowableException var11) {
                    throw var11.getUndeclaredThrowable();
                } finally {
                    lock.unlock();
                }

                return var7;
            } else {
                return invocation.proceed();
            }
        }

        private boolean isScopedObjectGetTargetObject(Method method) {
            return method.getDeclaringClass().equals(ScopedObject.class) && method.getName().equals("getTargetObject") && method.getParameterTypes().length == 0;
        }
    }

    private static class BeanLifecycleWrapper {
        private Object bean;
        private Runnable callback;
        private final String name;
        private final ObjectFactory<?> objectFactory;

        public BeanLifecycleWrapper(String name, ObjectFactory<?> objectFactory) {
            this.name = name;
            this.objectFactory = objectFactory;
        }

        public String getName() {
            return this.name;
        }

        public void setDestroyCallback(Runnable callback) {
            this.callback = callback;
        }

        public Object getBean() {
            if (this.bean == null) {
                synchronized(this.name) {
                    if (this.bean == null) {
                        this.bean = this.objectFactory.getObject();
                    }
                }
            }

            return this.bean;
        }

        public void destroy() {
            if (this.callback != null) {
                synchronized(this.name) {
                    Runnable callback = this.callback;
                    if (callback != null) {
                        callback.run();
                    }

                    this.callback = null;
                    this.bean = null;
                }
            }
        }

    }

    private static class BeanLifecycleWrapperCache {
        private final ScopeCache cache;

        public BeanLifecycleWrapperCache(ScopeCache cache) {
            this.cache = cache;
        }

        public GenericScope.BeanLifecycleWrapper remove(String name) {
            return (GenericScope.BeanLifecycleWrapper)this.cache.remove(name);
        }

        public Collection<GenericScope.BeanLifecycleWrapper> clear() {
            Collection<Object> values = this.cache.clear();
            Collection<GenericScope.BeanLifecycleWrapper> wrappers = new LinkedHashSet();
            Iterator var3 = values.iterator();

            while(var3.hasNext()) {
                Object object = var3.next();
                wrappers.add((GenericScope.BeanLifecycleWrapper)object);
            }

            return wrappers;
        }

        public GenericScope.BeanLifecycleWrapper get(String name) {
            return (GenericScope.BeanLifecycleWrapper)this.cache.get(name);
        }

        public GenericScope.BeanLifecycleWrapper put(String name, GenericScope.BeanLifecycleWrapper value) {
            return (GenericScope.BeanLifecycleWrapper)this.cache.put(name, value);
        }
    }
}
