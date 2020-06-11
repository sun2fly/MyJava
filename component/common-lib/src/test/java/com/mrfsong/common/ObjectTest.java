package com.mrfsong.common;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    /**
     * 日期Formatter
     */
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    public void testDateTime () {
        LocalDateTime startTime = LocalDateTime.parse("2020-03-01 01:00:00", DATE_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2020-06-02 00:00:00", DATE_FORMATTER);

        /*long hours = ChronoUnit.HOURS.between(startTime, endTime);
        long halfDays = ChronoUnit.HALF_DAYS.between(startTime, endTime);
        long days = ChronoUnit.DAYS.between(startTime, endTime);
        long months = ChronoUnit.MONTHS.between(startTime, endTime);
        long years = ChronoUnit.YEARS.between(startTime, endTime);

        log.info("hours:{} , halfDays:{} , days:{} , months:{} , years:{}" , hours,halfDays,days,months,years);*/


    }

    @Test
    public void testBloomFilter(){
        BloomFilter<String> dealIdBloomFilter ;

    }

    @Test
    public void testStringReplace(){

        String str = "a::1,b::2,c::3,d::4";

        //replace(char,char)方法底层使用的字符串匹配
        String replaceChar = str.replace(':', '=');

        //replace(charSequence,charSequence)方法底层使用的字符串匹配
        String replaceCharSequence = str.replace("::", "=");

        //replaceAll方法底层使用的正则表达式
        String replaceAll = str.replaceAll("::", "=");
        log.info("replace === > {}" , replaceChar);
        log.info("replaceAll === > {}" , replaceAll);

    }

    @Test
    public void testProcessBuilder() throws Exception {

        //ProcessBuilder用法
        ProcessBuilder proc = new ProcessBuilder();
        proc.command("notepad.exe","testfile");
        Process process = proc.start();
        int resultVal = process.waitFor();
        log.info("Result val : " + resultVal);

        //Process用法
        Process execProcess = Runtime.getRuntime().exec("notepad.exe testfile");
        int execRtValue = execProcess.waitFor();
        log.info("Result val : " + execRtValue);
    }

    @Test
    public void testSubList() {
        List<String> list = Lists.newArrayList("1","2","3","4","5","6","7");
        List<String> subList = list.subList(0, 2);
        subList.forEach(o -> log.info(o));

        for(;;){
            log.info("" + Math.random());
        }
    }








}
