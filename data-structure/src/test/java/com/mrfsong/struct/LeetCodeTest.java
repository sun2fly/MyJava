package com.mrfsong.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *      leetcode pratice
 * </P>
 *
 * @Author songfei20
 * @Date 2019/11/21
 */
@Slf4j
public class LeetCodeTest {


    @Test
    public void run() {
//        int maxLength = lengthOfLongestSubstring("abcdefabca");


//        double midVal = findMedianSortedArrays(new int[]{1,2} , new int[]{3,4});
//        log.info("findMedianSortedArrays values : {}" , String.valueOf(midVal));

        myAtoi("  -123&44 ");
        /*String s = "-012344";
        char[] chs = s.toCharArray();
        char[] tgt = new char[chs.length];
        System.arraycopy(chs, 0, tgt, 0, chs.length);*/
    }


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


    private boolean passCheck (char[] chs , int start  , int curr) {

        for(int i=start ; i<curr ;i++){

            if(chs[i] == chs[curr]){
                return false;
            }
        }

        //循环至最后元素、仍未重复，则取最大长度
        if(curr == chs.length - 1){
            return false;
        }

        log.info("sub string ---> {}" , String.valueOf(chs,start , (curr-start + 1)));

        return true;
    }

}
