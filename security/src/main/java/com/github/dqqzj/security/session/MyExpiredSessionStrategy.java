package com.github.dqqzj.security.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;

/**
 * @author qinzhongjian
 * @date created in 2018/7/25 18:05
 * @since 1.0.0
 */
public class MyExpiredSessionStrategy extends MyAbstractSessionStrategy implements SessionInformationExpiredStrategy {
    /**
     * @param invalidSessionUrl
     */
    public MyExpiredSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {

        onSessionInvalid(event.getRequest(), event.getResponse());
    }

}

