package com.github.dqqzj.athena.test;

import lombok.Data;

import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019/12/17 23:41
 * @description TODO
 * @since JDK 1.8.0_212-b10
 */
@Data
public class Test {
    private String name;
    private int age;
    private Test test;
}
