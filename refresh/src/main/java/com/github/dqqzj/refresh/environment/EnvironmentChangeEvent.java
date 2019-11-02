package com.github.dqqzj.refresh.environment;

import java.util.Set;
import org.springframework.context.ApplicationEvent;

public class EnvironmentChangeEvent extends ApplicationEvent {
    private Set<String> keys;

    public EnvironmentChangeEvent(Set<String> keys) {
        this(keys, keys);
    }

    public EnvironmentChangeEvent(Object context, Set<String> keys) {
        super(context);
        this.keys = keys;
    }

    public Set<String> getKeys() {
        return this.keys;
    }
}
