package com.github.dqqzj.log.agent;

import com.github.dqqzj.log.transfer.MethodDesc;
import com.github.dqqzj.log.transfer.Transformer;
import com.github.dqqzj.log.utils.AgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qinzhongjian
 * @date created in 2019/12/21 16:29
 * @description Invoke as follows:
 * <pre>java -javaagent:target/agent-0.1-SNAPSHOT.jar={opts} application.jar<pre/>
 *
 * Example:
 *  java -javaagent:target/agent-0.1-SNAPSHOT.jar=debug=true;methods=java.net.InetAddress::getByName(java.lang.String)|java.net.InetAddress::getByName(java.lang.String, java.net.InetAddress) -jar application.jar
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
 *         Example: "java.net.InetAddress::getByName(java.lang.String, java.net.InetAddress)|java.net.InetAddress::getByName(java.lang.String)"
 *
 *         Note: If not specified networking methods for DNS lookup and opening of TCP/UDP connections will be intercepted.
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
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
        //
        log.info("Register transformer.....");
        log.info("0000000000");
        inst.addTransformer(new Transformer(logForParams, logForResult, instructionMap));
        log.info("premain   end.....");
    }

}
