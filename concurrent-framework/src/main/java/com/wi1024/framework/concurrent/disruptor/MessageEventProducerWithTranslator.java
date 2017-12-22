package com.wi1024.framework.concurrent.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.wi1024.framework.concurrent.Message;

import java.util.Date;

import jodd.typeconverter.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用EventTranslatorVararg回调方式创建Producer
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 17:38
 **/
@Slf4j
public class MessageEventProducerWithTranslator {

    private EventTranslatorVararg eventTranslatorVararg = new EventTranslatorVararg<Message>() {
        @Override
        public void translateTo(Message event, long sequence, Object... args) {
            event.setId(Convert.toLong(args[0]));
            event.setContent(Convert.toString(args[1]));
            event.setCreateAt(Convert.toDate(args[2]));
        }
    };

    private String name ;
    private final RingBuffer<Message> ringBuffer;

    public MessageEventProducerWithTranslator(String name , RingBuffer<Message> ringBuffer) {
        this.name = name ;
        this.ringBuffer = ringBuffer;
    }

    public void onData(long id , String content , Date date ) {
        log.debug("MessageEventProducerWithTranslator Name : {}" , this.name);
        ringBuffer.publishEvent(eventTranslatorVararg , id , content , date);
    }
}
