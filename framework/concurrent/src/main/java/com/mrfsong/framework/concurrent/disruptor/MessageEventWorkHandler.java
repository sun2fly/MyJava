package com.mrfsong.framework.concurrent.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.mrfsong.framework.concurrent.MessageEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/22 13:17
 **/
@Slf4j
public class MessageEventWorkHandler implements WorkHandler<MessageEvent> {

    private String name;

    public MessageEventWorkHandler(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(MessageEvent event) throws Exception {
        log.info("MessageEventWorkHandler#Name : {} , event : {}" , name , event.toString());
    }
}
