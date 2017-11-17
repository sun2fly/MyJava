package com.wi1024.message.base;

import com.wi1024.message.RabbitConfig;

import org.junit.Before;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/11/15 09:40
 **/
@Slf4j
public class TestBase {

    protected RabbitConfig config ;
    protected String exchange;

    @Before
    public void init(){
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
            String host = properties.getProperty("rabbit.host");
            Integer port = Integer.parseInt(properties.getProperty("rabbit.port"));
            String user = properties.getProperty("rabbit.user");
            String password = properties.getProperty("rabbit.password");
            String vhost = properties.getProperty("rabbit.vhost");
            exchange = properties.getProperty("rabbit.exchange");
            config = new RabbitConfig(host , port , user ,password , vhost);
        } catch (IOException e) {
            log.error("fail to load config.properties , cause by : {}" , e.getMessage());
        }


    }

}
