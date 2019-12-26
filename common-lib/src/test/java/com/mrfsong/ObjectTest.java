package com.mrfsong;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/10/30
 */
@Slf4j
public class ObjectTest {

    private final List<String> valList = JMockData.mock(new TypeReference<List<String>>(){},new MockConfig().sizeRange(100, 100));


    @Test
    public void testMethodParam() {
        String strValue = String.join(",",valList);

        /*log.info("Internal of List : {}",ClassLayout.parseInstance(valList).toPrintable());
        log.info("Internal of String : {}",ClassLayout.parseInstance(strValue).toPrintable());

        log.info("External of List : {}",GraphLayout.parseInstance(valList).toPrintable());
        log.info("External of String : {}",GraphLayout.parseInstance(strValue).toPrintable());

        log.info("List total size :　{}",GraphLayout.parseInstance(valList).totalSize());
        log.info("String total size : {}",GraphLayout.parseInstance(strValue).totalSize());*/
        int h = 0;
        int hash = (h = strValue.hashCode()) ^ (h >>> 16);
        log.info("Hash: {}" , hash);



    }

    @Test
    public void testGoStatement() {
        int num, i,sum=0;
        go:{
            Scanner data = new Scanner(System.in);
            System.out.println("Enter a number");
            num=data.nextInt();
            for(i=0;i<100;i++)
            {
                sum=sum+i;
                if(i==num)
                    break go;
            }
        }
        System.out.println("Sum of odd number:"+sum);
    }






}
