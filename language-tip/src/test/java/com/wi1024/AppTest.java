package com.wi1024;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.MessageDigest;

/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest {

    private final String URL_STR = "http://blog.wi1024.com/2018/02/03/freelancer/";

    @Test
    public void shiftBit() {
        int result = 2 << 24;
        log.info(String.valueOf(result));
        log.info(String.valueOf(Long.valueOf("3FFFFFFF", 16)));

        int val = 32 & 31;//“与”运算，长度少的值会在“高位”补0参与运算
        log.info(String.valueOf(val));
        int mod1 = (int)(Long.valueOf("0000003D", 16) & 1073741822);
        int mod2 = (0x3D & 1073741822);
        int mod3 = 1073741822 / 62;
        int mod4 = 1073741822 % 62;

        log.info("{} - {} -{} - {}" , new Object[]{mod1 , mod2, mod3,mod4});
        long index = 0x0000003D & 63;
        log.info("index : {}" , index);
        int index2 = 62 & 60;
        log.info("index2 : {}" , index2);

       /* for(int i=0;i<62;i++){
            log.info(String.valueOf(61 & i));
        }

        log.info("=======================================");

        for(int i=0;i<62;i++){
            log.info(String.valueOf(i % 62));
        }*/

    }

    @Test
    public void md5() {
        byte[] toChapter = URL_STR.getBytes();
        //md5str用来保存字节数组转换成的十六进制数
        StringBuffer md5str = new StringBuffer();
        try {
            //得到一个实现特定摘要算法的消息摘要对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //将字节数组toChapter添加到待计算MD5值的字节序列中
            md5.update(toChapter);
            //计算字节序列的MD5值，返回16个字节的字节数组，保存到toChapterDigest
            byte[] toChapterDigest = md5.digest();

            log.info("MD5 byte length : {}", toChapterDigest.length);

            //每个8位的二进制字节用十六进制表示的话，需要两个字符，每个十六进制字符对应四位二进制位
            //故16个字节(128bit)转换后，变为了32个字符的字符串，将它们添加到md5str中

            int digital;
            for (int i = 0; i < toChapterDigest.length; i++) {
                digital = toChapterDigest[i];
                //这里字节类型赋值给int类型，会按符号位扩展的
                //如果字节的最高位是1，扩展为int时它的高位(9-32位)都会变为1
                if (digital < 0) {
                    //将8位字节之前的高位全变为0
                    digital += 256;
                }
                if (digital < 16) {
                    md5str.append("0");
                }
                //经过判断之后的操作，能保证digtal转换为十六进制字符的时候只得到两位
                md5str.append(Integer.toHexString(digital));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回十六进制字符串
        log.info(md5str.toString());
    }


}
