package com.github.dqqzj.springboot;

import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author qinzhongjian
 * @date created in 2019-07-31 21:22
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Component
public class MyEventListener extends AbstractEventListener {
    @EventListener({ContextRefreshedEvent.class,ContextClosedEvent.class})
    private void onContextClosedEvent(ApplicationContextEvent event) {
        System.out.println("MyEventListener :" + event.getClass().getSimpleName());
        //System.out.println("MyEventListener :" + closedEvent.getClass().getSimpleName());
    }
}
