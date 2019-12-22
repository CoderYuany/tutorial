package com.github.dqqzj.athena;

import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.agent.AgentArgs;
import com.github.dqqzj.athena.transfer.MethodDesc;
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
		List<MethodDesc> list = new ArrayList<>(2);
		String[] methodArgs = new String[] {"java.lang.Integer","double"};
		MethodDesc methodDesc = new MethodDesc("com.github.dqqzj.athena.test.TestController","hello",methodArgs);
		list.add(methodDesc);
		String[] methodArgs1 = new String[] {"java.lang.Integer","double"};
		MethodDesc methodDesc1 = new MethodDesc("com.github.dqqzj.athena.test.TestService","hello",methodArgs1);
		list.add(methodDesc1);
		args.setMethods(list);
		System.out.println(JSON.toJSONString(args));
	}

}
