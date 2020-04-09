package com.mrfsong.framework.disruptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 模拟Cas(Compare And Swap)实现
 * <p>
 *     CAS是对内存中共享数据操作的一种指令，该指令就是用乐观锁实现的方式，对共享数据做原子的读写操作。
 *     原子本意是“不能被进一步分割的最小粒子”，而原子操作意为”不可被中断的一个或一系列操作”。
 *     原子变量能够保证原子性的操作，意思是某个任务在执行过程中，要么全部成功，要么全部失败回滚，恢复到执行之前的初态，不存在初态和成功之间的中间状态。

 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 15:52
 **/
@Slf4j
public class Cas implements Runnable {

    private int memoryValue = 1;
    private int expectValue;
    private int updateValue;

    public Cas(int expectValue, int updateValue) {
        this.expectValue = expectValue;
        this.updateValue = updateValue;
    }

    @Override
    public void run() {

        if(memoryValue == expectValue) {
            this.memoryValue = updateValue;
            log.info("修改成功！");
        }else {
            log.info("修改失败!");
        }
    }

    public static void main(String[] args) throws Exception {
        Cas cas = new Cas(1 ,2 );
        Thread thread = new Thread(cas);
        thread.start();

        Thread thread1 = new Thread(cas);
        thread1.start();

        thread.join();
        thread1.join();

    }
}
