package com.wi1024.pattern.creat.prototype;

import lombok.extern.slf4j.Slf4j;

/**
 * 原型实现类，包含具体的方法逻辑
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:04
 **/
@Slf4j
public class ConcretePrototype extends Prototype {

    public void show() {
        log.info("========== Prototype Realize Class ==========");
    }

}
