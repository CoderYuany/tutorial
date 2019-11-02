package com.github.dqqzj.refresh.scope;

import org.springframework.context.ApplicationEvent;

public class RefreshedEvent extends ApplicationEvent {
    public static final String DEFAULT_NAME = "refreshAll";
    private String name;

    public RefreshedEvent() {
        this(DEFAULT_NAME);
    }

    public RefreshedEvent(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
