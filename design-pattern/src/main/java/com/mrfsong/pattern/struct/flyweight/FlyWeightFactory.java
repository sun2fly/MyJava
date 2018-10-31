package com.mrfsong.pattern.struct.flyweight;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 16:38
 **/
@Slf4j
public class FlyWeightFactory {

    private static ConcurrentHashMap<String, FlyWeight> allFlyWeight = new ConcurrentHashMap<String, FlyWeight>();

    public static FlyWeight getFlyWeight(String name) {
        if(allFlyWeight.get(name) == null){
            synchronized (allFlyWeight) {
                log.info("========== FlyWeightFactory#getFlyWeight name:{} doesn't exist  ==========" , name);
                FlyWeight flyWeight = new ConcreteFlyWeight(name);
                allFlyWeight.put(name , flyWeight);
                log.info("========== FlyWeightFactory#getFlyWeight create name:{}  ==========" , name);
            }
        }
        return allFlyWeight.get(name);
    }



}
