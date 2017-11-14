package com.wi1024.message;

import com.wi1024.message.vo.UserMessage;
import com.wi1024.message.worker.Receiver;
import com.wi1024.message.worker.Sender;

import java.util.Properties;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/11/14 16:44
 **/
public class Application {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(Application.class.getClassLoader().getResourceAsStream("config.properties"));
        String host = properties.getProperty("rabbit.host");
        Integer port = Integer.parseInt(properties.getProperty("rabbit.port"));
        String exchange = properties.getProperty("rabbit.exchange");


        Receiver receiver = new Receiver(host , port ,exchange);
        new Thread(receiver).start();

        //Sleep to wait receiver ready
        Thread.sleep(1000 * 3);

        UserMessage message = new UserMessage();
        message.setId(123456L);
        message.setBizUid("abcde");
        message.setEmail("songfei@xbniao.com");
        message.setMobile("18610335560");
        message.setUname("uname");

        Sender sender = new Sender(host , port , exchange , message.toString());
        new Thread(sender).start();



        Thread.sleep(1000 * 60);



    }
}
