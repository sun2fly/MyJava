package com.mrfsong.pattern.behavior.memento;

import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:03
 */
public class Memento {

    private Map<String,Object> stateMap;

    public Memento(Map<String, Object> stateMap) {
        this.stateMap = stateMap;
    }

    public Map<String, Object> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<String, Object> stateMap) {
        this.stateMap = stateMap;
    }
}
