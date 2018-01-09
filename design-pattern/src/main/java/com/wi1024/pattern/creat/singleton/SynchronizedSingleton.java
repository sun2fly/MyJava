package com.wi1024.pattern.creat.singleton;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 10:27
 **/
public class SynchronizedSingleton {

    private static volatile SynchronizedSingleton INSTANCE;

    private SynchronizedSingleton() {}

    public static SynchronizedSingleton getInstance() {
        if(INSTANCE == null ){
            synchronized (SynchronizedSingleton.class){
                if(INSTANCE == null) {
                    INSTANCE = new SynchronizedSingleton();
                }
            }
        }
        return INSTANCE;
    }
}
