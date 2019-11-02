//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.config")
public class PropertySourceBootstrapProperties {
    private boolean overrideSystemProperties = true;
    private boolean allowOverride = true;
    private boolean overrideNone = false;

    public PropertySourceBootstrapProperties() {
    }

    public boolean isOverrideNone() {
        return this.overrideNone;
    }

    public void setOverrideNone(boolean overrideNone) {
        this.overrideNone = overrideNone;
    }

    public boolean isOverrideSystemProperties() {
        return this.overrideSystemProperties;
    }

    public void setOverrideSystemProperties(boolean overrideSystemProperties) {
        this.overrideSystemProperties = overrideSystemProperties;
    }

    public boolean isAllowOverride() {
        return this.allowOverride;
    }

    public void setAllowOverride(boolean allowOverride) {
        this.allowOverride = allowOverride;
    }
}
