package com.wi1024.pattern.behavior.visitor;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:53
 **/
@Slf4j
public class AConcreteElement extends Element {
    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void doSomething() {
        log.info("========== This is Element A ==========");
    }
}
