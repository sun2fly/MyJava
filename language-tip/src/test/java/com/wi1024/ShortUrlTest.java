package com.wi1024;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.stream.Stream;

/**
 * <p>
 * ${DESCRIPTION}
 * </P>
 *
 * @Author songfei20
 * @Date 2018/9/15 17:10
 * @Version 1.0
 */
@Slf4j
public class ShortUrlTest {

    private final  String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    public  String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private  String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public  String MD5Encode(String origin) {
        String resultString = null;
        try {

            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");

            resultString.trim();

            resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception ex) {
        }
        return resultString;
    }



    public  String[] shortText(String string) {
        String key = "XuLiang";                 //自定义生成MD5加密字符串前的混合KEY
        String[] chars = new String[]{          //要使用生成URL的字符
                "a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p",
                "q", "r", "s", "t", "u", "v", "w", "x",
                "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D",
                "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"
        };

        String hex = MD5Encode(key + string);
        int hexLen = hex.length();
        int subHexLen = hexLen / 8;
        String[] ShortStr = new String[4];

        for (int i = 0; i < subHexLen; i++) {
            String outChars = "";
            int j = i + 1;
            String subHex = hex.substring(i * 8, j * 8);
            long idx = 0x3FFFFFFF & Long.valueOf(subHex, 16);//此处为了截取“低”30位

            for (int k = 0; k < 6; k++) {
                int index = (int) (0x3D & idx);//此处为了截取62位，等同于 idx
                outChars += chars[index];
                idx = idx >> 5;
            }
            ShortStr[i] = outChars;
        }

        return ShortStr;
    }


    @Test
    public void testShortUrl(){
        /**
         *  算法流程：
         *  1. 将长网址用md5算法生成32位签名串，分为4段,，每段8个字符；
         *  2. 对这4段循环处理，取每段的8个字符, 将他看成16进制字符串与0x3fffffff(30位1)的位与操作，超过30位的忽略处理；
         *  3. 将每段得到的这30位又分成6段，每5位的数字作为字母表的索引取得特定字符，依次进行获得6位字符串；
         *  4. 这样一个md5字符串可以获得4个6位串，取里面的任意一个就可作为这个长url的短url地址。
         *
         */
        String URL_STR = "http://blog.wi1024.com/2018/02/03/freelancer/";
        String[] arr = shortText(URL_STR);
        Stream.of(arr).forEach(val -> log.info(val));
    }

}
