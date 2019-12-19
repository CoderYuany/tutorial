package com.github.dqqzj.athena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy=true)
public class AthenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AthenaApplication.class, args);
	}

}
