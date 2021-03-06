package com.mrfsong.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorsTest {


    @Test
    public void scheduleWithFixedDelay() throws Exception {
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();

        /**
         * 每当上次任务执行完毕后，间隔一段时间执行。不管当前任务执行时间大于、等于还是小于间隔时间，执行效果都是一样的. (initialDelay+period * n)为单位执行
         * 创建并执行首先启用的定期操作在给定的初始延迟后，随后终止执行与执行之间的给定延迟下一个开始。 如果有任务执行
         * 遇到异常，将禁止后续执行。否则，任务将仅通过取消或终止而终止终止执行人。
         */
        ex.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                long nextLong = random.nextInt(5);
                log.info("==================== ScheduledExecutorService run , random : {} seconds ! ====================" , nextLong);

                try {
                    Thread.sleep(1000L * (5 + nextLong));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },1,5, TimeUnit.SECONDS);



        synchronized (ExecutorsTest.class) {
            while (true) {
                try {
                    ExecutorsTest.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Test
    public void scheduleAtFixedRate() throws Exception {
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        /*
            当前任务执行时间大于等于间隔时间，任务执行后立即执行下一次任务。相当于连续执行了.
            当具体任务处理时间小于调度周期时、仍会在配置的指定周期后执行下一个调度;
         */
        ex.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                long nextLong = random.nextInt(5);
                log.info("==================== ScheduledExecutorService run , random : {} seconds ! ====================" , nextLong);

                try {
                    Thread.sleep(1000L * (nextLong));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                throw new RuntimeException("eeee");
            }
        },1,5, TimeUnit.SECONDS);

        ex.shutdown();



        synchronized (ExecutorsTest.class) {
            while (true) {
                try {
                    ExecutorsTest.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
