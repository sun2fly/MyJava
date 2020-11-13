package com.mrfsong.common.util;

import java.util.Arrays;

public class IpUtil {

    /**
     * 把字符串IP转换成long
     *
     * @param ipStr 字符串IP
     * @return IP对应的long值
     */
    public static long ipv4ToLong(String ipStr) {
        String[] ip = ipStr.split("\\.");
        return (Long.valueOf(ip[0]) << 24) + (Long.valueOf(ip[1]) << 16)
                + (Long.valueOf(ip[2]) << 8) + Long.valueOf(ip[3]);
    }

    /**
     * 把IP的long值转换成字符串
     *
     * @param ipLong IP的long值
     * @return long值对应的字符串
     */
    public static String longToIpv4(long ipLong) {
        StringBuilder ip = new StringBuilder();
        ip.append(ipLong >>> 24).append(".");
        ip.append((ipLong >>> 16) & 0xFF).append(".");
        ip.append((ipLong >>> 8) & 0xFF).append(".");
        ip.append(ipLong & 0xFF);
        return ip.toString();
    }


    /**
     * 将 IPv6 地址转为 long 数组，只支持冒分十六进制表示法
     */
    public static long[] ipv6ToLong(String ipv6_string) {
        if (ipv6_string == null || ipv6_string.isEmpty()) {
            throw new IllegalArgumentException("ipv6_string cannot be null.");
        }

        String[] ipSlices = ipv6_string.split(":");
        if (ipSlices.length != 8) {
            throw new IllegalArgumentException(ipv6_string + " is not an ipv6 address.");
        }

        long[] ipv6 = new long[2];
        for (int i = 0; i < 8; i++) {
            String slice = ipSlices[i];

            // 以 16 进制解析
            long num = Long.parseLong(slice, 16);

            // 每组 16 位
            long right = num << (16 * i);

            // 每个 long 保存四组，i >> 2 等于 i / 4
            int length=i>>2;//即int length=i / 4;
            ipv6[length] = ipv6[length] | right;
        }

        return ipv6;
    }


    /**
     * 将 long 数组转为冒分十六进制表示法的 IPv6 地址
     */
    public static String longToIpv6(long[] numbers) {
        if (numbers == null || numbers.length != 2) {
            throw new IllegalArgumentException(Arrays.toString(numbers) + " is not an IPv6 address.");
        }

        StringBuilder sb = new StringBuilder(32);
        for (long numSlice : numbers) {
            // 每个 long 保存四组
            for (int j = 0; j < 4; j++) {
                // 取最后 16 位
                long current = numSlice & 0xFFFF;
                sb.append(Long.toString(current, 16)).append(":");
                // 右移 16 位，即去除掉已经处理过的 16 位
                numSlice >>= 16;
            }
        }

        // 去掉最后的 :
        return sb.substring(0, sb.length() - 1);
    }
}