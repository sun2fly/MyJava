package com.mrfsong.pattern.behavior.mediator;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 16:19
 **/
public class Mediator extends AbstractMediator{

    public Mediator(AbstractColleague colleagueA, AbstractColleague colleagueB) {
        super(colleagueA, colleagueB);
    }

    @Override
    public void AaffectB() {
        int number = colleagueA.getNumber();
        colleagueB.setNumber(number * 100);
    }

    @Override
    public void BaffectA() {
        int number = colleagueB.getNumber();
        colleagueA.setNumber(number / 100);

    }
}
