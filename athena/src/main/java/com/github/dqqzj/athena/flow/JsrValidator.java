package com.github.dqqzj.athena.flow;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import com.github.dqqzj.athena.core.InvokeMethod;
import com.github.dqqzj.athena.core.enums.ResultCodeEnum;
import com.github.dqqzj.athena.core.exception.SimpleBizException;
import com.github.dqqzj.athena.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.validator.internal.engine.ValidatorContextImpl;
import org.springframework.util.CollectionUtils;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:41
 * @description jsr380校验
 * @since JDK 1.8.0_212-b10
 */
public class JsrValidator {

    public static final ExecutableValidator VALIDATOR;
    public static final Validator DEFAULT_VALIDATOR;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        ValidatorContextImpl validatorContext = (ValidatorContextImpl) validatorFactory.usingContext();
        validatorContext.allowOverridingMethodAlterParameterConstraint(true);
        DEFAULT_VALIDATOR = validatorContext.getValidator();
        VALIDATOR = DEFAULT_VALIDATOR.forExecutables();
    }


    /**
     * 1. 从局部到整体,校验范围配置：参数、方法、类、全局
     * 2. 校验primate类型和复杂类型
     * 3. 有问题抛出去
     *
     * @param pjp 切入点
     */
    public static void validate(ProceedingJoinPoint pjp) {
        Object target = pjp.getTarget();
        Object[] args = pjp.getArgs();
        Method realMethod = ReflectionUtils.getClassMethod(pjp);
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validateParameters(target, realMethod, args);
        processConstraintViolations(constraintViolations, realMethod);
    }

    public void validate(InvokeMethod invokeMethod) {
        Object target = invokeMethod.getTarget();
        Object[] args = invokeMethod.getArgs();
        Method realMethod = invokeMethod.getMethod();
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validateParameters(target, realMethod, args);
        processConstraintViolations(constraintViolations, realMethod);
    }

    private static void processConstraintViolations(Set<ConstraintViolation<Object>> constraintViolations, Method realMethod) {
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            String errorMessage = parseConstraintViolation(constraintViolations, realMethod);
            // 这里可以更通用些, 但是统一错误码也没什么坏处
            throw new SimpleBizException(ResultCodeEnum.ILLEGAL_ARGUMENT, errorMessage);
        }
    }

    private static String parseConstraintViolation(Set<ConstraintViolation<Object>> constraintViolations, Method realMethod) {
        String methodFullName = RefUtil.getMethodFullName(realMethod);
        return methodFullName + " $$ " + "<< " + doParseConstraintViolation(constraintViolations) + " >>";
    }

    public static String doParseConstraintViolation(Set<ConstraintViolation<Object>> constraintViolations) {
        List<String> kvs = new ArrayList<>();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            String innerStr = String.valueOf(constraintViolation.getPropertyPath()) +
                    " " + constraintViolation.getMessage();
            kvs.add(innerStr);
        }
        return StringUtils.join(kvs, ", ");
    }
}
