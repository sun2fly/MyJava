package com.mrfsong.struct.sort.v2;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;



/**
 * <p>
 *          <B>
 *              冒泡排序排序思路：
 *              1. 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
 *              2. 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，最后的元素会是最大的数。
 *              3. 针对所有的元素重复以上的步骤，除了最后一个。
 *              4. 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
 *          </B>
 * </P>
 *
 */

@Slf4j
@RunWith(Parameterized.class)
public class BubbleSortV2Test {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0];
    }

    @Test
    public void execute() throws Exception {
        MockConfig mockConfig = new MockConfig();
        int[] nums = JMockData.mock(new TypeReference<int[]>(){} , mockConfig);
        sort(nums);
        log.info(Arrays.toString(nums));
    }


    private void sort(int[] arr) {
        int len = arr.length;
        for(int i=0;i<len;i++){
            for(int j = 0;j<len - 1 -i;j++){//重点：比较截止最后一个元素位置：swap(len-2,len-1)
                if(arr[j] > arr[j+1]){
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }

    }

}
