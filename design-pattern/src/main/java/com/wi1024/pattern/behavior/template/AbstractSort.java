package com.wi1024.pattern.behavior.template;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:18
 */
@Slf4j
public abstract class AbstractSort {

    protected abstract void sort(int[] array);

    public void showSortResult(int[] array){
        this.sort(array);
        log.info("排序结果：");
        for (int i = 0; i < array.length; i++){
            System.out.printf("%3s", array[i]);
        }
    }
}
