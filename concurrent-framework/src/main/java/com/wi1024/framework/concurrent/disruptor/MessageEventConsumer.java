package com.wi1024.framework.concurrent.disruptor;


import com.lmax.disruptor.EventHandler;
import com.wi1024.framework.concurrent.Message;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/22 09:12
 **/
@Slf4j
public class MessageEventConsumer implements EventHandler<Message> {
    /**
     * 消费者名称
     */
    private String name ;

    public MessageEventConsumer(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(Message event, long sequence, boolean endOfBatch) throws Exception {
        log.info("Consumer name : {} , sequence : {} , event : {} , endOfBatch : {}" , this.name, sequence , event.toString() , endOfBatch);
    }
}
