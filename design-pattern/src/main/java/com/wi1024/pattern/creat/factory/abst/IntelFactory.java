package com.wi1024.pattern.creat.factory.abst;

import com.wi1024.pattern.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:31
 **/
@Slf4j
public class IntelFactory implements AbstractFactory {
    @Override
    public Cpu createCpu() {
        log.info(LogUtil.print("IntelFactory#createMemory"));
        return new Cpu(8);
    }

    @Override
    public Memory createMemory() {
        log.info(LogUtil.print("IntelFactory#createMemory"));
        return new Memory(10240);
    }
}
