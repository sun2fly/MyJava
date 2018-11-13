package com.mrfsong.pattern.creat.singleton;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 10:29
 **/
public class StaticSingleton {

    private static final StaticSingleton INSTANCE = new StaticSingleton();

    private StaticSingleton() {}

    public static StaticSingleton getInstance() {
        return INSTANCE;
    }

}
