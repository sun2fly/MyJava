package com.mrfsong.struct.sort.v3;

import com.mrfsong.struct.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Auther: songfei20
 * @Date: 2021/1/20 20:03
 * @Description:
 */
@Slf4j
public class BubbleSortV3Test extends TestBase {

    @Test
    public void execute() throws Exception {
        log.info("Before : {}" , Arrays.toString(intArray));

        int len = intArray.length;

        /**
         * 逐个比较相邻元素、若后者较小则进行交换；
         * 每【趟】比较后、最大元素将会出现在数组最后；
         */
        for(int i=0;i < len; i++){//关键点1： 此处外层循环只是为了控制比较"趟次"、总共需要比较len趟
            for(int j = 0;j<(len-i-1);j++){//关键点2：一趟比较完成、最大值一定会出现在数组尾部，下趟不需要再参与比较；最后一组比较元素为(arr[len-2],arr[len-1])
                if(intArray[j] > intArray[j+1]){
                    int tmp = intArray[j];
                    intArray[j] = intArray[j+1];
                    intArray[j+1] = tmp;
                }
            }
        }


        log.info("After : {}" , Arrays.toString(intArray));




    }



}