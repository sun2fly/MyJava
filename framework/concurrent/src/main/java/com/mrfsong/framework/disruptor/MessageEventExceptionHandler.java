package com.mrfsong.framework.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.mrfsong.framework.MessageEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息Event异常处理器
 *
 * @author songfei@xbniao.com
 * @create 2017/12/22 13:08
 **/
@Slf4j
public class MessageEventExceptionHandler implements ExceptionHandler<MessageEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, MessageEvent event) {
        log.error("handleEventException" , ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("handleOnStartException" , ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("handleOnShutdownException" , ex);
    }
}
