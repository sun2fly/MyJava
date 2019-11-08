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



    private int[] segment1 = new int[]{5,7,2};

    private int[] segment2 = new int[]{3,4,1};


    @Test
    public void execute() throws Exception {
        sort(segment1);
        sort(segment2);

        log.info(Arrays.toString(segment1));
        log.info(Arrays.toString(segment2));


        int[] result = new int[segment1.length+segment2.length];

        int pos = 0;


        //STEP1: 比较数组起始值、取值小者

        //STEP2：取STEP1中值小者所在数组的下一个元素和另外一个数组的首元素比较

        //
        int position_1 = 0;
        int position_2 = 0;
        if(segment1[position_1] < segment2[position_2]){
            result[position_1] = segment1[position_1];
            position_1++;
        }else if(segment1[position_1] == segment2[position_2]){
            position_1++;
            position_2++;


        }



        for(int i=0;i<segment1.length;i++){
            //STEP1: 比较数组起始值、取值小者

            //STEP2：取STEP1中值小者所在数组的下一个元素和另外一个数组的首元素比较

            //
            for(int j=0;j<segment2.length;j++){
                if(segment1[i] <= segment2[j]){
                    result[i] = segment1[pos];
                    pos++;
                    break;
                }

            }
        }



    }

    public void sort(int[] array) {
        InsertSort.sort(array);
    }





}
