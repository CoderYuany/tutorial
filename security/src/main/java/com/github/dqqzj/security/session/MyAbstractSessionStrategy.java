package com.github.dqqzj.security.session;

import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.model.response.RestEntity;
import com.github.dqqzj.security.utils.Jacksons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qinzhongjian
 * @date created in 2018/7/25 18:08
 * @since 1.0.0
 */
@Slf4j
public class MyAbstractSessionStrategy {
    /**
     * 过期跳转的url
     */
    private String destinationUrl;

    /**
     * 重定向策略
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 验证请求url与配置得url是否匹配得工具类
     * 可能在某种情况下不必要跳转到门户首页，可进行自定义匹配路径
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public MyAbstractSessionStrategy(String invalidSessionUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
        Assert.isTrue(StringUtils.endsWithIgnoreCase(invalidSessionUrl, ".html"), "url must end with '.html'");
        this.destinationUrl = invalidSessionUrl;
    }
    protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("【MyAbstractSessionStrategy】-->> starting");
        request.getSession();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        if (isAjaxRequest(request)){
            RestEntity responseEntity = new RestEntity(RestStatusEnum.SESSION_INVALID, null);
            response.getWriter().write(Jacksons.parse(responseEntity));
        }else {
            redirectStrategy.sendRedirect(request, response, destinationUrl);
        }

    }
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }

}
