package com.wi1024.framework.concurrent;

import java.util.Date;

import lombok.Data;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/20 16:15
 **/
@Data
public class MessageEvent {

    private Long id ;
    private String content;
    private Date createAt;

}
