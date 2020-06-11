package com.mrfsong.common.util;

/**
 * <p>
 *      自定义元组
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/11 18:48
 */
public class Tuple3<T1,T2,T3> extends Tuple2<T1,T2>  {

    private T3 t3;
    public Tuple3(T1 t1, T2 t2,T3 t3) {
        super(t1, t2);
        this.t3 = t3;
    }

    public T3 _3() {
        return t3;
    }

    public void _3(T3 t3) {
        this.t3 = t3;
    }

    @Override
    public String toString() {
        return "Tuple3{" +
                "t1=" + _1() +
                ", t1=" + _2() +
                ", t3=" + t3 +
                '}';
    }
}

