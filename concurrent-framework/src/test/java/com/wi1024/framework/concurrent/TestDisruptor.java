package com.wi1024.framework.concurrent;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.Sequencer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

/**
 * Disruptor Test
 *
 * @author songfei@xbniao.com
 * @create 2017/12/20 16:09
 **/
@Slf4j
public class TestDisruptor {

    private static final long TOTAL = 1000000;

    @Test
    public void singleThread() throws Exception  {
        RingBuffer<Message> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Message>() {
            @Override
            public Message newInstance() {
                return new Message();
            }
        } , getRingBufferSize(TOTAL) , new YieldingWaitStrategy());
        SequenceBarrier barrier = ringBuffer.newBarrier();
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=TOTAL;i++){
                    long index = ringBuffer.next();
                    ringBuffer.get(index).setId(Long.valueOf(i));
                    ringBuffer.get(index).setContent("Message index : " + i);
                    ringBuffer.get(index).setCreateAt(new Date());
                    ringBuffer.publish(index);
                    log.info("Produce message : {}" , i);
                }
            }
        });

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message;
                int readCount = 0;
                long readIndex = Sequencer.INITIAL_CURSOR_VALUE;
                while(readCount < TOTAL){
                    try {
                        long nextIndex = readIndex + 1;
                        long availableIndex = barrier.waitFor(nextIndex);
                        while(nextIndex <= availableIndex){
                            message = ringBuffer.get(nextIndex);
                            log.info("Consumer : [{}]" , message.toString());
                            readCount++;
                            nextIndex++;
                        }
                        readIndex = availableIndex;
                    } catch (AlertException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        long timeStart = System.currentTimeMillis();
        producer.start();
        consumer.start();
        consumer.join();
        producer.join();
        long timeEnd = System.currentTimeMillis();
        log.info("Total cost time : {} Seconds !" , (timeEnd - timeStart) / 1000);
    }

    @Test
    public void multiThread() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.MULTI, new YieldingWaitStrategy());
        EventHandler<Message> eventHandler = new MessageEventHandler();
        disruptor.handleEventsWith(eventHandler);
        disruptor.start();

        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        for(int i=0;i<TOTAL;i++){
            long index = ringBuffer.next();
            try{
                ringBuffer.get(index).setId(Long.valueOf(i));
                ringBuffer.get(index).setContent("Message index : " + i);
                ringBuffer.get(index).setCreateAt(new Date());
            }finally {
                ringBuffer.publish(index);
            }
        }

        Thread.sleep(1000 * 60 * 10);

    }

    public void multiProducerByTranslator() {

    }




    @Test
    public void shift() {
        log.info(String.valueOf(getRingBufferSize(TOTAL)));
    }


    private int getRingBufferSize(long num) {
        long s = 2 ;
        while(s < num){
            s <<= 1;
        }
        return Long.valueOf(s).intValue();
    }

}
