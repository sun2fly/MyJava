package com.mrfsong.spring.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class TestService {

    @Value("${test.key}")
    private String testKey;

    @PostConstruct
    private void init (){
        System.out.println("Test key : " + testKey);
        log.info("Test key : {}" , testKey);
    }

}
