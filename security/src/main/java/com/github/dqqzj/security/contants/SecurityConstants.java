package com.github.dqqzj.security.contants;


/**
 * @author qinzhongjian
 * @date created in 2018/7/19 18:07
 * @since 1.0.0
 */
public class SecurityConstants {

    /**
     * 默认的处理验证码的url前缀
     */
    public static final String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/defaultKaptcha";

    /**
     * 当请求需要身份验证时跳转URL
     */
    public static final String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";

    /**
     * 默认的用户名密码登录请求处理url
     */
    public static final String DEFAULT_SIGN_IN_PROCESSING_URL_FORM = "/authentication/login";

    /**
     * 默认的手机验证码登录请求处理url
     */
    public static final String DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE = "/authentication/mobile/login";

    /**
     * 默认的用户注册请求处理url
     */
    public static final String DEFAULT_REGISTER_URL = "/mobile/register";


    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "captcha_code";

    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_SMS = "sms";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

    /**
     * session失效默认的跳转地址
     */
    public static final String DEFAULT_URL = "/";

}
