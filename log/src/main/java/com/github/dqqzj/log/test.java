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
        String desc = "java.net.InetAddress::getByName(java.lang.String)";
        String desc1 = "com.github.dqqzj.log.TestController::hello(java.lang.Void)";
        Optional<MethodDesc> optional = AgentUtils.parseMethodDesc(desc1);
        System.out.println(optional.get().toString());
    }
}
