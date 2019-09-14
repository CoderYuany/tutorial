package com.github.dqqzj.springboot.listener;

/**
 * @author qinzhongjian
 * @date created in 2019-07-31 22:03
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
