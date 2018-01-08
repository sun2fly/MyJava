package com.wi1024.pattern.struct;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.struct.decorator.Component;
import com.wi1024.pattern.struct.decorator.ConcreteComponent;
import com.wi1024.pattern.struct.decorator.ConcreteDecoratorA;
import com.wi1024.pattern.struct.decorator.ConcreteDecoratorB;

import org.junit.Test;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 17:47
 **/
public class DecoratorPatternTest extends TestBase {

    @Test
    @Override
    public void exec() throws Exception {
        Component component , decoratorA;
        component = new ConcreteComponent();

        decoratorA = new ConcreteDecoratorA(component);
        decoratorA.operation();


        Component decoratorB = new ConcreteDecoratorB(component);
        decoratorB.operation();
    }
}
