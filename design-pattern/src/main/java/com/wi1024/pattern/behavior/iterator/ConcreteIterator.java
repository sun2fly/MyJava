package com.wi1024.pattern.behavior.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:30
 */
public class ConcreteIterator implements Iterator {

    private List list = new ArrayList<>();

    private int cursor = 0;

    public ConcreteIterator(List list) {
        this.list = list;
    }

    @Override
    public Object next() {
        Object obj = null;
        if(this.hasNext()){
            obj = this.list.get(cursor++);
        }
        return obj;
    }

    @Override
    public boolean hasNext() {
        if(cursor == list.size()){
            return false;
        }
        return true;
    }
}
