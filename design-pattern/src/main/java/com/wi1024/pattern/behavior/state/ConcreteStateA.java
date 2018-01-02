package com.wi1024.pattern.behavior.state;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:41
 */
@Slf4j
public class ConcreteStateA implements State {
    @Override
    public void handle(String sampleParameter) {
        log.info("========== ConcreteStateA#handle:{} ==========" , sampleParameter);
    }
}
