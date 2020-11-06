package com.mrfsong.struct;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.mrfsong.struct.pojo.LinkedNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.github.jsonzou.jmockdata.JMockData.mock;

/**
 * <p>
 *      leetcode pratice
 * </P>
 *
 * @Author songfei20
 * @Date 2019/11/21
 */
@Slf4j
@RunWith(Parameterized.class)
public class LeetCodeTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0]; // repeat count which you want
    }


    @Test
    public void run() {
//        int maxLength = lengthOfLongestSubstring("abcdefabca");


//        double midVal = findMedianSortedArrays(new int[]{1,2} , new int[]{3,4});
//        log.info("findMedianSortedArrays values : {}" , String.valueOf(midVal));

//        myAtoi("  -123&44 ");
        /*String s = "-012344";
        char[] chs = s.toCharArray();
        char[] tgt = new char[chs.length];
        System.arraycopy(chs, 0, tgt, 0, chs.length);*/
//        log.info(intToRoman(140));
//        mergeSortedArray(new int[]{9,10,11,0,0,0},3,new int[]{2,5,6},3);
        log.info("climbStairs values : {}" , String.valueOf(climbStairs(5)));

    }

    /**
     * 最长不重复字符串
     * @param s
     * @return
     */
    public  int lengthOfLongestSubstring03(String s) {
        int n = s.length(), ans = 0;
        //创建map窗口,i为左区间，j为右区间，右边界移动
        Map<Character, Integer> map = new HashMap<>();
        for (int j = 0, i = 0; j < n; j++) {
            // 如果窗口中包含当前字符，
            if (map.containsKey(s.charAt(j))) {
                //左边界移动到 相同字符的下一个位置和i当前位置中更靠右的位置，这样是为了防止i向左移动
                i = Math.max(map.get(s.charAt(j)), i);
            }
            //比对当前无重复字段长度和储存的长度，选最大值并替换
            //j-i+1是因为此时i,j索引仍处于不重复的位置，j还没有向后移动，取的[i,j]长度
            ans = Math.max(ans, j - i + 1);
            // 将当前字符为key，下一个索引为value放入map中
            // value为j+1是为了当出现重复字符时，i直接跳到上个相同字符的下一个位置，if中取值就不用+1了
            map.put(s.charAt(j), j+1);
        }
        return ans;
    }

    /**
     * 最长不重复字符串（滑动窗口）
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {

        if(s == null || s.length() == 0) {
            return 0;
        }



        int size = s.length();
        int longestStrLength = 0;
        int left = 0 , right = 0; //Slide window范围

        Map<Character , Integer> map = new HashMap<>();//记录窗口数据


        while(right < size){
            char ch = s.charAt(right);
            if(map.containsKey(ch)){

                longestStrLength = Math.max(longestStrLength, right - left);

                //窗口重置
                left = map.get(ch) + 1;

                map.clear();


            }else {
                map.put(ch,right);
                right++;
            }
        }





        return longestStrLength;

    }

    /**
     * 求数组中位数
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //则当N为奇数时，X[n+1 / 2]；当N为偶数时，(X[n/2] + X[n/2+1]) / 2

        int[] arr = new int[nums1.length + nums2.length];

        System.arraycopy(nums1, 0, arr, 0, nums1.length);
        System.arraycopy(nums2, 0, arr, nums1.length, nums2.length);

        log.info(Arrays.toString(arr));

        //数组排序
        Arrays.sort(arr);

        int maxPos = arr.length - 1;
        boolean even = arr.length % 2 == 0 ? true : false;
        if(even){
            return (arr[maxPos/2] + arr[maxPos/2 +1] ) * 1.0 / 2;
        }else {
            return arr[(maxPos + 1) / 2];
        }


    }

    /**
     * 查找最长回文字符串（动态规划）
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {





        return null;

    }


    /**
     * 查找字符串中有效数值
     * @param str
     * @return
     */
    public int myAtoi(String str) {

        if(str == null || str.trim().length() == 0) {return 0;}

        str = str.trim();
        char[] chs = str.toCharArray();

        //数字Ascli取值范围[48,57] ([0,9])


        //1: 判断首字符、确定数字起始搜索位置
        boolean negative = false;
        int startIndex = 0;
        int result = 0;
        int bit = 0;

        if(chs[0] == '-'){
            negative = true;
        }

        if(chs[0] == '-' || chs[0] == '+'){
            startIndex++;
        }


        int lastIntBit = Math.min(Integer.MAX_VALUE % 10,Math.abs(Integer.MIN_VALUE % 10));
        for(;startIndex < chs.length;startIndex++){
            if(chs[startIndex] >= '0' && chs[startIndex] <= '9'){
                //计算Int值
                bit = chs[startIndex] - '0';
            }else {
                break;
            }


            // (Integer.MAX_VALUE % 10) = 7 , (Integer.MIN_VALUE % 10) = 8
            // 当result == Integer.MAX_VALUE / 10 时，若bit > 7 (等价于bit >= 8) 则首先已经超过正整数的最大值、并且小于等于负整数的最小值
            // byte取值范围：   -2^7 ~ 2^7-1             [1字节]
            // short取值范围：  -2^15 ~ 2^15-1           [2字节]
            // int取值范围：   -2^31 ~ 2^31-1            [4字节]
            // long取值范围:   -2^63 ~ 2^63-1            [8字节]
            // char [2字节]      boolean [1位]   float [4字节] double [8字节]
            if(result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && bit > lastIntBit)) {
                return negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }

            /** ========== 本算法核心逻辑，通过拆分数字位、逐步累加计算避免overflow ========== */
            result = result * 10 + bit;

        }

        return negative ? -result : result;

    }

    /**
     * [字符串遍历]判断数字是否为回文数
     * (单数字默认为true)
     * @param x
     * @return
     */
    public boolean isPalindrome(int x) {

        String str = String.valueOf(x);
        int length = str.length();

        if(length < 2){
            return true;
        }

        boolean isEven = str.length() % 2 == 0 ? true : false; //判断长度为偶数还是奇数
        int middle = (isEven) ? length / 2 : length / 2 + 1;
        for(int i=0;i<middle;i++) {
            if(str.charAt(i) != str.charAt(length-1-i)){
                return false;
            }
        }

        return true;

    }

    /**
     * [数字计算]判断数字是否为回文数
     * (单数字默认为true)
     * @param x
     * @return
     */
    public boolean isPalindrome2(int x) {

        if(x < 0){
            return false;
        }

        int reverse = 0;

        int up = x,bit=0;

        while(up != 0){
            bit = up % 10;
            up = up / 10;
            reverse = reverse * 10 + bit;
        }

        if(reverse == x) {
            return true;
        }else {
            return false;
        }

    }


    /**
     * 给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。
     * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     *
     *
     *
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        //(1,a1) | (2,a2) | (3,a3) | (4,a4) .... (i,ai)
        // (i,ai) | (j,aj)  ===> max((j-i) * min(ai,aj))

        if(height.length < 3){
            return 1 * Math.min(height[0],height[1]);
        }


        int maxArea = 0 ;
        for(int i=0;i<height.length;i++){
            for(int j = i+1 ; j < height.length;j++) {
                int area = (j - i) * Math.min(height[j] , height[i]);
                maxArea = (area > maxArea) ? area : maxArea;
            }

        }

        log.info("Max area : {}" , maxArea);

        return maxArea;
    }

    /**
     * 整数转罗马数字
     *
     *
     * 字符          数值
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     *
     * 这个特殊的规则只适用于以下六种情况：
     *
     * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
     * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。 
     * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
     *
     * @param num
     * @return
     */
    public String intToRoman(int num) {

        if(num <= 0 || num > 3999){
            return null;
        }

        // 判断目标区间 [1,5) [5,10) [10,50) [50,100) [100,500) [500,1000) [1000,4000)

        // 分解组合    (1000 * a) + (500 * b) + (100 * c) + (50 * d) + (10 * e) + (5 * f) + (1 * g) = num


        // 1000 -> 500 -> 100 -> 50 -> 10 -> 5 -> 1

//        int[] base = new int[]{1000,500,100,50,10,5,1};
        int[] base = new int[]{1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanChars = new String[]{"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        int round = 0;      //取整
        int remainder = 0; //取余

        int tmp = num;
        String result = "";
        for(int i=0;i<base.length;i++) {
            round = tmp / base[i];
            tmp = tmp % base[i];

            if(round > 0){
                for(int repeat=1;repeat <= round ; repeat++){
                    result += romanChars[i];
                }
            }

            if(tmp == 0) {
                break;
            }
        }


        // 判断特征值 4 / 9 / 40 / 90 / 400 / 900





        return result;
    }

    /**
     * 罗马数字转数字
     * @param s
     * @return
     */
    public int romanToInt(String s) {
        int[] base = new int[]{1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanChars = new String[]{"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};


        return 0;
    }

    /**
     * 合并 k 个排序链表，返回合并后的排序链表
     *
     * WARN: 对象引用、交换
     * TODO 内存不足
     *
     *
     * @param lists
     * @return
     */
    public LinkedNode mergeKLists(LinkedNode[] lists) {

        if(lists == null || lists.length == 0) {
            return null;
        }

        if(lists.length == 1){
            return lists[0];
        }


        // 最终合并完成链表
        LinkedNode finalNode = null;
        for(int i=0;i<lists.length-1;i++) {

            //依次比较、合并相邻链表
            LinkedNode nextNode = null;
            LinkedNode firstCmpNode = lists[i];
            LinkedNode secondCmpNode = lists[++i];


            // 循环结束条件：链表到尾元素
            while(firstCmpNode != null && secondCmpNode != null) {

                if(firstCmpNode.val < secondCmpNode.val){
                    nextNode = firstCmpNode;
                    firstCmpNode = firstCmpNode.next;
                } else {
                    nextNode = secondCmpNode;
                    secondCmpNode = secondCmpNode.next;
                }


                if(finalNode == null){
                    finalNode = nextNode;
                }else {
                    finalNode.next = nextNode;
                }

            }

            //追加剩余节点
            while(firstCmpNode !=null) {
                finalNode.next = finalNode;
                firstCmpNode = firstCmpNode.next;
            }

            while(secondCmpNode !=null) {
                finalNode.next = secondCmpNode;
                secondCmpNode = secondCmpNode.next;
            }

        }




        return finalNode;
    }


    /**
     *
     * 给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中，使得 num1 成为一个有序数组。
     *
     * 时间复杂度：O(m+n) 空间复杂度：O(m)
     *
     * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n。
     * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void mergeSortedArray(int[] nums1, int m, int[] nums2, int n) {

        int[] arr = new int[m];
        System.arraycopy(nums1,0,arr,0,m);

        int idx = 0,idx2 = 0,position = 0;
        while(idx < m && idx2 < n) {
            nums1[position++] = (arr[idx] < nums2[idx2]) ? arr[idx++] : nums2[idx2++];
        }

        // nums2已经遍历完
        if(idx < m) {
            System.arraycopy(arr,idx,nums1,position,m-idx);
        }

        // arr已经遍历完
        if(idx2 < n){
            System.arraycopy(nums2,idx2,nums1,position,n-idx2);
        }

        log.info("Result : {}" , Arrays.toString(nums1));



    }

    /**
     *
     * 给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中，使得 num1 成为一个有序数组。
     *
     * 时间复杂度：O(m+n) 空间复杂度：O(1)
     *
     * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n。
     * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void mergeSortedArray_Best(int[] nums1, int m, int[] nums2, int n) {

        int p1 = m - 1;
        int p2 = n - 1;
        int p = m + n - 1;

        while(p1 >=0 && p2 >= 0) {
            nums1[p--] = (nums1[p1] > nums2[p2]) ? nums1[p1--] : nums2[p2--];
        }

        System.arraycopy(nums2,0,nums1,0,p2 + 1);



        int[] arr = new int[m];
        System.arraycopy(nums1,0,arr,0,m);

        int idx = 0,idx2 = 0,position = 0;
        while(idx < m && idx2 < n) {
            nums1[position++] = (arr[idx] < nums2[idx2]) ? arr[idx++] : nums2[idx2++];
        }

        // nums2已经遍历完
        if(idx < m) {
            System.arraycopy(arr,idx,nums1,position,m-idx);
        }

        // arr已经遍历完
        if(idx2 < n){
            System.arraycopy(nums2,idx2,nums1,position,n-idx2);
        }

        log.info("Result : {}" , Arrays.toString(nums1));



    }


    public int climbStairs(int n) {

        // [x,y]   (x * 1) + (y * 2) = n
        // [x,y] 多少种组合方式


        int maxStep2 = n / 2; // [0,maxStep2]

        int x , y = 0;

        int count = 0;
        for(x = 0;x <= maxStep2;x++){

            y = (n - 2 * x);

            log.info("[x,y] -> [{},{}]" , x , y);

            if(x == 0 || y == 0) {
                count++;
            } else {
                // A(x+y,x+y) 排列、组合
                // TODO 包含重复元素的排列、组合
                int a_xy = 1;
                for(int idx = 1 ;idx <= (x + y) ;idx++) {
                    a_xy *= idx;
                }
                count += a_xy;
            }
        }

        return count;
    }

    @Test
    public void testMerge() {
        int threshold = 100;
        MockConfig mockConfig = MockConfig.newInstance();
        mockConfig.intRange(0,200);
        List<Integer> mockIntList = mock(new TypeReference<List<Integer>>(){},mockConfig);

        log.info("MockData : {}, element size : {}" , Arrays.toString(mockIntList.toArray()) , mockIntList.size());

        List<Integer> indexList = Lists.newArrayListWithCapacity(mockIntList.size());
        int summer=0, index=0, startPos = 0;
        for(int num : mockIntList){
            if(num > threshold){

                indexList.add(index);
                log.warn("Big element index : [{}] , num : {}" , index , num );

                if(summer > 0){
                    log.info("Break Merge element range : [{} - {}] , sum : {}" , startPos , (index-1) ,summer);
                    IntStream.range(startPos,index).forEach(i -> indexList.add(i));
                }

                startPos = (index + 1);//跳过当前元素、重新开始累加计算
                summer = 0;           //累加器清零
            }else {
                summer += num;
                if(summer > threshold){
                    log.info("Sum Merge element range : [{} - {}] , sum : {}" , startPos , (index-1) ,(summer-num));
                    IntStream.range(startPos,index).forEach(i -> indexList.add(i));
                    startPos = (index - 1);
                    summer -= num;
                }
            }
            index++;
        }

        if(summer > 0){
            log.warn("Left element range : [{} - {}] , sum : {}" , startPos , mockIntList.size() - 1 ,summer);
            IntStream.range(startPos,mockIntList.size()).forEach(i -> indexList.add(i));
        }

        log.info("Partition Index : {}" , Arrays.toString(indexList.toArray()));
        Assert.assertTrue(indexList.size() == mockIntList.size());


    }

    @Test
    public void testMerge2() {

        MockConfig mockConfig = MockConfig.newInstance();
        mockConfig.intRange(0,200);
        mockConfig.sizeRange(1,10);
        int[] sourceIdList = JMockData.mock(new TypeReference<int[]>(){},mockConfig);
        log.info("MockData : {}, element size : {}" , Arrays.toString(sourceIdList) , sourceIdList.length);

        int threadHold = 100;
        int summer = 0;//累加值
        int startPos = 0;//累加起始坐标


        List<Integer> indexList = Lists.newArrayListWithCapacity(sourceIdList.length);

        for (int i = 0; i < sourceIdList.length; i++) {

            summer += sourceIdList[i];
            if (summer > threadHold) {
                if (startPos == i) {
                    System.out.println(startPos + " ----- " + startPos);//就一个元素
                } else {
                    i--;
                    System.out.println(startPos + " ----- " + i);//回退
                }
                startPos = i + 1;
                summer = 0;

            }
            if (summer == threadHold) {
                System.out.println(startPos + " ----- " + i);
                startPos = i + 1;
                summer = 0;
            }
        }
        if (startPos < (sourceIdList.length)) {
            System.out.println(startPos + " ---- " + (sourceIdList.length - 1));
        }
    }
    @Test
    public void testMerge3() {


        MockConfig mockConfig = MockConfig.newInstance();
        mockConfig.intRange(0,200);
        mockConfig.sizeRange(1,10);
        List<Integer> countList = mock(new TypeReference<List<Integer>>(){},mockConfig);

        log.info("MockData : {}, element size : {}" , Arrays.toString(countList.toArray()) , countList.size());

        int threshold = 100;
        int summer = 0;//累加值
        int startPos = 0;//累加起始坐标

        List<Range<Integer>> splitRangeList = Lists.newArrayList();

        for (int i = 0; i < countList.size(); i++) {
            summer += countList.get(i);
            if (summer > threshold) {
                if (startPos == i) {
                    System.out.println(startPos + " ----- " + startPos);//就一个元素
                    splitRangeList.add(Range.closed(startPos, startPos));
                } else {
                    i--;
                    System.out.println(startPos + " ----- " + i);//回退
                    splitRangeList.add(Range.closed(startPos, i));
                }
                startPos = i + 1;
                summer = 0;

            }

            if (summer == threshold) {
                System.out.println(startPos + " ----- " + i);
                splitRangeList.add(Range.closed(startPos, i));
                startPos = i + 1;
                summer = 0;
            }
        }

        if (startPos < (countList.size())) {
            splitRangeList.add(Range.closed(startPos, (countList.size() - 1)));
            System.out.println(startPos + " ---- " + (countList.size() - 1));
        }

        splitRangeList.stream().forEach(rng -> {
            log.warn("split range : [{} - {}]" , rng.lowerEndpoint() , rng.upperEndpoint());
        });





    }


    @Test
    public void testLinkedList() {
        LinkedNode head = new LinkedNode(-1);
        LinkedNode child = new LinkedNode(0);
        LinkedNode grandson = new LinkedNode(1);
        head.setNext(child);
        head.next.setNext(grandson);



        LinkedNode ref = head;
        ref.next = new LinkedNode(100);


        log.info(String.valueOf(ref));
        log.info(String.valueOf(head));

    }
    
    

    






}
