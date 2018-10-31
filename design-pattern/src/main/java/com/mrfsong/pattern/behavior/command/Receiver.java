package com.mrfsong.pattern.behavior.command;

import lombok.extern.slf4j.Slf4j;

/**
 * 命令接收者
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 13:48
 **/
@Slf4j
public class Receiver {


    public void action () {
        log.info("========== 执行操作 ==========");
    }
}
