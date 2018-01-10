package com.wi1024.pattern.creat.prototype;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

/**
 * 原型类（主要用户重写clone方法）
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 13:41
 **/
@Slf4j
class Prototype implements Cloneable {

    private ArrayList list = new ArrayList();

    public Prototype clone() {
        Prototype prototype = null;
        try {
            prototype = (Prototype)super.clone();
            prototype.list = (ArrayList) this.list.clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage() , e);
        }

        return prototype;
    }


}
