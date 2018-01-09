package com.wi1024.pattern.creat.builder;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 11:14
 **/
public class Director {

    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public void construct() {
        builder.buildPartA();
        builder.buildPartB();
        builder.buildPartC();
    }
}
