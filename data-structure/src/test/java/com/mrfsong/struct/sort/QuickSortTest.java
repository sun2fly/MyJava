package com.mrfsong.struct.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/8/15 19:38
 * @Version 1.0
 */
public class QuickSortTest {


    public int[] sort(int[] sourceArray) throws Exception {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        return quickSort(arr, 0, arr.length - 1);
    }

    private int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }

    private int partition(int[] arr, int left, int right) {
        // 设定基准值（pivot）
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (arr[i] < arr[pivot]) {
                swap(arr, i, index);
                index++;
            }
        }
        swap(arr, pivot, index - 1);
        return index - 1;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

   /* private int partiton(int[] arr, int low , int high) {
        //此处随机选取“基准”
        int pivot = (int)Math.random() * (high - low) + low;
        int base = arr[pivot];
        if(pivot != low){
            arr[pivot] = arr[low];
            arr[low] = base;
        }

        //先从右侧比较、找到小于base值交换后在从左侧开始比较
        while(low != high){
            while(arr[high] >= base){
                high--;
            }
            arr[low] = arr[high];

            while(arr[low] <= base) {
                low++;
            }
            arr[high] = arr[low];
        }

        arr[low] = base;
        return low;
    }*/


//    private void sort(int[] arr) {
//
//
//    }

    @Test
    public void test(){

        int[] arr = new int[]{9,1,5,0,10,3,2};

     /*   int last = arr.length - 1;
        while(last > 0) {
            for(int i=0;i<last;i++){
                if(arr[i] > arr[i+1]){
                    int tmp = arr[i];
                    arr[i] = arr[i+1];
                    arr[i+1] = tmp;
                }
            }
            last--;
        }*/

        QuickSort.sort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));

        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));




    }


}
