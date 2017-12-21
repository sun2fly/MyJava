package com.wi1024.framework.concurrent;

import com.lmax.disruptor.EventHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 09:19
 **/
@Slf4j
public class MessageEventHandler implements EventHandler<Message> {

    @Override
    public void onEvent(Message event, long sequence, boolean endOfBatch) throws Exception {
        log.info("Event : {} , current sequence : {} , endOfBatch : {}" , event , sequence , endOfBatch);
    }
}
