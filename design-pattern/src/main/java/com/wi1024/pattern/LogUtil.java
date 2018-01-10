package com.wi1024.pattern;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 13:38
 **/
public class LogUtil {

    public static String print(String s) {
        StringBuilder stringBuilder = new StringBuilder("========== ");
        stringBuilder.append(s);
        stringBuilder.append(" ==========");
        return stringBuilder.toString();
    }

}
