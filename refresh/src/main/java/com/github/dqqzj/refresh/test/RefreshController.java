package com.github.dqqzj.refresh.test;

import com.github.dqqzj.refresh.ContextRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinzhongjian
 * @date created in 2019/11/1 22:27
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@RestController
public class RefreshController {
    @Autowired
    private ContextRefresher contextRefresher;

    @Autowired
    private Refresh refresh;

    @GetMapping(path = "/refresh")
    public String refresh() {
        new Thread(() -> contextRefresher.refresh()).start();
        return refresh.toString();
    }

}
