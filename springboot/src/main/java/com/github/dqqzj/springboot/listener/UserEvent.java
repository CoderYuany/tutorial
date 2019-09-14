package com.github.dqqzj.springboot.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @author qinzhongjian
 * @date created in 2019-07-31 22:08
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class UserEvent<User> extends ApplicationEvent {
    public UserEvent(User user) {
        super(user);
    }
}
