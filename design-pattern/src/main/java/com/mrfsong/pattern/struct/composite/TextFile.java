package com.mrfsong.pattern.struct.composite;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 16:37
 **/
@Slf4j
public class TextFile extends AbstractFile {
    public TextFile(String name) {
        super(name);
    }

    @Override
    public void display() {
        log.info("========== This is TextFile#{} ==========" , name);
    }
}
