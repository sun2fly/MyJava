package com.wi1024.framework.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.wi1024.framework.concurrent.MessageEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 09:12
 **/
@Slf4j
public class MessageEventFactory implements EventFactory<MessageEvent> {
    @Override
    public MessageEvent newInstance() {
        log.debug("Init message ... ");
        return new MessageEvent();
    }
}
