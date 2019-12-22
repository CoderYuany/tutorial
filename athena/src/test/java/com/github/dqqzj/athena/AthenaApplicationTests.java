package com.github.dqqzj.athena;

import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.agent.AgentArgs;
import com.github.dqqzj.athena.test.TestController;
import com.github.dqqzj.athena.test.TestService;
import com.github.dqqzj.athena.transfer.MethodDesc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
class AthenaApplicationTests {

	@Test
	void testMethods() {
		AgentArgs args = new AgentArgs();
		args.setLogForParams(true);
		List<MethodDesc> list = new ArrayList<>(2);
		List<String> methodArgs = new ArrayList<>();
		methodArgs.add("java.lang.Integer");
		methodArgs.add("double");
		MethodDesc methodDesc = new MethodDesc("com.github.dqqzj.athena.test.TestController","hello",methodArgs);
		list.add(methodDesc);
		List<String> methodArgs1 = new ArrayList<>();
		methodArgs1.add("java.lang.Integer");
		methodArgs1.add("double");
		MethodDesc methodDesc1 = new MethodDesc("com.github.dqqzj.athena.test.TestService","hello",methodArgs1);
		list.add(methodDesc1);
		args.setMethods(list);
		System.out.println(JSON.toJSONString(args));
	}
	@Test
	void testClasses() {
		AgentArgs args = new AgentArgs();
		args.setLogForParams(true);
		List<Class<?>> classes = new ArrayList<>();
		classes.add(TestController.class);
		classes.add(TestService.class);
		args.setClasses(classes);
		System.out.println(JSON.toJSONString(args));
	}
}
