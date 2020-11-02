package com.mrfsong.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * TODO 尾递归
 */
@Slf4j
public class TailRecursion {

    @Test
    public void test(){

        log.info("fibonacci === > " + fibonacci(10));
        log.info("fibonacci === > " + fibonacci(10,1));

    }


    /**
     * 普通递归
     * @param n
     * @return
     */
    public int fibonacci(int n) {

        if(n <= 1){
            return 1;
        }

        return n * fibonacci(n-1);
    }

    /**
     * 尾递归
     * @param n
     * @param preResult
     * @return
     */
    public int fibonacci(int n , int preResult) {
        if(n <= 1){
            return preResult;
        }
        return fibonacci(n-1 , n * preResult);
    }

}
