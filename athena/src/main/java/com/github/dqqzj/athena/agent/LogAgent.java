package com.github.dqqzj.athena.agent;

import com.alibaba.fastjson.JSONObject;
import com.github.dqqzj.athena.transfer.MethodDesc;
import com.github.dqqzj.athena.transfer.Transformer;
import com.github.dqqzj.athena.utils.AgentUtils;
import org.springframework.util.CollectionUtils;

import java.lang.instrument.Instrumentation;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qinzhongjian
 * @date created in 2019/12/23 23:52
 * @description Invoke as follows:
 * <pre>java -javaagent:target/athena-0.0.1-SNAPSHOT.jar={opts} application.jar<pre/>
 *
 * Example:
 *  一、如果你是采用方法级别的字节码增强，请参照如下方式：
 *   opts={\"logForParams\":true,\"logForResult\":false,\"methods\":[{\"className\":\"com.github.dqqzj.athena.test.TestController\",\"methodArgs\":[\"java.lang.Integer\",\"double\"],\"methodName\":\"hello\"},{\"className\":\"com.github.dqqzj.athena.test.TestService\",\"methodArgs\":[\"java.lang.Integer\",\"double\"],\"methodName\":\"hello\"}]}
 *  二、如果你是采用类级别的字节码增强，请参照如下方式：TODO
 *  三、如果你是采用包级别的字节码增强，请参照如下方式：TODO
 * opts就是将AgentArgs对象进行转化为json字符串作为参数用于传递给jvm的agentArgs，
 * 详细参数细节参考如下：
 * @see AgentArgs
 * 如果你是
 * @since JDK 1.8.0_212-b10
 */
public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println(agentArgs);
        Map<String, List<MethodDesc>> instructionMap = new LinkedHashMap<>();
        try {
            AgentArgs args = JSONObject.parseObject(agentArgs, AgentArgs.class);
            if (args.isBytecodeEnhanced()) {
                //获取进行字节码增强的候选方法
                List<MethodDesc> methods = args.getMethods();
                if (!CollectionUtils.isEmpty(methods)) {
                     instructionMap.putAll(methods
                             .stream()
                             .filter(methodDesc ->
                                     !CollectionUtils.isEmpty(methodDesc.getMethodArgs())
                             )
                             .collect(Collectors.groupingBy(MethodDesc::getClassName)));
                }
                List<Class<?>> classes = args.getClasses();
                if (!CollectionUtils.isEmpty(classes)) {
                    instructionMap.putAll(classes.stream().map(Class::getName).collect(Collectors.toMap(s -> s, AgentUtils::parseMethodDesc)));
                }
                inst.addTransformer(new Transformer(args, instructionMap));
            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
    }
}
