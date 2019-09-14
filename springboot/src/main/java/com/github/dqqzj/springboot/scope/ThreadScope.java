package com.github.dqqzj.springboot.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 18:49
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class ThreadScope implements Scope {
    private final ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(() -> new HashMap<>());
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = threadLocal.get();
        Object obj = scope.get(name);
        if (obj == null) {
            obj = objectFactory.getObject();
            scope.put(name, obj);

            System.out.println("Not exists " + name + "; hashCode: " + obj.hashCode());
        } else {
            System.out.println("Exists " + name + "; hashCode: " + obj.hashCode());
        }
        return obj;
    }
    @Override
    public Object remove(String name) {
        Map<String, Object> scope = threadLocal.get();
        return scope.remove(name);
    }

    @Override
    public String getConversationId() {
        return null;
    }
    @Override
    public void registerDestructionCallback(String arg0, Runnable arg1) {
    }
    @Override
    public Object resolveContextualObject(String arg0) {
        return null;
    }


}
