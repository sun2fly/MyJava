package com.wi1024.pattern.creat.factory.simple;

import com.wi1024.pattern.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:10
 **/
@Slf4j
public class CircleShape implements Shape{
    @Override
    public void draw() {
        log.info(LogUtil.print("CircleShape#draw()"));
    }
}
