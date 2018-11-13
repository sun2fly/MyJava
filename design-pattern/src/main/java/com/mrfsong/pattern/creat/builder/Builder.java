package com.mrfsong.pattern.creat.builder;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 11:13
 **/
public interface Builder {

    void buildPartA();
    void buildPartB();
    void buildPartC();
    Product getResult();

}
