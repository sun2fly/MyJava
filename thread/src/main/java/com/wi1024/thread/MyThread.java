package com.wi1024.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 2017/11/29 23:00
 */
@Slf4j
public class MyThread extends Thread {

    private Thread thread;

    public MyThread(String name , Thread thread) {
        super(name);
        this.thread = thread;
    }

    @Override
    public void run() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("My name is : {}" , Thread.currentThread().getName());
    }
}
