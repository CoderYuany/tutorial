package com.github.dqqzj.springboot;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;

/**
 * @author qinzhongjian
 * @date created in 2019-07-31 22:05
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class UserListener implements ApplicationListener<UserEvent<User>> {
    @Override
    public void onApplicationEvent(UserEvent<User> event) {
        System.out.println("onApplicationEvent : " + event.getSource());
    }
    @EventListener
    public void on(UserEvent<User> event) {
        System.out.println("on  onApplicationEvent : " + event.getSource());
    }
}
