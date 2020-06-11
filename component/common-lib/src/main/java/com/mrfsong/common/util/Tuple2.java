package com.mrfsong.common.util;

import java.io.Serializable;

/**
 * <p>
 *      自定义元组
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/11 18:48
 */
public class Tuple2<T1,T2> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T1 t1;
    private T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 _1() {
        return t1;
    }

    public void _1(T1 t1) {
        this.t1 = t1;
    }

    public T2 _2() {
        return t2;
    }

    public void _2(T2 t2) {
        this.t2 = t2;
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "t1=" + t1 +
                ", t2=" + t2 +
                '}';
    }
}
