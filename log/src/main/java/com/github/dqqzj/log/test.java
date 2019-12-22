package com.github.dqqzj.log;

import com.github.dqqzj.log.transfer.MethodDesc;
import com.github.dqqzj.log.utils.AgentUtils;

import java.util.Optional;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 14:35
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class test {
    public static void main(String[] args) {
        String x = "logger.warn(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss.SSS\")) + \" [\" + java.lang.Thread.currentThread().getName() + \"] LogAgent \" + \"com.github.dqqzj.log.TestController::hello + \"  ==> \" + $_);";

        System.out.println(x);
    }
}
