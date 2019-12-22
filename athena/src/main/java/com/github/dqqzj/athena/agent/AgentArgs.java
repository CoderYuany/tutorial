package com.github.dqqzj.athena.agent;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 21:11
 * @description agent的构造参数，便于传递给LogAgent使用
 * @see LogAgent
 * @since JDK 1.8.0_212-b10
 */
@Data
@ToString
public class AgentArgs implements Serializable {

    private static final long serialVersionUID = -6080694199619999506L;

    /**
     * 是否输出入参
     */
    boolean logForParams = false;

    /**
     * 是否输出出参
     */
    boolean logForResult = false;

    /**
     * 指定方法进行字节码增强
     */
    List<String> methods;

    /**
     * 指定类中的所有方法进行字节码增强
     */
    List<Class<?>> classes;

    /**
     * 指定包下所有类的所有方法进行字节码增强
     */
    List<String> packages;

    private boolean checkMethods() {
        return CollectionUtils.isEmpty(this.methods) ? false : true;
    }
    private boolean checkClasses() {
        return CollectionUtils.isEmpty(this.classes) ? false : true;
    }
    private boolean checkPackages() {
        return CollectionUtils.isEmpty(this.packages) ? false : true;
    }
}
