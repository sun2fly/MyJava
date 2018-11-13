package com.mrfsong.pattern.struct.flyweight;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 16:36
 **/
@Slf4j
public class ConcreteFlyWeight implements FlyWeight {

    private String name;

    public ConcreteFlyWeight(String name) {
        this.name = name;
    }

    @Override
    public void action(String externalState) {
        log.info("========== name:{} , outerState:{}" , this.name , externalState);
    }
}
