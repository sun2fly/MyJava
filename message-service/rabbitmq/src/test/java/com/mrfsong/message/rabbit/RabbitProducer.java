package com.mrfsong.message.rabbit;

import com.mrfsong.message.rabbit.base.TestBase;
import com.mrfsong.message.rabbit.vo.UserMessage;
import com.mrfsong.message.rabbit.worker.ExchangeSender;
import com.rabbitmq.client.BuiltinExchangeType;
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
