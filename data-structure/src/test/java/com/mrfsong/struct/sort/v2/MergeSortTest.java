package com.mrfsong.struct.sort.v2;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * 归并排序算法
 *
 *
 *
 */
@Slf4j
//@RunWith(Parameterized.class)
public class MergeSortTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[100][0];
    }

    @Test
    public void execute() throws Exception {
        MockConfig mockConfig = new MockConfig();
        int[] nums = JMockData.mock(new TypeReference<int[]>(){} , mockConfig);

        nums = new int[]{2933, 3608, 6995, 6098, 9903, 609, 4984, 4394, 7230,10};
        log.info("Before sort ===> {}" , Arrays.toString(nums));
        sort(nums , 0 , nums.length -1);
        log.info("After sort ===> {}" , Arrays.toString(nums));


    }


    public void sort(int[] arr , int low , int high) {


        //递归退出条件：拆分后结果中只有1个元素
        if(low >= high){
            log.debug("After Divide ===> {},{}" , low,high );
            return ;
        }

        int mid = (low + high) >> 1;


        /*
            1. 对数组元素一分为二、直到不能继续拆分(只包含一个元素)，这个过程中并没有发生真实的排序动作
            2. 真实的排序动作是在merge函数中
        */

        //对数组左侧元素继续“拆分”
        sort(arr,low , mid);

        //对数组右侧元素继续“拆分”
        sort(arr,mid+1 , high);

        //合并左右两侧元素排序结果
        //自顶向下的递归方式、在左右两侧拆分操作结束前不会走到此操作
        // 自定向下 ===> 自底向上
        merge(arr , low , mid , high);




    }


    public void merge(int[] nums , int low , int mid , int high){

        log.warn("[merge info]  low : {} , mid: {} , high: {}" , new Object[]{low,mid,high});

        //拆分后临近左右两侧元素的合并结果
        int[] temp = new int[high-low+1];
        int i = low;
        int j = mid+1;
        int k = 0;
        // 找出较小值元素放入temp数组中
        // 双指针比较两个数组大小
        while(i<=mid && j<=high){
            if(nums[i]<=nums[j])
                temp[k++] = nums[i++];
            else
                temp[k++] = nums[j++];
        }
        // 处理较长部分
        while(i<=mid){
            temp[k++] = nums[i++];
        }
        while(j<=high){
            temp[k++] = nums[j++];
        }
        // 使用temp中的元素覆盖nums中元素
        for (int k2 = 0; k2 < temp.length; k2++) {
            nums[k2+low] = temp[k2];
        }
    }


}
