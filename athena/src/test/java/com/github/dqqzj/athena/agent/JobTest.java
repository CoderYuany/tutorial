package com.github.dqqzj.athena.agent;

import org.junit.jupiter.api.Test;

class JobTest {

    @Test
    void doAdd() throws InterruptedException {
        Job demo = new Job();
        Main m = new Main();
        while (true) {
            //demo.doAdd(0,1);
            System.out.println(demo.doAdd(1,"abc",11L,demo,m,0.11d));
           // System.out.println(demo.doDelete(2,1));
            System.out.println("-------------------");
            Thread.sleep(8000);
        }
    }
}