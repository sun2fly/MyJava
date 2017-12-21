package com.wi1024.framework.concurrent;

import com.lmax.disruptor.RingBuffer;

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


    public void send(Message message) {
        long seq = ringBuffer.next();
        try{
            Message event = ringBuffer.get(seq);
        }finally {

        }
    }
}
