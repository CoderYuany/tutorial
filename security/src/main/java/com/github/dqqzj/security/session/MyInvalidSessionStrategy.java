package com.github.dqqzj.security.session;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qinzhongjian
 * @date created in 2018/7/25 18:07
 * @since 1.0.0
 */
public class MyInvalidSessionStrategy extends MyAbstractSessionStrategy implements InvalidSessionStrategy {
    public MyInvalidSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        onSessionInvalid(request, response);
    }
}
