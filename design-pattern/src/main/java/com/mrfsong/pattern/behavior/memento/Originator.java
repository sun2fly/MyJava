package com.mrfsong.pattern.behavior.memento;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:02
 */
public class Originator {

    private String stateA;
    private String stateB;
    private String stateC;

    public String getStateA() {
        return stateA;
    }

    public void setStateA(String stateA) {
        this.stateA = stateA;
    }

    public String getStateB() {
        return stateB;
    }

    public void setStateB(String stateB) {
        this.stateB = stateB;
    }

    public String getStateC() {
        return stateC;
    }

    public void setStateC(String stateC) {
        this.stateC = stateC;
    }

    public Memento createMemento(){
        return new Memento(BeanUtils.backupProp(this));
    }

    public void restoreMemento(Memento memento){
        BeanUtils.restoreProp(this, memento.getStateMap());
    }

    @Override
    public String toString() {
        return "Originator{" +
                "stateA='" + stateA + '\'' +
                ", stateB='" + stateB + '\'' +
                ", stateC='" + stateC + '\'' +
                '}';
    }
}
