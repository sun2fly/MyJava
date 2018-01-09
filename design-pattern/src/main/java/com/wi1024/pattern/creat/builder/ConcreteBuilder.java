package com.wi1024.pattern.creat.builder;

import com.wi1024.pattern.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 11:13
 **/
@Slf4j
public class ConcreteBuilder implements Builder {

    Part partA , partB , partC ;
    private Product product = new Product();

    public Product getProduct() {
        return product;
    }


    @Override
    public void buildPartA() {
        log.info(LogUtil.format("ConcreteBuilder#buildPartA()"));
        product.setPartA("Concrete PartA");
    }

    @Override
    public void buildPartB() {
        log.info(LogUtil.format("ConcreteBuilder#buildPartB()"));
        product.setPartB("Concrete PartB");
    }

    @Override
    public void buildPartC() {
        log.info(LogUtil.format("ConcreteBuilder#buildPartC()"));
        product.setPartC("Concrete PartC");
    }

    @Override
    public Product getResult() {
        log.info(LogUtil.format("ConcreteBuilder#getResult()"));
        //TODO The product info
        return product;
    }
}
