package com.mrfsong.pattern.struct.bridge;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 15:18
 **/
@Slf4j
public class AutoCar implements Transmission {
    @Override
    public void gear() {
        log.info("========== This is AutoCar#Gear ==========");
    }
}
