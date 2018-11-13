package com.mrfsong.pattern.behavior.mediator;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 16:18
 **/
public abstract class AbstractMediator {

    protected AbstractColleague colleagueA;
    protected AbstractColleague colleagueB;

    public AbstractMediator(AbstractColleague colleagueA, AbstractColleague colleagueB) {
        this.colleagueA = colleagueA;
        this.colleagueB = colleagueB;
    }

    public abstract void AaffectB();
    public abstract void BaffectA();
}
