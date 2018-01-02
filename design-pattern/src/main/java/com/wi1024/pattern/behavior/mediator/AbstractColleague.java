package com.wi1024.pattern.behavior.mediator;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 16:16
 **/
public abstract class AbstractColleague {

    protected int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public abstract void setNumber(int number , AbstractMediator am);
}
