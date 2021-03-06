package com.mrfsong.common;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.BloomFilter;
import com.mrfsong.common.model.Group;
import com.mrfsong.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        LocalDateTime startTime = LocalDateTime.parse("2020-06-20 13:02:08", DATE_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2020-06-23 00:00:02", DATE_FORMATTER);

        /*long hours = ChronoUnit.HOURS.between(startTime, endTime);
        long halfDays = ChronoUnit.HALF_DAYS.between(startTime, endTime);
        long days = ChronoUnit.DAYS.between(startTime, endTime);
        long months = ChronoUnit.MONTHS.between(startTime, endTime);
        long years = ChronoUnit.YEARS.between(startTime, endTime);

        log.info("hours:{} , halfDays:{} , days:{} , months:{} , years:{}" , hours,halfDays,days,months,years);*/





        //min time of Day
        LocalDateTime startOfDay = startTime.toLocalDate().atStartOfDay();
        LocalDateTime atDayMin = startTime.toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime dayMin = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN);
        LocalDateTime midnight = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIDNIGHT);


        //max time of Day
        LocalDateTime dayMax = endTime.toLocalDate().atTime(LocalTime.MAX);


        long days = ChronoUnit.DAYS.between(dayMin,dayMax);
        long hours = ChronoUnit.HOURS.between(dayMin, dayMax);
        log.info("Days : {} , Hours : {}" , days,hours);

        //convert to millseconds
        LocalDate today = LocalDate.now();
        long val = today.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        Assert.assertTrue(val == 1608134400000L);

        //convert millseconds to LocalDateTime
        long timestamp = System.currentTimeMillis();
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();

        //convert date to locaDate
        Date date = new Date();
        LocalDateTime localDateTime2 = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        LocalDate localDate2 = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDate();


    }

    @Test
    public void testBloomFilter(){
        BloomFilter<String> dealIdBloomFilter ;

    }

    @Test
    public void testString(){

        String str = "a::1,b::2,c::3,d::4";

        //replace(char,char)方法底层使用的字符串匹配
        String replaceChar = str.replace(':', '=');

        //replace(charSequence,charSequence)方法底层使用的字符串匹配
        String replaceString = str.replace("::", "=");

        //replaceAll方法底层使用的正则表达式
        String replaceAllString = str.replaceAll("::", "=");
        log.info("replace === > {}" , replaceString);
        log.info("replaceAll === > {}" , replaceAllString);

        log.warn("==================== Test Replace ====================");

        log.info("[0-0]: {}" ,str.substring(0, 0));
        log.info("[0-0]: {}" ,str.substring(1, 1));
        log.info("[0-0]: {}" ,str.substring(1, str.length()));
        log.warn("==================== Test Substring ====================");



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
    public void testTest() {

        int[] nums = new int[]{1,3,2,9,5,0,4};
        IntStream stream = Arrays.stream(nums);
        List<Integer> list = stream.boxed().collect(Collectors.toList());
        List<Integer> subList = new ArrayList<>(list.subList(0,3));


        subList.stream().forEach(e -> log.info("element : {}" , e));


        log.info("Max int : {}" , String.valueOf(Integer.MAX_VALUE));




    }

    /**
     * [)区间 不包括后者
     */
    @Test
    public void testSubList() {
        List<Integer> indexList = Lists.newArrayList(1,3,5);
        List<String> list = Lists.newArrayList("1","2","3","4","5","6","7");



        int position = 0;
        Map<Integer,Integer> map = new LinkedHashMap<>();
        for(int idx : indexList){
            if(position < idx){
                map.put(position,idx);
            }
            position = idx + 1;
        }

        if(position < list.size()) {
            map.put(position,list.size());
        }

        log.warn("position: {}" , position);

        map.entrySet().forEach(entry -> {
            log.info("startIndex : {} , endIndex : {}" , entry.getKey() , entry.getValue());
        });



       /* for(;;){
            log.info("" + Math.random());
        }*/
    }


    @Test
    public void testDeque() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.offerLast(5);
        deque.offerLast(3);
        deque.offerLast(6);
        deque.offerFirst(1);

        log.info("first ===> {} " , deque.peekFirst() );
        log.info("last ===> {} " , deque.peekLast() );
        log.info("deque size : {}" , deque.size());

        Iterator<Integer> iterator = deque.descendingIterator();
        StringBuilder stringBuilder = new StringBuilder("");
        while(iterator.hasNext()){
            stringBuilder.append(" " + iterator.next());
        }
        log.info("descendingIterator : {}" , stringBuilder.toString());

        stringBuilder = new StringBuilder("");
        iterator = deque.iterator();
        while(iterator.hasNext()){
            stringBuilder.append(" " + iterator.next());
        }
        log.info("iterator : {}" , stringBuilder.toString());



//        deque.descendingIterator();


    }

    @Test
    public void testNum(){
        Assert.assertFalse(100L > 100 * 1.00001d);

        log.warn("==================== Test Round ====================");
        long round = Math.round(1.6d);//四舍五入
        double ceil = Math.ceil(1160358 * 1.0d / 1000000);//向上取整
        double ceil2 = Math.ceil(1160358 / 1000000 * 1.0d);//向上取整
        double ceil3 = Math.ceil(113 * 0.9d);//向上取整
        double floor = Math.floor(1.6d);//向下取整
        log.info("round: {} , ceil:{} , floor:{} \n" , round , ceil3 ,floor);

        log.warn("==================== Test Precision ====================");
        double var1 = (1160358 * 1.0d) / 1000000;
        double var2= 1160358 / (1000000 * 1.0d);
        log.info("var1: {} , var2:{} \n" , var1, var2);

        log.warn("==================== Test Shift ====================");
        int i = 1 << 16 + 52;
        int j = 2 << 16 + 53;
        log.info("i: {} , j:{} \n" , i ,j );

        log.warn("==================== Test Mod ====================");
        log.info(String.valueOf(-123 % 10));
        log.info(String.valueOf(-123 / 10));
        log.info(String.valueOf(0 % 10));
        log.info(String.valueOf(0 / 10));

    }

    @Test
    public void testRandom() {

        Random random = new Random();
        for(int i =0;i<100 ;i++){
            log.info("Random with int : {} , value : {}" , 1, random.nextInt(10));
            log.info("Random with float : {} , value : {}" , 1, random.nextFloat());
            log.info("Random with double : {} , value : {}" , 1, random.nextDouble());
        }




    }

    @Test
    public void testObjectRef() throws Exception {

        List<String> list = Lists.newArrayList("a","b","c");
        User user = new User();
        user.setAge(35);
        user.setName("zangsan");
        user.setAddress("beijing");
        user.setList(Lists.newArrayList(list));


        log.info("User : {}" , user.toString());

        //Felix: 对象是地址引用、赋值只是调整对象的地址
        Group group = new Group("group1",user);
        group.getUser().setName("lisi");

        list.clear();

        log.info("User : {}" , user.toString());

    }


    @Test
    public void testByteKey() throws Exception {
        byte[] keyBytes1 = "hello".getBytes();
        byte[] keyBytes2 = "hello".getBytes();

        Assert.assertNotEquals(keyBytes1,keyBytes2);

        Map<byte[],String> map = Maps.newHashMap();

        map.put(keyBytes1,"java");
        map.put(keyBytes2,"golang");

        boolean equals = Arrays.equals(keyBytes1, keyBytes2);
        Assert.assertTrue(equals);

        Assert.assertNull(map.get("hello".getBytes()));

    }


    @Test
    public void testEquals() {
        //[-128 , 127] int cache
        List<Integer> intList = Lists.newArrayListWithCapacity(2);
        intList.add(-127);
        intList.add(-127);
        Assert.assertTrue(intList.get(0) == intList.get(1));


        intList.clear();
        intList.add(128);
        intList.add(128);
        Assert.assertFalse(intList.get(0) == intList.get(1));
    }

    @Test
    public void testShift() {
        /**
         *   在Java编程语言中，移位操作符包含三种，分别是 <<（左移）、 >>（带符号右移）和 >>>（无符号右移），
         * 这三种操作符都只能作用于long、int、short、byte、char这四种基本的整型类型上。
         *  原码： 原码就是符号位加上真值的绝对值, 即用第一位表示符号（正数：0 / 负数：1）, 其余位表示值
         *  补码：正数的补码就是其本身；负数的补码是在其原码的基础上, 符号位不变, 其余各位取反, 最后+1. (即在反码的基础上+1)；
         *  反码：正数的反码是其本身；负数的反码是在其原码的基础上, 符号位不变，其余各个位取反；
         *
         * 1. 左移操作符 << 是将数据转换成二进制数后，向左移若干位，高位丢弃，低位补零 （左移一位相当于乘以2的1次方，左移n位就相当于乘以2的n次方）
         * 2. 带符号右移操作符 >> 是带符号的右移操作符，将数据转换成二进制数后，向右移若干位，高位补符号位，低位丢弃 （右移一位相当于除以2的1次方，右移n位就相当于除以2的n次方）
         * 3. 无符号右移操作符 >>> 二进制数后右移若干位,不论负数与否，结果都是高位补零，低位丢弃(只是对32位和64位的值有意义)
         *
         */

        int result = 50;
        int i = 0;
        while (result != 0) {
            i++;
            result = result >> 1;
            log.info("========== After right shift 2 bit :{}==========" , result);
           /* tmp = result >>> 1;
            log.info("========== After right shift 3 bit :{}==========" , tmp);*/
        }
        log.info("==========i:{}==========" , i);


        result = 10;
        log.info("origin bit : {}" , Integer.toBinaryString(result));

        log.info("",Integer.toBinaryString(result << 1));
        log.info("",Integer.toBinaryString(result >> 1));
        log.info("",Integer.toBinaryString(result >>> 1));

        //0-1 1-2 2-4 4-8 8-16 16-32 32-64 64-128 128-256 256-512 512-1024 1024-2048

    }

    @Test
    public void testPath () throws Exception {
        java.nio.file.Path lastestJobPath = Paths.get("/tmp/flinkx/checkpoint");
        Optional<java.nio.file.Path> chkPath = Files.list(lastestJobPath)
                .filter(p -> Files.isDirectory(p) && p.toFile().getName().startsWith("chk-"))
                .sorted((p1, p2)-> Long.valueOf(p2.toFile().lastModified())
                        .compareTo(p1.toFile().lastModified()))
                .findFirst();
        String chkAbsPath = "";
        if(chkPath.isPresent()){
            chkAbsPath = chkPath.get().toAbsolutePath().toString();
        }
        log.info("Path : " + chkAbsPath);

        //长度不足，高位补零
        String prefFormat = String.format("/%019d", System.currentTimeMillis());
        String subfixFormat = String.format("/%d019", System.currentTimeMillis());
        log.info("format : {}" , prefFormat);
        log.info("format : {}" , subfixFormat);
        Long strToLong = Long.valueOf("0000001607414419733");
        log.info("strToLong : {}" , strToLong);

    }


    @Test
    public void testJdk8InterfaceFeture() {
        TestInterface dInter = new TestInterface() {
           /* @Override
            public String sayHi() {
                return null;
            }*/
        };
        String iMOk = TestInterface.areYouOk();
        String sayHi = dInter.sayHi();
        log.info("from default method : {}" , sayHi);
        log.info("from static method : {}" , iMOk);

    }



    @Test
    public void test_Exeption__Return_Finally() throws Exception {
        Throwable ex = null;
        try{
            log.info("I'm method body.");
        } catch (Exception e) {
            ex = e;
        }finally {
            //return后仍然会执行
            log.info("========= 倔强的finally =========");
            //addSuppressed()方法
            // 在java7中为Throwable类增加addSuppressed方法。当一个异常被抛出的时候，可能有其他异常因为该异常而被抑制住，从而无法正常抛出。这时可以通过addSuppressed方法把这些被抑制的方法记录下来。被抑制的异常会出现在抛出的异常的堆栈信息中，也可以通过getSuppressed方法来获取这些异常。
            ex.addSuppressed(new RuntimeException("finally exception!"));
            throw new RuntimeException(ex);
        }
    }

    private String print(Object obj) {
        return obj.toString();
    }








}
