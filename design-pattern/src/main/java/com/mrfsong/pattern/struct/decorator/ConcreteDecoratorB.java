package com.mrfsong.pattern.struct.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 17:46
 **/
@Slf4j
public class ConcreteDecoratorB extends Decorator {
    public ConcreteDecoratorB(Component component) {
        super(component);
    }


    @Override
    public void operation() {
        super.operation();
        addOtherBehavior();

    }

    private void addOtherBehavior() {
        log.info("========== This is ConcreteDecoratorB#addOtherBehavior() ==========");
    }
}
