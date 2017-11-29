package com.wi1024.message;

import com.rabbitmq.client.BuiltinExchangeType;
import com.wi1024.message.base.TestBase;
import com.wi1024.message.vo.UserMessage;
import com.wi1024.message.worker.ExchangeSender;

import org.junit.Test;

/**
 * RabbitMQ Producer Test
 *
 * @author songfei@xbniao.com
 * @create 2017/11/15 09:39
 **/
public class RabbitProducer extends TestBase {

    @Test
    public void publish() throws Exception{

        UserMessage message = new UserMessage();
        message.setId(123456L);
        message.setBizUid("abcde");
        message.setEmail("songfei@xbniao.com");
        message.setMobile("18610335560");
        message.setUname("uname");

        ExchangeConfig exchangeConfig = new ExchangeConfig();
        exchangeConfig.setExchange("local.user.fanout.exchange");
        exchangeConfig.setExchangeType(BuiltinExchangeType.FANOUT);
        exchangeConfig.setProperties(null);

        ExchangeSender sender = new ExchangeSender(config , exchangeConfig , message.toString());
        new Thread(sender).start();

        Thread.sleep(1000 * 3);
    }

}
