package com.mrfsong.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: songfei20
 * @Date: 2021/2/23 20:29
 * @Description: 合并两个包含重复元素的有序列表（合并后要求列表有序、无重复数据）
 */
@Slf4j
public class MergeLinkedListTest {

    @Test
    public void execute() {
        int[] arr1 = new int[]{1,1,2,3,4,5,5,6,7,7};
        int[] arr2 = new int[]{3,3,5,6,6,7,7,8,9,10};
        int[] result = merge(arr1, arr2);
        log.info(Arrays.toString(result));
    }


    public int[] merge(int[] arr1 , int[] arr2) {
        int len1 = arr1.length;
        int len2 = arr2.length;

        Map<Integer,Integer> map = new HashMap<>();

        int idx1 = 0 , idx2 = 0 , curIdx = 0;
        while(idx1 < len1 && idx2 < len2){
            //双指针比较、取小值
            int curr = (arr1[idx1] <= arr2[idx2]) ? arr1[idx1++] : arr2[idx2++];

            //去重重复数值
            if(!map.containsKey(curr)){
                map.put(curr,curIdx);
                curIdx++;
            }
        }


        if(idx1 < len1){
            for(;idx1<len1;idx1++){
                if(!map.containsKey(arr1[idx1])){
                    map.put(arr1[idx1],curIdx);
                    curIdx++;
                }
            }
        }


        if(idx2 < len2){
            for(;idx2<len2;idx2++){
                if(!map.containsKey(arr2[idx2])){
                    map.put(arr2[idx2],curIdx);
                    curIdx++;
                }
            }
        }


        //debug
        map.entrySet().stream().forEach(entry -> {
            log.debug("Key: {} , Value: {}" , entry.getKey() , entry.getValue());
        });

        //String[] y = x.toArray(new String[0]);
        return map.keySet().stream().mapToInt(i->i).toArray();
    }




}