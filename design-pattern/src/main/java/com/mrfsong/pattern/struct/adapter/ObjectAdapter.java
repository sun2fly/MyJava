package com.mrfsong.pattern.struct.adapter;

/**
 * 对象适配器
 *
 * @author lysongfei@gmail.com
 * @create 28/12/2017 22:39
 */
public class ObjectAdapter {

    private Adaptee adaptee;

    public ObjectAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    public void method1(){
        this.adaptee.method1();
    }

    public void method2(){

    }
}
