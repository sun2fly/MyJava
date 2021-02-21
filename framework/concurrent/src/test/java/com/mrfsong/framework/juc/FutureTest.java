package com.mrfsong.framework.juc;


import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: songfei20
 * @Date: 2021/2/20 09:44
 * @Description:
 */
@Slf4j
public class FutureTest {


    @Test
    public void testThreadRetry() throws Exception {

        Retryer<String> RETRYER = RetryerBuilder.<String>newBuilder()
                //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()

                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(50, TimeUnit.MILLISECONDS))
                //尝试次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();

        final AtomicInteger retryCounter = new AtomicInteger(0);

        System.out.println("MainThread : " + Thread.currentThread().getName());
        Retryer.RetryerCallable<String> wrap = RETRYER.wrap(() -> {
            System.out.println("Curr Thread : " + Thread.currentThread().getName());

            System.out.println("The " + retryCounter.incrementAndGet() + "  retry !");
            ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
            // 执行任务
            ListenableFuture<String> future = executorService.submit(new Callable<String>() {
                public String call() throws Exception {
                    TimeUnit.SECONDS.sleep(1);
                    int i = 1 / 0;

                    return "" + System.currentTimeMillis();
                }
            });

            try {
                future.get(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException("future.get err");

            }

            //TODO onSuccess

            return null;

        });

        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(wrap);


        TimeUnit.SECONDS.sleep(10);


    }


    /**
     * ExecutorService#submit | ExecutorService#execute 区别：
     * 区别：
     *      1. submit方法提交任务、返回Future对象；execute方法无返回值；
     *      2. execute方法提交任务、若任务执行异常会捕获并向外抛出异常；submit方法将任务重新封装为FutureTask类型，run方法内部将异常全部捕获后、统一转换为FutureTask内部状态（可以通过get方法捕获异常）
     */
    @Test
    public void testThreadPoolException() {

        //1.实现一个自己的线程池工厂
        ThreadFactory factory = (Runnable r) -> {
            //创建一个线程
            Thread t = new Thread(r);
            //给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
            //原理：Thread#dispatchUncaughtException
            t.setDefaultUncaughtExceptionHandler((Thread thread1, Throwable e) -> {
                log.warn("执行自定义线程工厂设置的exceptionHandler");
                e.printStackTrace();
            });
            return t;
        };

        //2.创建一个自己定义的线程池，使用自己定义的线程工厂
        ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(10) , factory) {
            //重写afterExecute方法
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);

                //excute提交时、异常信息不为空
                if (t != null) {
                    System.out.println("afterExecute里面获取到异常信息" + t.getMessage());
                }

                //如果r的实际类型是FutureTask 那么是submit提交的，所以可以在里面get到异常
                if (r instanceof FutureTask) {
                    try {
                        Future<?> future = (Future<?>) r;
                        //submit提交需要通过此方式获取运行异常
                        future.get();
                    } catch (Exception e) {
                        //捕获后继续向外抛出、让UncaughtExceptionHandler生效
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        //2.提交任务
        service.submit(() -> {
            int i = 1 / 0;
        });



        //3.提交任务

        //注意： execute方法提交任务、会抛出线程运行异常信息；submit方式不会抛出异常
      /*  singleThreadExecutor.execute(()->{
            int i=1/0;
        });

        singleThreadExecutor.submit(()->{
            int i=1/0;
        });*/



    }


    /**
     * newSingleThreadExecutor | newFixedThreadPool(1)
     * 区别：
     *      1. newSingleThreadExecutor创建的线程池、后续无法再修改任何配置
     *      2.  newFixedThreadPool(1)创建的线程池、可以动态修改线程池配置
     *
     * @throws Exception
     */
    @Test
    public void testSingleThreadPool() throws Exception {

        /**
         * Thread ID:15---> Task1
         * Thread ID:15---> Task2
         * Thread ID:16---> Task3
         * Thread ID:16---> Task4
         * Thread ID:17---> Task3
         * Thread ID:16---> Task4
         */

        List<Runnable> tasks = Arrays.asList(
                ()->{System.out.println("Thread ID:" + Thread.currentThread().getId() + "---> Task1");},
                ()->{System.out.println("Thread ID:" + Thread.currentThread().getId() + "---> Task2");});

        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        tasks.forEach(singleThreadPool::submit);



        // 等待前面两个任务结束
        Thread.sleep(1000L);
        // 定义任务组 tasks2
        List<Runnable> tasks2 = Arrays.asList(
                ()->{System.out.println("Thread ID:" + Thread.currentThread().getId() + "---> Task3");},
                ()->{System.out.println("Thread ID:" + Thread.currentThread().getId() + "---> Task4");});

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        tasks2.forEach(fixedThreadPool::submit);
        Thread.sleep(1000L);

        //注意：fixedThreadPool可以重新修改线程池配置
        ThreadPoolExecutor configurableFixedThreadPool = (ThreadPoolExecutor) fixedThreadPool;
        configurableFixedThreadPool.setCorePoolSize(2);

        tasks2.forEach(fixedThreadPool::submit);

        // 关闭线程池
        fixedThreadPool.shutdown();
        // 等待任务执行结束
        fixedThreadPool.awaitTermination(1, TimeUnit.MINUTES);




    }

}