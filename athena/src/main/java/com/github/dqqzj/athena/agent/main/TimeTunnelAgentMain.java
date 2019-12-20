package com.github.dqqzj.athena.agent.main;

import java.lang.instrument.Instrumentation;

import com.github.dqqzj.athena.agent.transformer.TimeTunnelTransformer;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 10:54
 * @description 采集一个方法出参、入参的结果 --先不考虑子调用过程
 * @since JDK1.8.0_211-b12
 */
public class TimeTunnelAgentMain {

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        String methodName = agentArgs.substring(agentArgs.lastIndexOf(".") + 1, agentArgs.length());
        String className = agentArgs.substring(0, agentArgs.lastIndexOf("."));
        inst.addTransformer(new TimeTunnelTransformer(className, methodName), true);
        inst.retransformClasses(getClassByRedefinePath(className));
    }

    public static Class<?> getClassByRedefinePath(String targetClassName) throws Exception {
        return Class.forName(targetClassName);
    }
}
