package com.mrfsong.struct.sort;

/**
 * <p>
 *     快速排序算法
 *
 *     思路：随机选取基准值，从数组两端（先右后左）交替与基准值比较、直到满足基准值左侧元素<=当前值，右侧元素>=当前值.
 *     STEP1: 从数列中挑出一个元素，称为 “基准”（pivot）;
 *     STEP2: 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
 *     STEP3: 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序；
 * </p>
 *
 * @author lysongfei@gmail.com
 * @create 11/01/2018 12:07
 */
public class QuickSort {

    public static void sort(int[] arr , int low , int high){
        if(arr.length < 2){
            return;
        }
        if(low < high){
            //获取中轴
            int pivot = partiton(arr , low , high);
            //左分区快排
            sort(arr, low , pivot - 1);
            //右分区快排
            sort(arr, pivot + 1 ,high);
        }

    }

    /**
     * 获取指定范围数据元素的中位值
     * @param arr   源数组
     * @param low   数组起始下标
     * @param high  数据结束下标
     * @return
     */
    public static int partiton(int[] arr , int low ,int high){
        int rand = (int) (Math.random() * (high - low)) + low;
        int base = arr[rand];
        //交换arr[low],arr[rand]值
        if(rand != low){
            arr[rand] = arr[low];
            arr[low] = base;
        }
        while(low < high){
            //自右向左依次与选取的基准值比较，最终结果：左侧值 <= base && 右侧值 >= base
            //TODO 为何一定要自右向左？？？
            while(low < high && arr[high] >= base){
                high--;
            }
            //右侧元素小于base，则将此元素换位至数组左侧
            arr[low] = arr[high];
            while(low < high && arr[low] <= base){
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = base;
        return low;
    }

}
