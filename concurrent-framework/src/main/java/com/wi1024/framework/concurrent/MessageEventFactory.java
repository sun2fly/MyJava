package com.wi1024.framework.concurrent;

import com.lmax.disruptor.EventFactory;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 09:12
 **/
public class MessageEventFactory implements EventFactory<Message> {
    @Override
    public Message newInstance() {
        return new Message();
    }
}
