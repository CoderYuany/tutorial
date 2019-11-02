//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.scope;

import java.util.Collection;

public interface ScopeCache<T> {
    T remove(String name);

    Collection<T> clear();

    T get(String name);

    T put(String name, T value);
}
