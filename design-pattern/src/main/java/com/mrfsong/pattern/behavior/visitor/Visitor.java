package com.mrfsong.pattern.behavior.visitor;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:55
 **/
public class Visitor implements IVisitor{
    @Override
    public void visit(AConcreteElement element) {
        element.doSomething();
    }

    @Override
    public void visit(BConcreteElement element) {
        element.doSomething();
    }
}
