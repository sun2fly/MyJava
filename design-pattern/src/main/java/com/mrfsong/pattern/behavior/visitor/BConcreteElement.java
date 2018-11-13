package com.mrfsong.pattern.behavior.visitor;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:54
 **/
@Slf4j
public class BConcreteElement extends Element {
    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void doSomething() {
        log.info("========== This is Element B ==========");
    }
}
