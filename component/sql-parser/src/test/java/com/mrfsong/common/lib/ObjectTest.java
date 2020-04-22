package com.mrfsong.common.lib;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.hash.BloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
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


    class Entry {

        String key ;
        Entry next ;

        public Entry(String key) {
            this.key = key;
        }

        public Entry(String key, Entry next) {
            this.key = key;
            this.next = next;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Entry getNext() {
            return next;
        }

        public void setNext(Entry next) {
            this.next = next;
        }
    }

    @Test
    public void testMethodParam() {
        String strValue = String.join(",",valList);

        /*log.info("Internal of List : {}",ClassLayout.parseInstance(valList).toPrintable());
        log.info("Internal of String : {}",ClassLayout.parseInstance(strValue).toPrintable());

        log.info("External of List : {}",GraphLayout.parseInstance(valList).toPrintable());
        log.info("External of String : {}",GraphLayout.parseInstance(strValue).toPrintable());

        log.info("List total size :　{}",GraphLayout.parseInstance(valList).totalSize());
        log.info("String total size : {}",GraphLayout.parseInstance(strValue).totalSize());*/
       /* int h = 0;
        int hash = (h = strValue.hashCode()) ^ (h >>> 16);
        log.info("Hash: {}" , hash);*/

       /*Entry node_1 = new Entry("node1");
       Entry node_2 = new Entry("node2");
       Entry node_3 = new Entry("node3");
       Entry node_4 = new Entry("node4");

       node_1.next = node_2;
       node_2.next = node_3;

        log.info( ClassLayout.parseInstance(node_1).toPrintable());
        log.info( GraphLayout.parseInstance(node_1).toPrintable());


        *//*synchronized (this) {
            this.getClass().wait();
        }*//*

        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        int initSize = 1 << 4;

        int threshold = (int)(initSize * 0.75f);

        int capacity = Integer.highestOneBit((threshold - 1) << 1);
        log.info("capacity : {}" , capacity);
        log.info("2 << 1 capacity : {}" , Integer.highestOneBit(2 << 1));
        log.info("5 << 1 capacity : {}" , Integer.highestOneBit(5 << 1));

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
        log.info("Sum of odd number:"+sum);
    }

    @Test
    public void testDate() {

        Date date = new Date();
        log.info("Date.getTime:{}" , date.getTime());
        log.info("System.currentMills:{}" , Long.MAX_VALUE);

    }

    @Test
    public void testBloomFilter(){
        BloomFilter<String> dealIdBloomFilter ;

    }






}
