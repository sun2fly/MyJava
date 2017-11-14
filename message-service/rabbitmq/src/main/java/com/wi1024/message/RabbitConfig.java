package com.wi1024.message;

import lombok.*;

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



}
