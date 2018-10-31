package com.mrfsong.struct.sort;

/**
 * <p>
 *     插入排序
 * </p>
 *
 * @author lysongfei@gmail.com
 * @create 11/01/2018 12:08
 */
public class InsertSort {

    public static void sort(int[] arr){
        for(int i=1; i < arr.length ;i++){
            int value = arr[i];
            int j = i - 1;

            //插入已排序列表
            for(;j >= 0 ; j--){
                if(arr[j] > value){
                    arr[j+1] = arr[j];
                    arr[j] = value;
                }
            }
        }
    }


}
