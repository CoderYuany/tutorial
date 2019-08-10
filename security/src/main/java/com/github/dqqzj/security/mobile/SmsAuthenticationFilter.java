package com.github.dqqzj.security.mobile;

import com.github.dqqzj.security.config.GoogleGuavaCache;
import com.github.dqqzj.security.contants.SecurityConstants;
import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.exception.RestStatusException;
import com.github.dqqzj.security.model.response.RestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * 短信登录过滤器
 * Created on 2018/1/10.
 * @author qzj
 * @since 1.0
 */
@Slf4j
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * request中必须含有mobile参数
     */
    private String mobileParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;

    protected SmsAuthenticationFilter() {
        /**
         * 处理的手机验证码登录请求处理url
         */
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE , "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //判断是不是post请求
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            RestEntity restResponseEntity = new RestEntity(RestStatusEnum.REQUEST_METHOD_NOT_SUPPORTED,request.getRequestURL());
            throw new AuthenticationServiceException(restResponseEntity.toString());
        }
        //从请求中获取手机号码
        String mobile = obtainMobile(request);
        String code = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS);
        //进行验证码对比校验
        try {
            String sms = (String) GoogleGuavaCache.CACHE.get(mobile, () -> null);
            if (Objects.equals(code,sms)){
                //创建SmsCodeAuthenticationToken(未认证)
                SmsAuthenticationToken authRequest = new SmsAuthenticationToken(mobile);
                //设置用户信息
                setDetails(request, authRequest);
                //返回Authentication实例
                return this.getAuthenticationManager().authenticate(authRequest);
            }else {
                log.error("【SmsAuthenticationFilter】-->> 阿里云短信验证码校验失败");
                throw new RestStatusException(RestStatusEnum.SMS_CODE_CHECK_ERROR.toString());
            }
        } catch (ExecutionException e) {
            log.error("【SmsAuthenticationFilter】-->> 从【GoogleGuavaCache】缓存中获取验证码失败",e);
            throw new RestStatusException(RestStatusEnum.SMS_GOOGLE_CACHE_ERROR.toString());
        } finally {
            /**
             * 只能使用一次验证码，错误过后必须重新获取
             */
            GoogleGuavaCache.CACHE.invalidate(mobile);
        }
    }

    /**
     * 获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
