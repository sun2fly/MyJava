package com.wi1024.thread;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;


/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/07 13:10
 **/
@Slf4j
public class MyTest extends TestBase {

    @Test
    public void run() throws Exception {
        MyTask task = new MyTask();
        Thread thread = new Thread(task , "MyTask");
        Thread thread1 = new MyThread("MyThread" , thread);

        thread1.start();
        thread.start();

    }


    @Test
    public void completableFuture() throws Exception {
        CompletableFuture<Double> futurePrice = getPriceAsync();
        log.info("========== Start ============");
        futurePrice.whenComplete(((aDouble, throwable) -> {
            log.info("========== Price:{} ============" , aDouble);
        }));
        log.info("========== End ============");

        Thread.sleep(1000 * 10);

    }


    private CompletableFuture<Double> getPriceAsync() {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 22.5;
        } , executor);

        return futurePrice;
    }

}
