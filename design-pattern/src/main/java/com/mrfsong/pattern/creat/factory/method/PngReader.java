package com.mrfsong.pattern.creat.factory.method;

import com.mrfsong.pattern.LogUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:15
 **/
@Slf4j
public class PngReader implements Reader {
    @Override
    public void read() {
        log.info(LogUtil.print("JpgReader#reader()"));
    }
}
