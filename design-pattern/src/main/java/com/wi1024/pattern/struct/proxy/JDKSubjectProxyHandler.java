package com.wi1024.pattern.struct.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 17:11
 **/
@Slf4j
public class JDKSubjectProxyHandler implements InvocationHandler {

    private Object target ;

    public JDKSubjectProxyHandler(Class clazz) {
        try {
            this.target = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error("Create proxy for {} failed !" , clazz.getName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        preAction();
        Object result = method.invoke(target , args);
        postAction();
        return result;
    }

    private void preAction(){
        log.info("========== JDKSubjectProxyHandler#preAction() ==========");
    }

    private void postAction() {
        log.info("========== JDKSubjectProxyHandler#postAction() ==========");
    }


}
