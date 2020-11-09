package com.mrfsong.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class LoggerTest {

    private Logger logger = LogManager.getLogger(LoggerTest.class);


    @Test
    public void testLambda() throws Exception {

        // lambda表达式会延迟执行
        logger.info("lambda is : {}" , () -> "abc");
        if(logger.isInfoEnabled()){
            logger.info("string is : {}" , "abc");
        }
        logger.warn("here is : {}" , 123);

        TimeUnit.SECONDS.sleep(60L * 10);

    }


}
