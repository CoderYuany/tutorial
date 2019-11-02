package com.github.dqqzj.refresh;

import com.github.dqqzj.refresh.environment.EnvironmentChangeEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RefreshApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefreshApplication.class, args);
    }
    @EventListener
    public void envListener(EnvironmentChangeEvent event) {
        System.out.println("keys: "+ event.getKeys());
        System.out.println("conf change: " + event);
    }
}
