package com.mrfsong.pattern.struct.composite;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 16:39
 **/
@Slf4j
public class VideoFile extends AbstractFile {
    public VideoFile(String name) {
        super(name);
    }

    @Override
    public void display() {
        log.info("========== This is VideoFile#{} ==========" , name);
    }
}
