package com.wi1024.pattern.struct.composite;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 16:38
 **/
@Slf4j
public class ImageFile extends AbstractFile {
    public ImageFile(String name) {
        super(name);
    }

    @Override
    public void display() {
        log.info("========== This is ImageFile#{} ==========" , name);
    }
}
