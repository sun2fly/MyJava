package com.mrfsong.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.Data;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 2017/11/18 11:00
 */
@Data
public class ExchangeConfig {
    private String exchange;
    private String routingKey;
    private Boolean durable;
    private AMQP.BasicProperties properties;
    private BuiltinExchangeType exchangeType;

}
