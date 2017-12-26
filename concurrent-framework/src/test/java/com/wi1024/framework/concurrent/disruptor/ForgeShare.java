package com.wi1024.framework.concurrent.disruptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 缓存行"伪共享"示例
 * <p>
 *      几个在内存中相邻的数据，被CPU的不同核心加载在同一个缓存行当中，数据被修改后，由于数据存在同一个缓存行当中，进而导致缓存行失效，引起缓存命中降低
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 14:12
 **/
@Slf4j
public class ForgeShare implements Runnable {

    public static int NUM_THREADS = 4 ;

    public static final long ITERATIONS = 500L * 1000L * 1000L;

    private int handleArrayIndex;
    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];


    {
        for(int i=0;i < longs.length; i++){
            longs[i] = new VolatileLong();
        }
    }

    public ForgeShare(int handleArrayIndex) {
        this.handleArrayIndex = handleArrayIndex;
    }

    public static void main(String[] args) throws Exception {
        Thread.sleep(10000);
        long start = System.nanoTime();
        Thread[] threads = new Thread[NUM_THREADS];
        for(int i=0; i < threads.length ; i++) {
            threads[i] = new Thread(new ForgeShare(i));
        }
        for(Thread t : threads) {
            t.start();
        }
        for(Thread t : threads) {
            t.join();
            log.info("耗时："+(System.nanoTime() - start));
        }
    }



    public final static class VolatileLong {
        public volatile long value =0L;
        //VolatileLong=对象头(12字节)+value(8字节)+p1-p5(40字节)+p6(4字节) = 64 , 可以保证VolatileLong对象分布在不同缓存行中
        //IMPORTANT : 以下两行可以保证VolatileLong对象分布在不同缓存行中，避免冲突、产生伪共享，影响性能
        public long p1 , p2 ,p3 , p4 , p5 ;
        public int p6;

    }

    @Override
    public void run() {
        long i = ITERATIONS;
        while(0 != --i){
            longs[handleArrayIndex].value = i;
        }
    }
}
