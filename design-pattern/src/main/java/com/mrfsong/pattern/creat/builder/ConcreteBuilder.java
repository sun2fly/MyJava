package com.mrfsong.pattern.creat.builder;

import com.mrfsong.pattern.LogUtil;
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
        log.info(LogUtil.print("ConcreteBuilder#buildPartA()"));
        product.setPartA("Concrete PartA");
    }

    @Override
    public void buildPartB() {
        log.info(LogUtil.print("ConcreteBuilder#buildPartB()"));
        product.setPartB("Concrete PartB");
    }

    @Override
    public void buildPartC() {
        log.info(LogUtil.print("ConcreteBuilder#buildPartC()"));
        product.setPartC("Concrete PartC");
    }

    @Override
    public Product getResult() {
        log.info(LogUtil.print("ConcreteBuilder#getResult()"));
        //TODO The product info
        return product;
    }
}
