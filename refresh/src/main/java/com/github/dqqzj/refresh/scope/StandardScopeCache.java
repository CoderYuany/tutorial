//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StandardScopeCache implements ScopeCache {
    private final ConcurrentMap<String, Object> cache = new ConcurrentHashMap();

    public StandardScopeCache() {
    }

    public Object remove(String name) {
        return this.cache.remove(name);
    }

    public Collection<Object> clear() {
        Collection<Object> values = new ArrayList(this.cache.values());
        this.cache.clear();
        return values;
    }

    public Object get(String name) {
        return this.cache.get(name);
    }

    public Object put(String name, Object value) {
        Object result = this.cache.putIfAbsent(name, value);
        return result != null ? result : value;
    }
}
