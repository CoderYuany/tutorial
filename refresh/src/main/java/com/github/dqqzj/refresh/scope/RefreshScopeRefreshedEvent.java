//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.scope;

import org.springframework.context.ApplicationEvent;

public class RefreshScopeRefreshedEvent extends ApplicationEvent {
    public static final String DEFAULT_NAME = "__refreshAll__";
    private String name;

    public RefreshScopeRefreshedEvent() {
        this("__refreshAll__");
    }

    public RefreshScopeRefreshedEvent(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
