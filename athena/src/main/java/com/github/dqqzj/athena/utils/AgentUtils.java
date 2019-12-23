package com.github.dqqzj.athena.utils;

import com.alibaba.fastjson.JSON;
import com.github.dqqzj.athena.transfer.MethodDesc;
import javassist.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 00:15
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
public class AgentUtils {

    public static List<MethodDesc> parseMethodDesc(String className) {
        /**
         * @author qinzhongjian
         * @date 2019/12/23 23:15
         * @param className
         * @return java.util.List<com.github.dqqzj.athena.transfer.MethodDesc>
         * @description 对类级别进行方法描述处理，目前无参方法也进行了处理，尚不知有何问题，后续思考如何处理
         */
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = null;
        try {
            ctClass = pool.get(className);
        } catch (NotFoundException e) {
            log.error("AgentUtils parseMethodDesc pool.get error. className:{}",className,e);
        }
        CtMethod[] ctMethods = ctClass.getDeclaredMethods();
        List<MethodDesc> methodDescList = new ArrayList<>();
        for (int i=0; i<ctMethods.length; i++) {
            CtMethod ctMethod = ctMethods[i];
            CtClass[] parameterTypes = new CtClass[0];
            try {
                parameterTypes = ctMethod.getParameterTypes();
            } catch (NotFoundException e) {
                log.error("AgentUtils parseMethodDesc ctMethod.getParameterTypes error. className:{}",className,e);
            }
            List<String> methodArgs = new ArrayList<>(parameterTypes.length);
            for (int j=0; j<parameterTypes.length; j++) {
                CtClass parameterType = parameterTypes[j];
                    methodArgs.add(parameterType.getName());
            }
            MethodDesc methodDesc = new MethodDesc(className,ctMethod.getName(),methodArgs);
            methodDescList.add(methodDesc);
        }
        return methodDescList;
    }

}
