package com.github.dqqzj.log.utils;

import com.github.dqqzj.log.transfer.MethodDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 00:15
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class AgentUtils {
    private static Logger logger = LoggerFactory.getLogger(AgentUtils.class);
    private static final String SPLIT_EQUALS = "=";
    private static final String SPLIT_SEMICOLON = ";";
    private static final Pattern PATTERN = Pattern.compile("([^:]+)#([^(]+)\\(([^)]+)\\)");

    public static Optional<MethodDesc> parseMethodDesc(String desc) {
        Matcher matcher = PATTERN.matcher(desc);
        if (matcher.matches()) {
            String className = matcher.group(1);
            String methodName = matcher.group(2);
            String argString = matcher.group(3);
            String[] methodArgs = Stream.of(argString.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
            return Optional.of(new MethodDesc(className, methodName, methodArgs));
        }
        return Optional.empty();
    }
    public static Map<String, String> parseAgentArgs(String agentArgs) {
        Map<String, String> result = new LinkedHashMap<>();
        if (agentArgs == null) {
            return result;
        }
        for (String arg : agentArgs.split(SPLIT_SEMICOLON)) {
            String[] argParts = arg.split(SPLIT_EQUALS);
            if (argParts.length == 2) {
                result.put(argParts[0].trim(), argParts[1].trim());
            } else {
                System.out.println("parseAgentArgs ignoring.....");
                logger.warn("parseAgentArgs ignoring agentArg");
            }
        }
        return result;
    }
}
