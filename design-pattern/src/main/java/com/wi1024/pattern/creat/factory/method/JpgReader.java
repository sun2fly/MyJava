package com.wi1024.pattern.creat.factory.method;

import com.wi1024.pattern.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:14
 **/
@Slf4j
public class JpgReader implements Reader {
    @Override
    public void read() {
        log.info(LogUtil.print("JpgReader#reader()"));
    }
}
