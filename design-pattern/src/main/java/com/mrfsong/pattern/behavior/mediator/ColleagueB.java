package com.mrfsong.pattern.behavior.mediator;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 16:17
 **/
public class ColleagueB extends AbstractColleague {
    @Override
    public void setNumber(int number, AbstractMediator am) {
        this.number = number;
        am.BaffectA();
    }
}
