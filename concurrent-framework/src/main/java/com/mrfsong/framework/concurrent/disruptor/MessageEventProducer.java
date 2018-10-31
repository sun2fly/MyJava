package com.mrfsong.framework.concurrent.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.mrfsong.framework.concurrent.MessageEvent;

import java.util.Date;

/**
 * 消息发送端
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 16:33
 **/
public class MessageEventProducer {

    private final RingBuffer<MessageEvent> ringBuffer;

    public MessageEventProducer(RingBuffer<MessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }


    public void onData(long id , String content , Date date ) {
        long seq = ringBuffer.next();
        try{
            MessageEvent event = ringBuffer.get(seq);
            event.setId(id);
            event.setContent(content);
            event.setCreateAt(date);
        }finally {
            ringBuffer.publish(seq);
        }
    }
}
