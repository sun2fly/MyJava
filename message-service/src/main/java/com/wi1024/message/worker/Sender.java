package com.wi1024.message.worker;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/11/14 16:54
 **/
@Getter
@Setter
@Slf4j
public class Sender implements Runnable {

    private String host;
    private Integer port;
    private String exchange;
    private String message;


    public Sender(String host, Integer port, String exchange, String message) {
        this.host = host;
        this.port = port;
        this.exchange = exchange;
        this.message = message;
    }

    public void run() {
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername("admin");
            factory.setPassword("1qw23er4");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            channel.basicPublish(exchange, "", null, message.getBytes("UTF-8"));
            log.debug("Sent '" + message + "'");
            channel.close();
            connection.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }
}
