package com.mrfsong.framework.concurrent.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.mrfsong.framework.concurrent.MessageEvent;
import jodd.typeconverter.Convert;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 使用EventTranslatorVararg回调方式创建Producer
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 17:38
 **/
@Slf4j
public class MessageEventProducerWithTranslator {

    private EventTranslatorVararg eventTranslatorVararg = new EventTranslatorVararg<MessageEvent>() {
        @Override
        public void translateTo(MessageEvent event, long sequence, Object... args) {
            event.setId(Convert.toLong(args[0]));
            event.setContent(Convert.toString(args[1]));
            event.setCreateAt(Convert.toDate(args[2]));
        }
    };

    private String name ;
    private final RingBuffer<MessageEvent> ringBuffer;

    public MessageEventProducerWithTranslator(String name , RingBuffer<MessageEvent> ringBuffer) {
        this.name = name ;
        this.ringBuffer = ringBuffer;
    }

    public void onData(long id , String content , Date date ) {
        log.debug("MessageEventProducerWithTranslator Name : {}" , this.name);
        ringBuffer.publishEvent(eventTranslatorVararg , id , content , date);
    }
}
