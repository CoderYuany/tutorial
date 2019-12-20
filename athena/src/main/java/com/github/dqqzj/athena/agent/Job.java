package com.github.dqqzj.athena.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import lombok.SneakyThrows;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 10:35
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class Job implements Serializable {

    public int x = 1;
    public String str = "job";

    @SneakyThrows
    public int doAdd(int x, String s, long l, Job j, Main n, double d) {
        String clazz = Thread.currentThread().getStackTrace()[1].getClassName();
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String path = clazz + "@" + method + "#" + System.currentTimeMillis();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path), true));
        String[] args = new String[6];
        args[0] = JSON.toJSONString(x);
        args[1] = JSON.toJSONString(s);
        args[2] = JSON.toJSONString(l);
        args[3] = JSON.toJSONString(j);
        args[4] = JSON.toJSONString(n);
        args[5] = JSON.toJSONString(d);
        out.writeObject(args);
        return n.x + x;
    }
}
