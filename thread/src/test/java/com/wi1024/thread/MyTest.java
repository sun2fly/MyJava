package com.wi1024.thread;

import org.junit.Test;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/07 13:10
 **/
public class MyTest extends TestBase {

    @Test
    public void run() throws Exception {
        MyTask task = new MyTask();
        Thread thread = new Thread(task , "MyTask");
        Thread thread1 = new MyThread("MyThread" , thread);

        thread1.start();
        thread.start();

    }

}
