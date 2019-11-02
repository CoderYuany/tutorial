//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.autoconfigure;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

public interface PropertySourceLocator {
    PropertySource<?> locate(Environment environment);
}
