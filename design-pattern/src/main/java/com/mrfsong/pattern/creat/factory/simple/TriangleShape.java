package com.mrfsong.pattern.creat.factory.simple;

import com.mrfsong.pattern.LogUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:11
 **/
@Slf4j
public class TriangleShape implements Shape{
    @Override
    public void draw() {
        log.info(LogUtil.print("TriangleShape#draw()"));
    }
}
