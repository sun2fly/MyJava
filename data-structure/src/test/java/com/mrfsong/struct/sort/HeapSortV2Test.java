package com.mrfsong.struct.sort;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * 堆排序
 */
@Slf4j
public class HeapSortV2Test {

    @Test
    public void execute() {
        int[] nums = JMockData.mock(new TypeReference<int[]>(){});
//        nums = new int[]{5104, 6979, 1955, 3925, 2127, 2831, 735, 8217, 8540, 9457};

        log.info("排序前 ====> {}" , Arrays.toString(nums));
        sort(nums);
        log.info("排序后 ====> {}" , Arrays.toString(nums));

    }


    public void sort(int[] nums) {

        //1. 构造大顶堆


        // 获取最后一个非叶子节点 , 最后一个元素满足：  n = 2 * m + 1 （左节点） | n = 2 * m + 2 (左右子节点)  （其中n为节点总数，m为最后一个非叶子节点坐标）
        int currRootIndex = (nums.length - 1) >> 1 ;
        //自底向上构建大顶堆
        for(;currRootIndex >= 0 ;currRootIndex--){
            adjust(nums ,nums.length, currRootIndex);
        }

        log.debug("max heap调整完成 ====> {}" , Arrays.toString(nums));





        //2. 交换数组收尾元素
        for(int i = nums.length - 1 ; i>0 ;i--){
            //大顶堆构建完成、首元素几位当前堆最大元素
            swap(nums,0,i);

            //交换后需要再次构建大顶堆
            adjust(nums,i , 0);
            log.debug("max heap调整完成 ====> {}" , Arrays.toString(nums));

        }



    }

    /**
     *
     * @param nums      源数据
     * @param heapSize  堆大小（用于控制参与构建大顶堆元素范围）
     * @param rootIndex 当前需要调整为大顶堆的子二叉树根节点【坐标】
     */
    public void adjust(int[] nums , int heapSize , int rootIndex){

        int leftIndex = rootIndex << 1 + 1;
        int rightIndex = rootIndex << 1 + 2;


        int maxIndex = rootIndex;

        //==================== 注意：根节点需要分别和左、右节点均比较一次，与【最大值】者交换 ！！！！==================== //
        //左侧子节点大于当前节点
        if(leftIndex < heapSize && nums[leftIndex] > nums[maxIndex]){
            maxIndex = leftIndex;
        }


        //右侧子节点大于当前节点或左侧节点
        if(rightIndex < heapSize && nums[rightIndex] > nums[maxIndex]){
            maxIndex = rightIndex;
        }

        //说明当前元素小于左子元素或右子元素
        if(maxIndex != rootIndex){

            // 大顶堆要求每个结点的值都大于或等于其左右孩子结点的值 ，
            // 收尾交换完毕后、它子节点树仍然保持正常的大顶堆结构
            swap(nums , rootIndex , maxIndex);

            //交换后、下游子节点原有大顶堆结构可能被破坏、需要再次构建
            adjust(nums, heapSize , maxIndex);

        }



    }


    public void swap(int[] arr , int i , int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    /**
     * TODO 打印二叉树树形结构
     * @param arr
     */
    private void printTree(int[] arr) {
        int treeDepth = (arr.length - 1) >> 1 + 1; //获取二叉树深度

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i ++) {
            for (int j = 0; j < arrayWidth; j ++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
       for(int i = 0;i<= treeDepth ; i++){

       }

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (String[] line: res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i ++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2: line[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
        }
    }





}
