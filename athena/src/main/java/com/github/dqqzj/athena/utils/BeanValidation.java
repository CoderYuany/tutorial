package com.github.dqqzj.athena.utils;

import java.util.Set;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:45
 * @description 验证，执行Bean Validation标准
 * @since JDK 1.8.0_212-b10
 */
public class BeanValidation {

    private static final Validator VALIDATOR = JsrValidator.DEFAULT_VALIDATOR;

    /**
     * 检查，并抛出异常，主要是为了校验，执行Bean Validation标准，其他简单校验可以使用
     *
     * @param waitForCheck 等待被检查的对象
     * @throws BizRuntimeException 运行时异常，包括CODE & MSG
     * @see Assert
     */
    public static void checkAndThrow(Object waitForCheck) throws BizRuntimeException {
        Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(waitForCheck);
        if (!CollectionUtils.isEmpty(violations)) {
            String violationMsg = JsrValidator.doParseConstraintViolation(violations);
            throw new BizRuntimeException(ResultCodeEnum.PARAMETER_ERROR, violationMsg);
        }
    }

    /**
     * 检查，并抛出组装错误信息
     *
     * @param waitForCheck 等待被检查的对象
     * @return 错误信息
     */
    public static String check(Object waitForCheck) {
        String violationMsg = null;
        Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(waitForCheck);
        if (!CollectionUtils.isEmpty(violations)) {
            violationMsg = JsrValidator.doParseConstraintViolation(violations);
        }
        return violationMsg;
    }
}
