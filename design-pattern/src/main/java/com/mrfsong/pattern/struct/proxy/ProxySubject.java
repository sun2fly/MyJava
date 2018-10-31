package com.mrfsong.pattern.struct.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 09:39
 **/
@Slf4j
public class ProxySubject implements ISubject {

    private ISubject subject;

    public ProxySubject() {
        this.subject = new ConcreteSubject();
    }

    @Override
    public void action() {
        preAction();
        this.subject.action();
        postAction();
    }


    private void preAction(){
        log.info("========== ProxySubject#preAction() ==========");
    }

    private void postAction() {
        log.info("========== ProxySubject#postAction() ==========");
    }
}
