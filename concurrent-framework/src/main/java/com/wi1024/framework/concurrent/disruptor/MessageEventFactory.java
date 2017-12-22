package com.wi1024.framework.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.wi1024.framework.concurrent.Message;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 09:12
 **/
@Slf4j
public class MessageEventFactory implements EventFactory<Message> {
    @Override
    public Message newInstance() {
        log.debug("Init message ... ");
        return new Message();
    }
}
