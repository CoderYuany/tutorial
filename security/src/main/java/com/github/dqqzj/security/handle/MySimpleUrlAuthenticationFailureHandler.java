package com.github.dqqzj.security.handle;

import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.model.response.RestEntity;
import com.github.dqqzj.security.utils.Jacksons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: 自定义登录失败处理
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Component
public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("【MySimpleUrlAuthenticationFailureHandler】自定义认证失败");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        RestEntity restResponseEntity;
        if (exception instanceof InternalAuthenticationServiceException){
            restResponseEntity = new RestEntity(RestStatusEnum.ACCOUNT_NOT_EXIST,exception);
        }else {
            restResponseEntity = new RestEntity(RestStatusEnum.INVALID_CREDENTIAL,exception);
        }
        response.getWriter().write(Jacksons.parse(restResponseEntity));
    }
}
