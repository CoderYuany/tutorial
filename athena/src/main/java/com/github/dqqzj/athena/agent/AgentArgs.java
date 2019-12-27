package com.github.dqqzj.athena.agent;

import com.github.dqqzj.athena.transfer.MethodDesc;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
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
    boolean logForParams;

    /**
     * 是否输出出参
     */
    boolean logForResult;

    /**
     * 指定方法进行字节码增强
     */
    List<MethodDesc> methods;

    /**
     * 指定类中的所有方法进行字节码增强
     */
    List<Class<?>> classes;

    /**
     * 指定包下所有类的所有方法进行字节码增强
     */
    List<String> packages;

    /**
     * 指定注解的类进行字节码增强（主要考虑类似阿里的hsf调用进行入参和出参记录）
     */
    List<Annotation> annotations;

    private boolean checkMethods() {
        return CollectionUtils.isEmpty(this.methods) ? false : true;
    }
    private boolean checkClasses() {
        return CollectionUtils.isEmpty(this.classes) ? false : true;
    }
    private boolean checkPackages() {
        return CollectionUtils.isEmpty(this.packages) ? false : true;
    }
    private boolean checkAnnotations() {
        return CollectionUtils.isEmpty(this.annotations) ? false : true;
    }
    protected boolean isBytecodeEnhanced() {
        if (checkMethods() || checkClasses() || checkPackages() || checkAnnotations()) {
            return true;
        }
        return false;
    }
}
