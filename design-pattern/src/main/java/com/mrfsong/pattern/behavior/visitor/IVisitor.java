package com.mrfsong.pattern.behavior.visitor;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:51
 **/
public interface IVisitor {

    void visit(AConcreteElement element);

    void visit(BConcreteElement element);


}
