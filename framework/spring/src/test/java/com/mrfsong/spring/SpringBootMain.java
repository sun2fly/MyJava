package com.mrfsong.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * SpringBoot
 * </P>
 *
 * @Author songfei20
 * @Date 2019/12/4
 */
@SpringBootApplication
@Slf4j
@ComponentScan(value = "com.mrfsong.spring.boot")
public class SpringBootMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class,args);
    }
}
