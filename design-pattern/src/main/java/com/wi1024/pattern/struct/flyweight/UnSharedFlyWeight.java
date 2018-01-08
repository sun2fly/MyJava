package com.wi1024.pattern.struct.flyweight;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 16:40
 **/
@Slf4j
public class UnSharedFlyWeight implements FlyWeight {

    private String name;

    @Override
    public void action(String externalState) {
        log.info("========== name:{} , outerState:{}" , this.name , externalState);
    }
}
