package com.wi1024.framework.concurrent;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;

import java.util.Date;

import jodd.typeconverter.Convert;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 17:38
 **/
public class MessageEventProducerWithTranslator {

    private EventTranslatorVararg eventTranslatorVararg = new EventTranslatorVararg<Message>() {
        @Override
        public void translateTo(Message event, long sequence, Object... args) {
            event.setId(Convert.toLong(args[0]));
            event.setContent(Convert.toString(args[1]));
            event.setCreateAt(Convert.toDate(args[2]));

        }
    };

    private final RingBuffer<Message> ringBuffer;

    public MessageEventProducerWithTranslator(RingBuffer<Message> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void send(long id , String content , Date date ) {
        long seq = ringBuffer.next();
        try{
            Message event = ringBuffer.get(seq);
            event.setId(id);
            event.setContent(content);
            event.setCreateAt(date);
        }finally {
            ringBuffer.publish(seq);
        }
    }
}
