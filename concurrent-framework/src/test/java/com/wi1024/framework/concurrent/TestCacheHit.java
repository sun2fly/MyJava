package com.wi1024.framework.concurrent;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 缓存行示例
 * <p>
 *     在CPU缓存中，数据是以缓存行(cache line)为单位进行存储的，每个缓存行的大小一般为32--256个字节，常用CPU中缓存行的大小是64字节；
 *     CPU每次从内存中读取数据的时候，会将相邻的数据也一并读取到缓存中，填充整个缓存行；
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 13:37
 **/
@Slf4j
public class TestCacheHit {

    private static long[][] longs;
    private static int length = 1024 * 1024;

    @Test
    public void execute(){
        longs = new long[length][];
        for(int x=0; x < length; x++){
            longs[x] = new long[6];
            for(int y=0; y<6 ; y++){
                longs[x][y] = 1L;
            }
        }
        cacheHit();
        cacheMiss();
    }


    /**
     * <p>
     *     在Java中，一个long类型是8字节，而一个缓存行是64字节，因此一个缓存行可以存放8个long类型。
     * 但是，在内存中的布局中，对象不仅包含了实例数据(long类型变量)，还包含了对象头。对象头在32位系统上占用8字节，而64位系统上占用16字节。
     * </p>
     *
     * <p>
     *     二维数组中填充了6个元素，占用了48字节,当第一次遍历的时候，获取longs[0][0]，而longs[0][0]--longs[0][5]也同时被加载到了缓存行中，接下来获取longs[0][1]，已存在缓存行中，直接从缓存中获取数据，不用再去内存中查找
     * </p>
     *
     */
    private void cacheHit(){
        long sum = 0L;
        long start = System.nanoTime();
        for(int x=0; x < length; x++){
            for(int y=0;y<6;y++){
                sum += longs[x][y];
            }
        }
        log.info("命中耗时："+(System.nanoTime() - start));
    }

    /**
     * <p>
     *     在Java中，一个long类型是8字节，而一个缓存行是64字节，因此一个缓存行可以存放8个long类型。
     * 但是，在内存中的布局中，对象不仅包含了实例数据(long类型变量)，还包含了对象头。对象头在32位系统上占用8字节，而64位系统上占用16字节。
     * </p>
     *
     * <p>
     *     二维数组中填充了6个元素，占用了48字节 ，当第一次遍历的时候，也是获取longs[0][0]的数据，longs[0][0]--longs[0][5]也被加载到了缓存行中，接下来获取long[1][0]，不存在缓存行中，去内存中查找。
     * </p>
     *
     */
    private void cacheMiss() {
        long sum = 0L;
        long start = System.nanoTime();
        for(int x=0; x < 6;x++){
            for(int y=0;y < length;y++){
                sum += longs[y][x];
            }
        }
        log.info("未命中耗时："+(System.nanoTime() - start));
    }
}
