package com.mrfsong.struct.sort;

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
 *          快速排序
 *          <B>
 *              排序思路：
*                  1. 挑选基准值：从数列中挑出一个元素，称为“基准”（pivot），
*                  2. 分割：重新排序数列，所有比基准值小的元素摆放在基准前面，所有比基准值大的元素摆在基准后面（与基准值相等的数可以到任何一边）。在这个分割结束之后，对基准值的排序就已经完成，
*                  3. 递归排序子序列：递归地将小于基准值元素的子序列和大于基准值元素的子序列排序。
 *
 *
 *          </B>
 * </P>
 *
 */
@Slf4j
@RunWith(Parameterized.class)
public class QuickSortV2Test {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[100][0]; // repeat count which you want
    }

    @Test
    public void execute() throws Exception {
        MockConfig mockConfig = new MockConfig();
        int[] nums = JMockData.mock(new TypeReference<int[]>(){} , mockConfig);

        log.info(" before sort ===> {}" , Arrays.toString(nums));

        quickSort(nums,0, nums.length-1);

        log.info("sort result ===> {}" , Arrays.toString(nums));



    }


    private void quickSort(int[] arr , int low ,int high) {

        if(low >= high){
            return ;
        }

        while(low < high){
            int privot = partition(arr , low ,high);
            quickSort(arr , low ,privot - 1);
            //此操作可以较少一次递归
            low = privot + 1;
        }




    }


    /**
     * 获取数组中位值（左侧值 <= 当前值  && 右侧值 > 当前值）
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private int partition(int arr[], int low ,int high) {

        int privotVal = arr[low];
        while(low < high){
            //自右向左
            while(low < high && arr[high] > privotVal){
                high--;
            }
            swap(arr,low,high);
            //自左向右
            while(low < high && arr[low] < privotVal){
                low++;
            }
            swap(arr,low,high);
        }


        return low;
    }


    private void swap(int[] arr ,int i ,int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
