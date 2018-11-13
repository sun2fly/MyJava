package com.mrfsong.pattern.behavior.visitor;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:51
 **/
public abstract class Element {
    public abstract void accept(IVisitor visitor);
    public abstract void doSomething();
}
