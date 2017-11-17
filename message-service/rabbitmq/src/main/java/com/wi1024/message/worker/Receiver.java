package com.wi1024.message.worker;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import com.wi1024.message.RabbitConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/11/14 16:56
 **/
@Getter
@Setter
@Slf4j
public class Receiver implements Runnable {

    private RabbitConfig config;
    private String exchange;

    public Receiver(RabbitConfig config, String exchange) {
        this.config = config;
        this.exchange = exchange;
    }

    public void run() {
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUsername());
            factory.setPassword(config.getPassword());
            factory.setVirtualHost(config.getVhost());

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT , true);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchange, "");

            log.debug("Waiting for messages");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    log.info("Received '" + message + "'");
                }
            };
            channel.basicConsume(queueName, false, consumer);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
