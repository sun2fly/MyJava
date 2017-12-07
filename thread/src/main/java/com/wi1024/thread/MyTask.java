package com.wi1024.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/07 12:39
 **/
@Slf4j
public class MyTask implements Runnable {

    public void run() {

        log.info("My name is : {}" , Thread.currentThread().getName());



    }
}
