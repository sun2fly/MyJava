package com.wi1024.framework.concurrent;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.Sequencer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.wi1024.framework.concurrent.disruptor.MessageEventConsumer;
import com.wi1024.framework.concurrent.disruptor.MessageEventFactory;
import com.wi1024.framework.concurrent.disruptor.MessageEventHandler;
import com.wi1024.framework.concurrent.disruptor.MessageEventProducer;
import com.wi1024.framework.concurrent.disruptor.MessageEventProducerWithTranslator;

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

    //发送消息总数量
    private static final long TOTAL = 10;
    private static final long ringBufferSize = 1 << 3;

    /**
     * @throws Exception
     */
    @Test
    public void singleWithApi() throws Exception  {
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
    public void multiWithApi() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.SINGLE, new YieldingWaitStrategy());
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

        disruptor.shutdown();


    }

    @Test
    public void withSingleProducer() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.SINGLE, new YieldingWaitStrategy());
        disruptor.handleEventsWith(new MessageEventConsumer("消费者-1"));
        disruptor.start();

        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        MessageEventProducer producer = new MessageEventProducer(ringBuffer);
        for(int i=0;i<TOTAL;i++){
            producer.onData(i , "Message index : " + i , new Date());
        }
        Thread.sleep(1000);
        disruptor.shutdown();

    }

    @Test
    public void singleProducerWithTranslator() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.SINGLE, new YieldingWaitStrategy());
        disruptor.handleEventsWith(new MessageEventConsumer("消费者-1"));
        disruptor.start();

        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        MessageEventProducerWithTranslator producerWithTranslator = new MessageEventProducerWithTranslator("Producer-1",ringBuffer);
        for(int i=0;i<TOTAL;i++){
            producerWithTranslator.onData(i , "Message index : " + i , new Date());
        }
        Thread.sleep(1000);
        disruptor.shutdown();
    }

    /**
        一个生产者，3个消费者，其中前面2个消费者完成后第3个消费者才可以消费
        也即使说当前面2个消费者把所有的RingBuffer占领完成，同时都消费完成后才会有第3个消费者的消费
        当发布的事件数量大于RingBuffer的大小的时候，在第3个消费者消费完RingBuffer大小的时候前面2个消费者才能继续消费，序号递增的
     * @throws Exception
     */
    @Test
    public void withMultiConsumer() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.SINGLE, new YieldingWaitStrategy());

        EventHandlerGroup<Message> eventHandlerGroup = disruptor.handleEventsWith(new MessageEventConsumer("消费者-1") , new MessageEventConsumer("消费者-2") );
        eventHandlerGroup.then(new MessageEventConsumer("消费者-3"));
        disruptor.start();

        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        MessageEventProducerWithTranslator producerWithTranslator = new MessageEventProducerWithTranslator("Producer-1",ringBuffer);
        for(int i=0;i<TOTAL;i++){
            producerWithTranslator.onData(i , "Message index : " + i , new Date());
        }
        Thread.sleep(1000);
        disruptor.shutdown();
    }


    /**
        一个生产者，多个消费者，有2条支线，其中消费者1和消费者3在同一条支线上，
        消费者2和消费者4在同一条支线上，消费者5是消费者3和消费者4的终点消费者
        这样的消费将会在消费者1和消费者2把所有的RingBuffer大小消费完成后才会执行消费者3和消费者4
        在消费者3和消费者4把RingBuffer大小消费完成后才会执行消费者5
        消费者5消费完RingBuffer大小后又按照上面的顺序来消费
        如果剩余的生产数据比RingBuffer小，那么还是要依照顺序来
     * @throws Exception
     */
    @Test
    public void withMultiOrderConsumer() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.SINGLE, new YieldingWaitStrategy());

        MessageEventConsumer eventConsumer1 = new MessageEventConsumer("消费者-1");
        MessageEventConsumer eventConsumer2 = new MessageEventConsumer("消费者-2");
        MessageEventConsumer eventConsumer3 = new MessageEventConsumer("消费者-3");
        MessageEventConsumer eventConsumer4 = new MessageEventConsumer("消费者-4");
        MessageEventConsumer eventConsumer5 = new MessageEventConsumer("消费者-5");

        disruptor.handleEventsWith(eventConsumer1 , eventConsumer2);
        disruptor.after(eventConsumer1).handleEventsWith(eventConsumer3);
        disruptor.after(eventConsumer2).handleEventsWith(eventConsumer4);
        disruptor.after(eventConsumer3,eventConsumer4).handleEventsWith(eventConsumer5);
        disruptor.start();

        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        MessageEventProducerWithTranslator producerWithTranslator = new MessageEventProducerWithTranslator("Producer-1",ringBuffer);
        for(int i=0;i<TOTAL;i++){
            producerWithTranslator.onData(i , "Message index : " + i , new Date());
        }
        Thread.sleep(1000);
        disruptor.shutdown();
    }

    /**
         多个生产者，多个消费者，有2条消费者支线，其中消费者1和消费者3在同一条支线上，
         消费者2和消费者4在同一条支线上，消费者5是消费者3和消费者4的终点消费者
         这样的消费将会在消费者1和消费者2把所有的RingBuffer大小消费完成后才会执行消费者3和消费者4
         在消费者3和消费者4把RingBuffer大小消费完成后才会执行消费者5
         消费者5消费完RingBuffer大小后又按照上面的顺序来消费
         如果剩余的生产数据比RingBuffer小，那么还是要依照顺序来
         生产者只是多生产了数据
     * @throws Exception
     */
    @Test
    public void withMultiProducerConsumer() throws Exception {
        MessageEventFactory eventFactory = new MessageEventFactory();
        Disruptor<Message> disruptor = new Disruptor(eventFactory , getRingBufferSize(TOTAL) , Executors.defaultThreadFactory() , ProducerType.MULTI, new YieldingWaitStrategy());

        MessageEventConsumer eventConsumer1 = new MessageEventConsumer("消费者-1");
        MessageEventConsumer eventConsumer2 = new MessageEventConsumer("消费者-2");
        MessageEventConsumer eventConsumer3 = new MessageEventConsumer("消费者-3");
        MessageEventConsumer eventConsumer4 = new MessageEventConsumer("消费者-4");
        MessageEventConsumer eventConsumer5 = new MessageEventConsumer("消费者-5");

        disruptor.handleEventsWith(eventConsumer1 , eventConsumer2);
        disruptor.after(eventConsumer1).handleEventsWith(eventConsumer3);
        disruptor.after(eventConsumer2).handleEventsWith(eventConsumer4);
        disruptor.after(eventConsumer3,eventConsumer4).handleEventsWith(eventConsumer5);
        disruptor.start();

        RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
        MessageEventProducerWithTranslator producerWithTranslator1 = new MessageEventProducerWithTranslator("Producer-1",ringBuffer);
        MessageEventProducerWithTranslator producerWithTranslator2 = new MessageEventProducerWithTranslator("Producer-2",ringBuffer);
        MessageEventProducerWithTranslator producerWithTranslator3 = new MessageEventProducerWithTranslator("Producer-3",ringBuffer);

        for(int i=0;i<TOTAL;i++){
            producerWithTranslator1.onData(i , "Message-1 index : " + i , new Date());
            producerWithTranslator2.onData(i , "Message-2 index : " + i , new Date());
            producerWithTranslator3.onData(i , "Message-3 index : " + i , new Date());
        }
        Thread.sleep(1000);
        disruptor.shutdown();
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
