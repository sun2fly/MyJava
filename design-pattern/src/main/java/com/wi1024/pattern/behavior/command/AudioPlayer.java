package com.wi1024.pattern.behavior.command;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:26
 **/
@Slf4j
public class AudioPlayer {

    public void play(){
        log.info("Play ... ");
    }
    public void rewind(){
        log.info("Rewind ... ");
    }
    public void stop(){
        log.info("Stop ... ");
    }
}
