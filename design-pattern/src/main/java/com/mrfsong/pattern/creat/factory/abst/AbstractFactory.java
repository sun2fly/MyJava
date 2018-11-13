package com.mrfsong.pattern.creat.factory.abst;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:29
 **/
public interface AbstractFactory {

    Cpu createCpu();
    Memory createMemory();

}
