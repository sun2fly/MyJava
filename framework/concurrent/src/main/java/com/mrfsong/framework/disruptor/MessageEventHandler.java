package com.mrfsong.framework.disruptor;

import com.lmax.disruptor.EventHandler;
import com.mrfsong.framework.MessageEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 09:19
 **/
@Slf4j
public class MessageEventHandler implements EventHandler<MessageEvent> {

    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("Event : {} , current sequence : {} , endOfBatch : {}" , event , sequence , endOfBatch);
    }
}
