package com.mrfsong.struct.sort;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 *          归并排序
 *          <B>
 *              排序思路：
 *              将“大数组”分段为“小数组”，对小数组进行排序后、再依次对排序后的”小数组"进行合并。
 *              合并过程：
 *
 *          </B>
 * </P>
 *
 * @Author songfei20
 * @Date 2019/11/8
 */
@Slf4j
public class MergeSortTest {



    private int[] arr1 = new int[]{5,7,2,1,3};

    private int[] arr2 = new int[]{3,4,1};


    @Test
    public void execute() throws Exception {

        // 1: 先分别对数据进行排序
        InsertSort.sort(arr1);
        InsertSort.sort(arr2);

        log.info("Arr1 after sort : {}",Arrays.toString(arr1));
        log.info("Arr2 after sort : {}",Arrays.toString(arr2));


        int[] result = new int[arr1.length+ arr2.length];

        int pos = 0;

        //STEP1: 比较数组起始值、取值小者

        //STEP2：取STEP1中值小者所在数组的下一个元素和另外一个数组的首元素比较

        //
        int idx1 = 0 , idx2 = 0;
        int len1 = arr1.length , len2 = arr2.length;

        // 判断特殊情况（最大值小于最小值情况）

        // arr1最小值 >= arr2最大值，直接合并
        if(arr1[0] >= arr2[len2-1]) {
            System.arraycopy(arr2,0,result,0,arr2.length);
            System.arraycopy(arr1,0,result,arr2.length,arr1.length);

        }

        // arr2最小值 >= arr1最大值，直接合并
        if(arr2[0] >= arr1[len2-1]) {
            System.arraycopy(arr1,0,result,0,arr1.length);
            System.arraycopy(arr2,0,result,arr1.length,arr2.length);
        }


        // 循环比较插入元素位置
        while(idx1 < len1 || idx2 < len2) {

            // arr1已经处理完毕、直接插入arr2剩余元素

            if(idx1 >= len1) {

                System.arraycopy(arr2,idx2,result,(idx1 + idx2),(len2 - idx2));
                break;


              /*  result[pos] = arr2[idx2];
                idx2++;
                pos++;
                continue;*/
            }

            // arr2已经处理完毕、直接插入arr2剩余元素
            if(idx2 >= len2) {

                System.arraycopy(arr1,idx1,result,(idx1 + idx2),(len1 - idx1));
                break;
               /* break;
                result[pos] = arr1[idx1];
                idx1++;
                pos++;
                continue;*/
            }



            if(arr1[idx1] > arr2[idx2]){
                //推入arr2[idx2]
                result[pos] = arr2[idx2];
                idx2++;
            }else if(arr1[idx1] < arr2[idx2]) {
                // 推入arr1[idx1]
                result[pos] = arr1[idx1];
                idx1++;
            }else {
                // 推入arr1[idx1] arr2[idx2]
                result[pos] = arr1[idx1];
                result[++pos] = arr2[idx2];
                idx1++;
                idx2++;
            }
            pos++;
        }

        log.info("Result : {}" , Arrays.toString(result));
    }

}
