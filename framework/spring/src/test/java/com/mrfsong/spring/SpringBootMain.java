package com.mrfsong.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
public class SpringBootMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class,args);
    }
}
