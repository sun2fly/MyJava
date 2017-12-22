package com.wi1024.framework.concurrent.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.wi1024.framework.concurrent.Message;

import java.util.Date;

/**
 * 消息发送端
 *
 * @author songfei@xbniao.com
 * @create 2017/12/21 16:33
 **/
public class MessageEventProducer {

    private final RingBuffer<Message> ringBuffer;

    public MessageEventProducer(RingBuffer<Message> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }


    public void onData(long id , String content , Date date ) {
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
