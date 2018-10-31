package com.mrfsong.pattern.behavior.observer;


import java.util.Vector;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 15:51
 **/
public abstract class Subject {

    private Vector<Observer> observerVector = new Vector<>();

    public void addObserver(Observer observer) {
        this.observerVector.add(observer);
    }

    public void delObserver(Observer observer){
        this.observerVector.remove(observer);
    }

    protected void notifyObserver() {
        for(Observer observer : observerVector){
            observer.update();
        }
    }

    public abstract void doSomething();



}
