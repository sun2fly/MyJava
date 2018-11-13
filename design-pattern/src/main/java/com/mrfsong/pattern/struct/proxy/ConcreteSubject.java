package com.mrfsong.pattern.struct.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 17:10
 **/
@Slf4j
public class ConcreteSubject implements ISubject {
    @Override
    public void action() {
        log.info("========== ConcreteSubject#action() ==========");
    }
}
