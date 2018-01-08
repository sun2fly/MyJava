package com.wi1024.pattern.struct;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.struct.bridge.AbstractCar;
import com.wi1024.pattern.struct.bridge.AutoCar;
import com.wi1024.pattern.struct.bridge.BMWCar;
import com.wi1024.pattern.struct.bridge.BenzCar;
import com.wi1024.pattern.struct.bridge.ManualCar;
import com.wi1024.pattern.struct.bridge.Transmission;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     桥接模式是一种很实用的结构型设计模式，如果软件系统中某个类存在两个独立变化的维度，通过该模式可以将这两个维度分离出来，使两者可以独立扩展，让系统更加符合“单一职责原则”。
 *     与多层继承方案不同，它将两个独立变化的维度设计为两个独立的继承等级结构，并且在抽象层建立一个抽象关联，该关联关系类似一条连接两个独立继承结构的桥，故名桥接模式
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 15:12
 **/
@Slf4j
public class BridgePatternTest extends TestBase {

    @Test
    public void exec() throws Exception {
        Transmission auto = new AutoCar();
        AbstractCar bmw = new BMWCar();
        bmw.setTransmission(auto);
        bmw.run();

        Transmission manual = new ManualCar();
        AbstractCar benz = new BenzCar();
        benz.setTransmission(manual);
        benz.run();


    }

}
