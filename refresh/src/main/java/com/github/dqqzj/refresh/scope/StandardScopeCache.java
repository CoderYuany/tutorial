package com.github.dqqzj.refresh.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StandardScopeCache<T> implements ScopeCache<T> {
    private final ConcurrentMap<String, T> cache = new ConcurrentHashMap();

    public StandardScopeCache() {
    }

    public T remove(String name) {
        return this.cache.remove(name);
    }

    public Collection<T> clear() {
        Collection<T> values = new ArrayList(this.cache.values());
        this.cache.clear();
        return values;
    }

    public T get(String name) {
        return this.cache.get(name);
    }

    public T put(String name, T value) {
        T result = this.cache.putIfAbsent(name, value);
        return result != null ? result : value;
    }

}
