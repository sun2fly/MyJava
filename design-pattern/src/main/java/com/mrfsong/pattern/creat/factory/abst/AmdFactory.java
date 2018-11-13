package com.mrfsong.pattern.creat.factory.abst;

import com.mrfsong.pattern.LogUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:30
 **/
@Slf4j
public class AmdFactory implements AbstractFactory {
    @Override
    public Cpu createCpu() {
        log.info(LogUtil.print("AmdFactory#createCpu"));
        return new Cpu(4);
    }

    @Override
    public Memory createMemory() {
        log.info(LogUtil.print("AmdFactory#createMemory"));
        return new Memory(1024);
    }
}
