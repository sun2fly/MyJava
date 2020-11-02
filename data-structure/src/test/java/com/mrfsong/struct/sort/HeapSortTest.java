package com.mrfsong.struct.sort;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 *
 *      堆排序的基本思想是：将待排序序列构造成一个大顶堆，此时，整个序列的最大值就是堆顶的根节点。
 *      将其与末尾元素进行交换，此时末尾就为最大值。然后将剩余n-1个元素重新构造成一个堆，这样会得到n个元素的次小值。如此反复执行，便能得到一个有序序列了.
 *      最大堆/最小堆（完全二叉树）序
 *      最大堆：每个结点的值都大于或等于其左右孩子结点的值   arr[i] >= arr[2i+1] && arr[i] >= arr[2i+2]
 *      最小堆：每个结点的值都小于或等于其左右孩子结点的值   arr[i] <= arr[2i+1] && arr[i] <= arr[2i+2]
 *
 *
 *      树 -> 二叉树 -> 满二叉树 -> 完全二叉树 -> 堆 （大顶堆/小顶堆）
 *
 *      堆是一种使用数组形式保存的二叉树，可以通过遍历数组快速构造出一颗二叉树，这种数据结构可以利用连续的内存区域、节省空间。
 *      完全二叉树（堆）定义：
 *          若设二叉树的深度为h，除第h层外，其它各层(1～h-1) 的结点数都达到最大个数，第h 层所有的结点都连续集中在最左边，这就是完全二叉树
 *
 *      一个完整的二叉树可以被存在一个紧凑数组A中,因为一个完整的数组和一个完整的二叉树一样、中间都没有空隙。你会看到这个完整二叉树的所有的元素 从上到下，从左到右。
 *      这样，我们可以用操作序号的方式实现简单的二叉树遍历操作（运用简单的 位移操作):
 *          parent(i) = i>>1, 序号 i 除以二 (整数除法),
 *          left(i) = i<<1, 序号 i 乘 2,
 *          right(i) = (i<<1)+1, 序号 i 乘 2 再加 1.
 * </P>
 *
 * @Author songfei20
 * @Date 2019/8/21 9:11
 * @Version 1.0
 */
@Slf4j
public class HeapSortTest {

    @Test
    public void sort() {
//        int[] arr = JMockData.mock(new TypeReference<int[]>(){});
        int[] arr = new int[]{5741, 3775, 1657, 8785, 4634, 5787, 3538, 4537};
        log.info("Before sort : {}" , Arrays.toString(arr));
        buildMaxHeap(arr);
        log.info("After adjust : {}" , Arrays.toString(arr));
        sortHeap(arr);
        log.info("After sort : {}" , Arrays.toString(arr));
    }

    /**
     * 构造最大二叉堆(从最后一个非叶子节点开始，自下往上构造最大堆)
     * @param arr
     */
    private void buildMaxHeap(int[] arr){
        //获取最后叶子节点的父节点（即为最后一个非叶子节点位置）
        //此处是为何必须是完全二叉树的原因
        int lastNoLeafIndex = (int) Math.floor(arr.length / 2);;

        //自下往上，直到根节点
        for(;lastNoLeafIndex >= 0;lastNoLeafIndex--){
            adjustMaxHeap(arr,arr.length,lastNoLeafIndex);
        }
    }


    /**
     * 自上往下调整构造最大堆
     * @param arr
     * @param heapSize  堆大小（数据元素个数）
     * @param index     起始排序元素下标（构建最大堆起始元素）
     */
    private void adjustMaxHeap(int[] arr, int heapSize , int index) {
        int largestIndex = index;

        int leftIndex = index *2 + 1;
        int rightIndex = index * 2 + 2;

        //比较左侧叶子节点
        if(leftIndex < heapSize && arr[leftIndex] > arr[largestIndex]){
            largestIndex = leftIndex;
        }

        if(rightIndex < heapSize && arr[rightIndex] > arr[largestIndex]){
            largestIndex = rightIndex;
        }


        if(largestIndex != index){
            swap(arr, index ,largestIndex);

            log.debug("adjust heap : {}" , Arrays.toString(arr));
            //比较原大值节点子节点，保证子节点有序
            adjustMaxHeap(arr,heapSize,largestIndex);
        }

    }


    private void sortHeap(int[] arr){
        for(int heapSize=arr.length-1;  heapSize > 0 ;heapSize--){
            log.debug("sort source : {}" , Arrays.toString(arr));
            // 交换堆顶元素与第0个元素
            swap(arr , 0 , heapSize);

            log.debug("sort loop : {}" , Arrays.toString(arr));
            //TODO 当前heapSize位置节点已经归位，不再参与后续排序过程
            adjustMaxHeap(arr,heapSize,0);
        }

    }

    private void swap(int[] arr , int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    /*@Test
    public void test(){
        log.info(String.valueOf(5 >> 1));
        log.info(String.valueOf(1 >> 1));
        log.info(String.valueOf(0 >> 1));
        log.info(String.valueOf(-1 >> 1));
    }*/








}
