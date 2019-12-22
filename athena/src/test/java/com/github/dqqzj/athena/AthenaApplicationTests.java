package com.github.dqqzj.athena;

import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.agent.AgentArgs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
class AthenaApplicationTests {

	@Test
	void contextLoads() {
		AgentArgs args = new AgentArgs();
		args.setLogForParams(true);
		List<String> list = new ArrayList<>(2);
		list.add("com.github.dqqzj.athena.test.TestController#hello(java.lang.Integer,double)");
		list.add("com.github.dqqzj.athena.test.TestService#hello(java.lang.Integer,double)");
		args.setMethods(list);
		System.out.println(JSON.toJSONString(args));
	}

}
