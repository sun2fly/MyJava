package com.mrfsong.pattern.behavior.strategy;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:56
 */
public class Context {

    private IStrategy strategy;

    public Context(IStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute(){
        strategy.doSomething();
    }

}
