package com.wi1024.pattern.behavior.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:44
 */
public class ConcreteAggregate implements Aggregate {

    private List list = new ArrayList<>();

    @Override
    public void add(Object object) {
        list.add(object);
    }

    @Override
    public void remove(Object object) {
        list.remove(object);
    }

    @Override
    public Iterator iterator() {
        return new ConcreteIterator(list);
    }
}
