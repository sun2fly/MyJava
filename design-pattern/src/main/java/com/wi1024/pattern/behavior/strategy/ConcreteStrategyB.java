package com.wi1024.pattern.behavior.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:55
 */
@Slf4j
public class ConcreteStrategyB implements IStrategy {
    @Override
    public void doSomething() {
        log.info("========== ConcreteStrategyB#doSomething ==========");
    }
}
