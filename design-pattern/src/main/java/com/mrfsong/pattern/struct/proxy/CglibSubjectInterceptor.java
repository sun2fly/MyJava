package com.mrfsong.pattern.struct.proxy;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 09:40
 **/
@Slf4j
public class CglibSubjectInterceptor implements MethodInterceptor {


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        preAction();
        Object result = methodProxy.invokeSuper(o , objects);
        postAction();
        return result;
    }


    private void preAction(){
        log.info("========== CglibSubjectInterceptor#preAction() ==========");
    }

    private void postAction() {
        log.info("========== CglibSubjectInterceptor#postAction() ==========");
    }


}
