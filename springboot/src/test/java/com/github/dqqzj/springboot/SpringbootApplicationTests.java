package com.github.dqqzj.springboot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringbootApplicationTests {

    @Test
    public void contextLoads() {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();
        List<String> buffer3 = new ArrayList<>();
        Exchanger<List<String>> exchanger = new Exchanger<>();
        Thread producerThread = new Thread(new Producer(buffer1,exchanger));
        Thread consumerThread = new Thread(new Consumer(buffer2,exchanger));
        Thread centerThread = new Thread(new Center(buffer3,exchanger));
        producerThread.start();
        consumerThread.start();
        centerThread.start();
    }


        static class Producer implements Runnable{

            //生产者、消费者交换的数据结构
            private List<String> buffer;

            //步生产者和消费者的交换对象
            private Exchanger<List<String>> exchanger;

            Producer(List<String> buffer,Exchanger<List<String>> exchanger){
                this.buffer = buffer;
                this.exchanger = exchanger;
            }

            @Override
            public void run() {
                for(int i = 1 ; i < 5 ; i++){
                    System.out.println("生产者第" + i + "次提供");
                    for(int j = 1 ; j <= 3 ; j++){
                        System.out.println("生产者装入" + i  + "--" + j);
                        buffer.add("buffer：" + i + "--" + j);
                    }

                    System.out.println("生产者装满，等待与其他人交换...");
                    try {
                        buffer = exchanger.exchange(buffer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("生产者第" + i + "次提取");
                    for (int j = 1; j <= 3 ; j++) {
                        System.out.println("生产者 : " + buffer.get(0));
                        buffer.remove(0);
                    }
                }
            }
        }

        static class Consumer implements Runnable {
            private List<String> buffer;

            private final Exchanger<List<String>> exchanger;

            public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
                this.buffer = buffer;
                this.exchanger = exchanger;
            }

            @Override
            public void run() {
                for(int i = 1 ; i < 5 ; i++){
                    System.out.println("消费者第" + i + "次提供");
                    for(int j = 1 ; j <= 3 ; j++){
                        System.out.println("消费者装入" + i  + "--" + j);
                        buffer.add("buffer：" + i + "--" + j);
                    }

                    System.out.println("消费者装满，等待与其他人交换...");
                    try {
                        buffer = exchanger.exchange(buffer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("消费者第" + i + "次提取");
                    for (int j = 1; j <= 3 ; j++) {
                        System.out.println("消费者 : " + buffer.get(0));
                        buffer.remove(0);
                    }
                }
            }
        }

    static class Center implements Runnable{

        //生产者、消费者交换的数据结构
        private List<String> buffer;

        //步生产者和消费者的交换对象
        private Exchanger<List<String>> exchanger;

        Center(List<String> buffer,Exchanger<List<String>> exchanger){
            this.buffer = buffer;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for(int i = 1 ; i < 5 ; i++){
                System.out.println("第三者第" + i + "次提供");
                for(int j = 1 ; j <= 3 ; j++){
                    System.out.println("第三者装入" + i  + "--" + j);
                    buffer.add("buffer：" + i + "--" + j);
                }

                System.out.println("第三者装满，等待与其他人交换...");
                try {
                    buffer = exchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("第三者第" + i + "次提取");
                for (int j = 1; j <= 3 ; j++) {
                    System.out.println("第三者 : " + buffer.get(0));
                    buffer.remove(0);
                }
            }
        }
    }
}
