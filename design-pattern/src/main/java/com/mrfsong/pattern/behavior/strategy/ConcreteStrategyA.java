package com.mrfsong.pattern.behavior.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:54
 */
@Slf4j
public class ConcreteStrategyA implements IStrategy {
    @Override
    public void doSomething() {
        log.info("========== ConcreteStrategyA#doSomething ==========");
    }
}
