//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.scope;

import java.util.Collection;

public interface ScopeCache {
    Object remove(String name);

    Collection<Object> clear();

    Object get(String name);

    Object put(String name, Object value);
}
