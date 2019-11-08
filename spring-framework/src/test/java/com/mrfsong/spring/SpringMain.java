package com.mrfsong.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/10/24
 */
public class SpringMain {

    public static void main(String[] args) {

        //ConfigurableApplicationContext
        //GenericApplicationContext

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        applicationContext.getBean("userService");
        applicationContext.getBean("prototypeService");
    }

}
