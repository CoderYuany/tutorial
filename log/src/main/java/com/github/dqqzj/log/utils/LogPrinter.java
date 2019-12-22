package com.github.dqqzj.log.utils;

import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 00:11
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class LogPrinter {
    private static final String LOG_WARN_PREFIX = "logger.warn(";
    private static final String LOG_ERROR_PREFIX = "logger.error(";
    private static final String TIME_STAMP_STRING_CODE = "java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss.SSS\"))";
    private static final String THREAD_NAME_STRING_CODE = "java.lang.Thread.currentThread().getName()";
    private static final String LOG_PREFIX_STRING_CODE = TIME_STAMP_STRING_CODE + " + \" [\" + " + THREAD_NAME_STRING_CODE + " + \"] LogAgent \"";

    public String getCodeLogMethodArgs(CtMethod m) throws NotFoundException {
        StringBuilder code = new StringBuilder();
        // Prefix
        code.append(LOG_WARN_PREFIX)
                .append(LOG_PREFIX_STRING_CODE)
                .append(" + ");
        // ClassName::methodName
        code.append("\"")
                .append(m.getDeclaringClass().getSimpleName())
                .append("::")
                .append(m.getName())
                .append("(\"");
        // Arguments
        code.append(" + ");
        int argCount = m.getParameterTypes().length;
        for (int i = 1; i <= argCount; i++) {
            if (i != 1) {
                code.append(" + \", \" + ");
            }
            code.append("$").append(i);
        }
        // Right Bracket
        code.append(" + \")\"");
        code.append(");");
        return code.toString();
    }

    public String getCodeLogMethodResult(CtMethod m, String result) {
        StringBuilder code = new StringBuilder();

        // Prefix
        code.append(LOG_WARN_PREFIX)
                .append(LOG_PREFIX_STRING_CODE)
                .append(" + ");

        // ClassName::methodName
        code.append("\"")
                .append(m.getDeclaringClass().getSimpleName())
                .append("::")
                .append(m.getName());
        // Result
        code.append(" + \"  ==> \"");
        code.append(" + ");
        code.append(result);
        code.append(");");

        return code.toString();
    }

    public String getCodeLogMethodArgsAndResult(CtMethod m, String result) throws NotFoundException {
        StringBuilder code = new StringBuilder();
        // Prefix
        code.append(LOG_ERROR_PREFIX)
                .append(LOG_PREFIX_STRING_CODE)
                .append(" + ");

        // ClassName::methodName
        code.append("\"")
                .append(m.getDeclaringClass().getSimpleName())
                .append("::")
                .append(m.getName())
                .append("(\"");

        // Arguments
        code.append(" + ");
        int argCount = m.getParameterTypes().length;
        for (int i = 1; i <= argCount; i++) {
            if (i != 1) {
                code.append(" + \", \" + ");
            }
            code.append("$").append(i);
        }
        // Right Bracket
        code.append(" + \")\"");
        // Result
        code.append(" + \"  ==> \"");
        code.append(" + ");
        code.append(result);
        code.append(");");
        return code.toString();
    }
}
