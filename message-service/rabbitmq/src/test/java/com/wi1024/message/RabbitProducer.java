package com.wi1024.message;

import com.wi1024.message.base.TestBase;
import com.wi1024.message.vo.UserMessage;
import com.wi1024.message.worker.Sender;

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

        Sender sender = new Sender(config , exchange , message.toString());
        new Thread(sender).start();

        Thread.sleep(1000 * 3);
    }

}
