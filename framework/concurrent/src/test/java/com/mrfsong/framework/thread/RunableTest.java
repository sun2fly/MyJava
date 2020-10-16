package com.mrfsong.framework.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


//AtomicStampedReference CyclicBarrier Semaphore Exchanger ReentrantReadWriteLock
@Slf4j
public class RunableTest {

    private Random random = new Random();

    @Before
    public void setUp(){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.info("Thread:[{}] exception !" , Thread.currentThread().getName());
            }
        });
    }

    @Test
    public void testJoin() throws InterruptedException {
        Thread subThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10L);
                    log.info("Thread {} finished !" , Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"SubThread");
        subThread.start();

        //join方法会让主线程等待当前线程运行结束
        //join方法必须在start()方法后调用
        //join底层通过synchronize方法内、自旋获取线程存活状态实现
        subThread.join();

        log.info("====== Waiting =====");
    }


    /**
     * CountDownLatch是直接基于AQS实现的
     */
    @Test
    public void testCountDownLatch() {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(3);

        for(int i=0;i<3;i++){
            new Thread(new CountDownLatchRunner(startSignal,doneSignal),"CountDownLatchWorker-"+i).start();
        }

        startSignal.countDown();
        try {
            log.info("[{}] is waiting for the worker to done ..." , Thread.currentThread().getName());
            doneSignal.await(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Everything is done ...");


    }

    /**
     * CyclicBarrier是基于ReentrantLock#Condition实现的、可以重用
     * @throws Exception
     */
    @Test
    public void testCyclicBarrier() throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> log.info("All thread is ready !"));

        for(int i=0;i<3;i++){
            new Thread(new CyclicBarrierRunner(("lp1-" + i),barrier)).start();
        }


   /*     TimeUnit.MICROSECONDS.sleep(300L);
        barrier.reset();*/


        for(int i=0;i<3;i++){
            new Thread(new CyclicBarrierRunner(("lp2-" + i),barrier)).start();
        }



    }

    @Test
    public void testSemaphore() throws Exception {
        Semaphore semaphore = new Semaphore(3,true);
        List<Thread> runThreads = new ArrayList<>();
        for(int i=0;i<3;i++){
            runThreads.add(new Thread(new SemaphoreRunner(2,semaphore),"SemaphoreRunner-"+i));
        }

        for(Thread t : runThreads){
            t.start();
        }

        //尝试等待子线程执行完成
        TimeUnit.SECONDS.sleep(30L);






    }


    //CyclicBarrier测试
    class CyclicBarrierRunner implements Runnable {

        private String name;
        CyclicBarrier barrier;

        public CyclicBarrierRunner(String name, CyclicBarrier barrier) {
            this.name = name;
            this.barrier = barrier;
        }

        @Override
        public void run() {

            try {
                SecureRandom random = new SecureRandom();
                int seed = random.nextInt(10);
                System.out.println("seed : " + seed);
                barrier.await(seed * 100L , TimeUnit.MILLISECONDS);
                System.out.println("[" + name + "] has fininshed !");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    class CountDownLatchRunner implements Runnable {

        private CountDownLatch startCountDownLatch;
        private CountDownLatch workerCountDownLatch;

        public CountDownLatchRunner(CountDownLatch startCountDownLatch, CountDownLatch workerCountDownLatch) {
            this.startCountDownLatch = startCountDownLatch;
            this.workerCountDownLatch = workerCountDownLatch;
        }

        @Override
        public void run() {
            try {
                startCountDownLatch.await();
                System.out.println("[" + Thread.currentThread().getName() + "] is working ..." );
                workerCountDownLatch.countDown();
                System.out.println("[" + Thread.currentThread().getName() + "] has finished !" );

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SemaphoreRunner implements Runnable {

        int demand;
        Semaphore semaphore ;

        public SemaphoreRunner(int demand, Semaphore semaphore) {
            this.demand = demand;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try{
                log.info("[" + Thread.currentThread().getName() + "] try to get " + demand + " permits , current available permits : " + semaphore.availablePermits());
                semaphore.acquire(demand);
                log.info("[" + Thread.currentThread().getName() + "] has got " + demand + "  permist successfully ! ");
                int rdn = ThreadLocalRandom.current().nextInt(10) + 1;
                TimeUnit.SECONDS.sleep(1L * rdn);
            } catch (InterruptedException e) {
                log.warn("[" + Thread.currentThread().getName() + "] was interrupted! ");
            }catch (Exception e ) {
                e.printStackTrace();
            } finally {
                semaphore.release(demand);
            }
            log.warn("[" + Thread.currentThread().getName() + "] has released " + demand + "  permists ! \n");
        }
    }


}
