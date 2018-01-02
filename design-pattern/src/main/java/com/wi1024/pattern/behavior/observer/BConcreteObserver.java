package com.wi1024.pattern.behavior.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 15:56
 **/
@Slf4j
public class BConcreteObserver implements Observer {
    @Override
    public void update() {
        log.info("========== BConcreteObserver#update ==========");
    }
}
