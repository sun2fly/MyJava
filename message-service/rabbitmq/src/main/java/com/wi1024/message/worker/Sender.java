package com.wi1024.message.worker;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.MessageProperties;
import com.wi1024.message.RabbitConfig;
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

    private RabbitConfig config;
    private String exchange;
    private String message;


    public Sender(RabbitConfig config, String exchange, String message) {
        this.config = config;
        this.exchange = exchange;
        this.message = message;
    }

    public void run() {
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUsername());
            factory.setPassword(config.getPassword());
            factory.setVirtualHost(config.getVhost() == null ? "/" : config.getVhost());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            channel.basicPublish(exchange, "", null, message.getBytes("UTF-8"));
            log.debug("Sent '" + message + "'");
            channel.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }
}
