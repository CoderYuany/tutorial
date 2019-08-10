package com.github.dqqzj.security.config;

import com.github.dqqzj.security.contants.SecurityConstants;
import com.github.dqqzj.security.contants.SessionConstants;
import com.github.dqqzj.security.handle.MyLogoutSuccessHandler;
import com.github.dqqzj.security.mobile.SmsAuthenticationSecurityConfigure;
import com.github.dqqzj.security.service.CustomUserDetailsService;
import com.github.dqqzj.security.session.MyExpiredSessionStrategy;
import com.github.dqqzj.security.session.MyInvalidSessionStrategy;
import com.github.dqqzj.security.validate.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:47
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    private InvalidSessionStrategy invalidSessionStrategy = new MyInvalidSessionStrategy(SessionConstants.sessionInvalidUrl);

    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy = new MyExpiredSessionStrategy(SessionConstants.sessionInvalidUrl);

    @Autowired
    private SmsAuthenticationSecurityConfigure smsAuthenticationSecurityConfigure;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    ValidateCodeFilter validateCodeFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/backend/web/**","/backend/v2/**","/manage/v2/**","/css/**","/fonts/**","/img/**","/js/**")
                .permitAll()
                .antMatchers("/backend/**","/keyword/add","/mall/patent/**","/mall/trade/**")
                .authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint)
                //.accessDeniedHandler(myAccessDeniedHandler).accessDeniedPage("/")
                .and()
                .formLogin()
                /**
                 * 如果请求的URL需要认证则跳转的URL
                 */
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                /**
                 * 处理表单中自定义的登录URL
                 */
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .apply(smsAuthenticationSecurityConfigure)
                .and()
                .rememberMe()
                //.userDetailsService(customUserDetailsService)
                //.tokenRepository(persistentTokenRepository())
                .key("secret")
//                .rememberMeCookieName("remember-me")
//                .rememberMeParameter("remember-me")
                //.tokenValiditySeconds(604800)
                .and()
                .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy)
                /**
                 * 最大session并发数量1
                 */
                .maximumSessions(SessionConstants.maximumSessions)
                /**
                 * 之后的登录踢掉之前的登录
                 */
                .maxSessionsPreventsLogin(SessionConstants.maxSessionsPreventsLogin)
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
    /**
     * 记住我功能的token存取器配置
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}