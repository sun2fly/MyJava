package com.mrfsong.framework.concurrent.disruptor;


import com.lmax.disruptor.EventHandler;
import com.mrfsong.framework.concurrent.MessageEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/22 09:12
 **/
@Slf4j
public class MessageEventConsumer implements EventHandler<MessageEvent> {
    /**
     * 消费者名称
     */
    private String name ;

    public MessageEventConsumer(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("Consumer name : {} , sequence : {} , event : {} , endOfBatch : {}" , this.name, sequence , event.toString() , endOfBatch);
    }
}
