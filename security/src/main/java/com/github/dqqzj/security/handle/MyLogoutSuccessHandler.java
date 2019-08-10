package com.github.dqqzj.security.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        log.info("【LogoutSuccessHandler】-->>"+authentication.getName()+"退出登陆成功");

    }
}
