package com.mrfsong.message.rabbit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 2017/11/14 21:21
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class RabbitConfig {

    private String  host;
    private Integer port;
    private String  username;
    private String  password;
    private String  vhost;



}
