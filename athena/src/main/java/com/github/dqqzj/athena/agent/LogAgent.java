package com.github.dqqzj.athena.agent;

import com.alibaba.fastjson.JSONObject;
import com.github.dqqzj.athena.transfer.MethodDesc;
import com.github.dqqzj.athena.transfer.Transformer;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 17:18
 * @description Invoke as follows:
 * <pre>java -javaagent:target/athena-0.0.1-SNAPSHOT.jar={opts} application.jar<pre/>
 *
 * Example:
 *  java -javaagent:/Users/qinzhongjian/IdeaProjects/tutorial/athena/target/athena-0.0.1-SNAPSHOT.jar={"logForParams":true,"logForResult":false,"methods":["com.github.dqqzj.athena.test.TestController#hello(java.lang.Integer,double)","com.github.dqqzj.athena.test.TestService#hello(java.lang.Integer,double)"]}
 * TODO 后期考虑支持class级别和扫描包级别等其他手段，以做到更加灵活配置
 * @see AgentArgs 将其进行转化为json字符串作为参数用于传递给jvm使用
 * @since JDK 1.8.0_212-b10
 */
public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(agentArgs);
        try {
            AgentArgs args = JSONObject.parseObject(agentArgs, AgentArgs.class);
            if (args.isBytecodeEnhanced()) {
                //获取进行字节码增强的候选方法
                Map<String, List<MethodDesc>> instructionMap = args.getMethods().stream()
                        .collect(Collectors.groupingBy(MethodDesc::getClassName));
                inst.addTransformer(new Transformer(args, instructionMap));
            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
    }
}
