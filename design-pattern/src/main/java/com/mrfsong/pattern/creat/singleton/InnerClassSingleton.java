package com.mrfsong.pattern.creat.singleton;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 10:30
 **/
public class InnerClassSingleton {

    private InnerClassSingleton(){}

    public static InnerClassSingleton getInstance() {
        return InnerClass.INSTANCE;
    }



    private static class InnerClass {
        private static  final InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }
}
