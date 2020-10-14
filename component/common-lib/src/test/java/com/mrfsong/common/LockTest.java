package com.mrfsong.common;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    
    public void rn() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        try {
            long l = condition.awaitNanos(1_000);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    
}
