package com.github.dqqzj.springboot;

import com.github.dqqzj.springboot.filter.MyTypeFilter;
import com.github.dqqzj.springboot.filter.Tt;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.*;
@ComponentScan(
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {TypeExcludeFilter.class}
        ), @ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {AutoConfigurationExcludeFilter.class}
        )},
        includeFilters = {
                @ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {MyTypeFilter.class}
        )
        }
)
@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {
        /*AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserListener.class);
        context.refresh();
        UserEvent<User> userUserEvent = new UserEvent<>(new User("dqqzj"));
        context.publishEvent(userUserEvent);
        context.publishEvent(new User("aaaa"));
        context.close();*/
        SpringApplication.run(SpringbootApplication.class, args);
    }

    /**
     * 进入父类初始化构造器
     * @param beanFactory
     * @return
     */
    @Bean
    public ApplicationRunner runner(DefaultListableBeanFactory beanFactory) {
        return args -> {
            Tt tt = beanFactory.getBean(Tt.class);
            System.out.println(tt);
            /*BeanOne beanOne =  beanFactory.getBean(BeanOne.class);
            BeanTwo beanTwo =  beanFactory.getBean(BeanTwo.class);
            if (beanOne.getBeanTwo() == beanTwo){
                System.out.println("同样的bean");
            } else {
                System.out.println("不同样的bean");
            }*/
        };
    }
}
