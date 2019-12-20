package com.github.dqqzj.athena.agent.collect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.github.dqqzj.athena.agent.Advice;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 11:08
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class TimeTunnelCollect {

    public static void collect(Advice advice, String path) throws IOException{
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            out.writeObject(advice);
        }catch (IOException e){
            throw e;
        }
    }
}
