package com.github.dqqzj.security.validate;

import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.exception.RestStatusException;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.github.dqqzj.security.contants.SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 15:20
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    /**
     * 登录失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler failureHandler;

    /**
     * 创建一个Set 集合 存放 需要验证码的 urls
     */
    private Set<String> urls = new HashSet<>();

    /**
     * spring的一个工具类：用来判断 两字符串 是否匹配
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getMethod().equals(HttpMethod.GET.name())) {
            throw new RestStatusException(RestStatusEnum.REQUEST_METHOD_NOT_SUPPORTED.getCode().toString());
        }
        boolean action = false;
        for (String url : urls) {
            //如果请求的url 和 配置中的url 相匹配
            if (pathMatcher.match(url, request.getRequestURI())) {
                action = true;
            }
        }

        //拦截请求
        if (action) {
            log.info("ValidateCodeFilter#doFilterInternal 获取验证码的请求路径: [{}]", request.getRequestURI());
            //如果是登录请求
            validate(request);
        }
        //不做任何处理，调用后面的 过滤器
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        //从session中取出 验证码
        String code = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString();
        //从request 请求中 取出 验证码
        String captcha_code = ServletRequestUtils.getStringParameter(request, DEFAULT_PARAMETER_NAME_CODE_IMAGE);

        if (StringUtils.isEmpty(captcha_code)) {
            log.warn("验证码不能为空");
            throw new RestStatusException(RestStatusEnum.KAPTCHA_NOT_BLANK.getCode().toString());
        }

        if (!Objects.equals(captcha_code, code)) {
            log.warn("验证码不匹配 请求参数code:[{}], session中的验证码:[{}]", captcha_code, code);
            throw new RestStatusException(RestStatusEnum.KAPTCHA_ERROR.getCode().toString());
        }
        //把对应 的 session信息  删掉
        request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
    }

    /**
     * 失败 过滤器 getter and setter 方法
     */
    public AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
}

