package com.wi1024.pattern.behavior.memento;

import java.util.HashMap;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:06
 */
public class Caretaker {

    private Map<String, Memento> memMap = new HashMap<String, Memento>();

    public Memento getMemento(String index) {
        return memMap.get(index);
    }

    public void setMemento(String index , Memento memento ) {
        memMap.put(index, memento);
    }

}
