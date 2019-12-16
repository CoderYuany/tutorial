package com.github.dqqzj.athena.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:45
 * @description 有清晰说明的参数名成解析器
 * @since JDK 1.8.0_212-b10
 */
public class ClearParameterNameProvider implements ParameterNameProvider {

    private static ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private static ParameterNameProvider provider = new DefaultParameterNameProvider();

    @Override
    public List<String> getParameterNames(Constructor<?> constructor) {
        String[] parameterNames = discoverer.getParameterNames(constructor);
        return Optional.ofNullable(parameterNames).map(Arrays::asList).orElse(provider.getParameterNames(constructor));
    }

    @Override
    public List<String> getParameterNames(Method method) {
        String[] parameterNames = discoverer.getParameterNames(method);
        return Optional.ofNullable(parameterNames).map(Arrays::asList).orElse(provider.getParameterNames(method));
    }

//    {
//        List<Path.Node> nodes =
//                (constraintViolations.iterator().next().getPropertyPath().iterator());
//        nodes.get(nodes.size()-1)
//    }
}
