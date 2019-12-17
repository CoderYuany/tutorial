package com.github.dqqzj.athena.config;

import com.github.dqqzj.athena.core.InvokeMethod;
import com.github.dqqzj.athena.flow.JsrValidator;
import com.github.dqqzj.athena.flow.LogPrinter;
import com.github.dqqzj.athena.utils.TraceUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author qinzhongjian
 * @date created in 2019/12/17 22:43
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class LogConfig {

    public static Supplier<String> traceUtil = TraceUtils::getTraceId;

    public static String TRACE_KEY = "traceId";

    public static Consumer<InvokeMethod> log4InputParams = LogPrinter::printLog4InputParams;
    public static Consumer<InvokeMethod> log4ReturnValues = LogPrinter::printLog4ReturnValues;
    public static Consumer<InvokeMethod> log4Exceptions = LogPrinter::printLog4Exceptions;

    public static class ValidatorConfig {
        public static Consumer<InvokeMethod> jsrValidator = new JsrValidator()::validate;
    }
}
