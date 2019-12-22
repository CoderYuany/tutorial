package com.github.dqqzj.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qinzhongjian
 * @date created in 2019/12/22 00:00
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
public class LoggingUtils {

    public static void logger(Class clazz, String msg, Object... args) {
        Logger logger = LoggerFactory.getLogger(LogAgent.class);
        if (clazz != null) {
            logger = LoggerFactory.getLogger(clazz);
        }
        logger.info(msg, args);

    }
}
