package com.mrfsong.struct.sort;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 * 冒泡排序
 *   排序思路：依次比较数组相邻元素大小，如果后者大于前者、则进行元素交换；如果不满足、则继续比较后续相邻元素。
*    arr[0] > arr[1] , swap arr[0] <-> arr[1]
 *   arr[1] > arr[2] , swap arr[1] <-> arr[2]
 *   ......
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/8/21 9:12
 * @Version 1.0
 */
@Slf4j
public class BubbleSortTest {

    private final int[] arr = JMockData.mock(new TypeReference<int[]>(){},new MockConfig().sizeRange(5, 10));

    @Test
    public void sort() {

        if(arr == null || arr.length < 2){
            return;
        }

        print(arr);


        for(int i=1;i<arr.length;i++){//确定循环次数
            boolean isOrdered = true;
            for(int j=0;j<(arr.length-i);j++){
                int tmp;
                if(arr[j] > arr[j+1]){
                    //交换元素
                    tmp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = tmp;
                    isOrdered = false;
                }
            }

            //此处判断数组是否有序，有序则只比较一次
            if(isOrdered){
                break;
            }

        }

        print(arr);


    }


    private void print(int[] arr) {
        log.debug("Array : [{}]" , Arrays.toString(arr));
    }

}
