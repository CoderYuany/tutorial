package com.github.dqqzj.springboot;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

/**
 * @author qinzhongjian
 * @date created in 2019-07-31 21:21
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public abstract class AbstractEventListener {
    @EventListener(ContextRefreshedEvent.class)
     void onContextRefreshedEvent(ContextRefreshedEvent event) {
        System.out.println("AbstractEventListener :" + event.getClass().getSimpleName());
    }
}
