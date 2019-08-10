package com.github.dqqzj.security.handle;

import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.model.response.RestEntity;
import com.github.dqqzj.security.utils.Jacksons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author qinzhongjian
 * @date created in 2018/7/25 18:50
 * @since 1.0.0
 */
@Slf4j
@Component
public class MySavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * 登录成功处理器
     * @param request
     * @param response
     * @param authentication
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        log.info("【MySavedRequestAwareAuthenticationSuccessHandler】 onAuthenticationSuccess authentication={}", authentication);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        RestEntity responseEntity = new RestEntity(RestStatusEnum.OK,null);
        response.getWriter().write(Jacksons.parse(responseEntity));
    }
}
