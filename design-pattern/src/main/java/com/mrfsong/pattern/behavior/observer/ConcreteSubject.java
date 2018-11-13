package com.mrfsong.pattern.behavior.observer;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 15:54
 **/
@Slf4j
public class ConcreteSubject extends Subject {
    @Override
    public void doSomething() {
        log.info("========== Event happen ==========");
        this.notifyObserver();
    }
}
