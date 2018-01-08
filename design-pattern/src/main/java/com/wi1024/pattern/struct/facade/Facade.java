package com.wi1024.pattern.struct.facade;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 11:23
 **/
@Slf4j
public class Facade extends AbstractFacade {

    private SubSystemA systemA = new SubSystemA();
    private SubSystemB systemB = new SubSystemB();
    private SubSystemC systemC = new SubSystemC();

    public void method() {
        systemA.methodA();
        systemB.methodB();
        systemC.methodC();
    }

    @Override
    protected void doSomething() {
        log.info("========== This is AbstractFacade#doSomething() ==========");
    }
}
