package com.github.dqqzj.athena.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019/12/23 22:21
 * @description 方法描述
 * @since JDK 1.8.0_212-b10
 */
@Data
@AllArgsConstructor
@ToString
public class MethodDesc {
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 方法参数
     */
    private List<String> methodArgs;

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getMethodArgs() {
        return methodArgs;
    }

}