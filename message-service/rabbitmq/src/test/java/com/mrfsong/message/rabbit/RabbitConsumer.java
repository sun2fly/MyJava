package com.mrfsong.message.rabbit;

import com.mrfsong.message.rabbit.base.TestBase;
import com.mrfsong.message.rabbit.worker.Receiver;
import org.junit.Test;

/**
 * RabbitMQ Consumer Test
 *
 * @author songfei@xbniao.com
 * @create 2017/11/15 09:39
 **/
public class RabbitConsumer extends TestBase {

    @Test
    public void subscribe() throws Exception{

        //Start first consumer
        Receiver receiver = new Receiver(config ,exchange);
        new Thread(receiver).start();



        //Start second consumer

        receiver = new Receiver(config ,exchange);
        new Thread(receiver).start();

        //Sleep to wait receiver ready
        Thread.sleep(1000 * 60 * 10);



    }



}
