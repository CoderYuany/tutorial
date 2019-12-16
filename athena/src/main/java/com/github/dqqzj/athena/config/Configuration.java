package com.github.dqqzj.athena.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author qinzhongjian
 * @date created in 2019/12/16 23:27
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class Configuration {
    /**
     * trace Util，可更换
     */
    public static Supplier<String> traceUtil = TraceUtil::getTraceId;

    public static String TRACE_KEY = "traceId";


    public static class LogConfig {
        public static Boolean logForInputParams;
        public static Boolean logForResult;
        public static boolean logForAll = false;

        public static Consumer<InvokeMethod> log4InputParams = new LogPrinter()::printLog4InputParams;
        public static Consumer<InvokeMethod> log4ReturnValues = new LogPrinter()::printLog4ReturnValues;
        public static Consumer<InvokeMethod> log4Exceptions = new LogPrinter()::printLog4Exceptions;
    }

    public static class ValidatorConfig {
        public static Consumer<InvokeMethod> jsrValidator = new JsrValidator()::validate;
    }

}
