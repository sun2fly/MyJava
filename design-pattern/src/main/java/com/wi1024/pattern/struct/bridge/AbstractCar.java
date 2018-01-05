package com.wi1024.pattern.struct.bridge;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 15:19
 **/
public abstract class AbstractCar {
    protected Transmission transmission;

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public abstract void run();
}
