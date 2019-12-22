package com.github.dqqzj.athena.agent;

import com.github.dqqzj.athena.transfer.MethodDesc;
import com.github.dqqzj.athena.transfer.Transformer;
import com.github.dqqzj.athena.utils.AgentUtils;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 17:18
 * @description Invoke as follows:
 * <pre>java -javaagent:target/athena-0.0.1-SNAPSHOT.jar={opts} application.jar<pre/>
 *
 * Example:
 *  java -javaagent:/Users/qinzhongjian/IdeaProjects/tutorial/athena/target/athena-0.0.1-SNAPSHOT.jar=logForParams=true;logForResult=true;methods=com.github.dqqzj.athena.test.TestController#hello(java.lang.Integer,double)|com.github.dqqzj.athena.test.TestService#hello(java.lang.Integer,double) -jar application.jar
 * TODO 后期考虑支持class级别和扫描包级别等其他手段，以做到更加灵活配置
 *
 * Agent opts (as key1=value1;key2=value2)
 *
 * - logForParams=true
 *         enable logging to standard logForParams
 *
 * - methods=methodDefinitions
 *         definitions of methods to be intercepted separated by "|"
 *
 *         Example: "com.github.dqqzj.athena.test.TestController#hello(java.lang.Integer,double)|com.github.dqqzj.athena.test.TestService#hello(java.lang.Integer,double)"
 *
 * @since JDK 1.8.0_212-b10
 */
public class LogAgent {

    private static final String LOG_FOR_PARAMS = "logForParams";
    private static final String LOG_FOR_RESULT = "logForResult";

    public static void premain(String agentArgs, Instrumentation inst) {
        // Parse args
        Map<String, String> agentArgsMap = AgentUtils.parseAgentArgs(agentArgs);
        //Get logForParams argument
        boolean logForParams = agentArgsMap.get(LOG_FOR_PARAMS) == null ? false : Boolean.valueOf(agentArgsMap.get(LOG_FOR_PARAMS));
        //Get logForResult argument
        boolean logForResult = agentArgsMap.get(LOG_FOR_RESULT) == null ? false : Boolean.valueOf(agentArgsMap.get(LOG_FOR_RESULT));
        // Get methods argument
        Object methodArgs = agentArgsMap.get("methods");
        if (methodArgs == null) {
            return;
        }
        Stream<String> methods = Stream.of(agentArgsMap.get("methods").split("\\|"));
        // Parse method arguments into instruction map
        Map<String, List<MethodDesc>> instructionMap = methods
                .map(String::trim)
                .map(AgentUtils::parseMethodDesc)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(MethodDesc::getClassName));
        inst.addTransformer(new Transformer(logForParams, logForResult, instructionMap));
    }
}
