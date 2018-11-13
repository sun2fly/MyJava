package com.mrfsong.pattern.struct.bridge;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 15:20
 **/
@Slf4j
public class BMWCar extends AbstractCar {
    @Override
    public void run() {
        log.info("========== This is BMWCar#Run() ==========");
        transmission.gear();
    }
}
