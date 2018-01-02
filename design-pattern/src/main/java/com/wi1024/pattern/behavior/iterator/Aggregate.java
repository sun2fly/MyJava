package com.wi1024.pattern.behavior.iterator;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:43
 */
public interface Aggregate {

    void add(Object object);
    void remove(Object object);
    Iterator iterator();
}
