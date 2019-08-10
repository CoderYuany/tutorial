package com.github.dqqzj.security.service;

import com.github.dqqzj.security.bean.UserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: 注册后自动登陆
 * @since JDK 1.8.0_212-b10
 */
@Component
public class AutoLogin {

    @Autowired
    protected AuthenticationManager authenticationManager;

    public void login(HttpServletRequest request, UserDo userDo){
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(userDo.getUsername(),userDo.getPassword());
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser=authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());}
}
