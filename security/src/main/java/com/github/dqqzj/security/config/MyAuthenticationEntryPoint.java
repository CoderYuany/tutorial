package com.github.dqqzj.security.config;

import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.model.response.RestEntity;
import com.github.dqqzj.security.utils.Jacksons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 验证请求url与配置得url是否匹配得工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    /**
     * 权限认证url 集合，自定义实现，比如从mysql读取或者java编码
     */
    private List<String> authenticationUrls;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String requestUrl = request.getRequestURI();
        log.info("【MyAuthenticationEntryPoint】-->> Spring security错误端点处理，ajax请求路径为："+requestUrl);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        RestEntity restResponseEntity;
        if (isLoginInvalid(requestUrl)){
            restResponseEntity = new RestEntity(RestStatusEnum.ACCOUNT_LOGIN_INVALID,authException);
        }else {
            restResponseEntity = new RestEntity(RestStatusEnum.ACCOUNT_NOT_LOGGED_IN,authException);
        }
        response.getWriter().write(Jacksons.parse(restResponseEntity));
    }
    private boolean isLoginInvalid(String requestUrl) {
        for (String url : authenticationUrls) {
            if (pathMatcher.match(url, requestUrl)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
