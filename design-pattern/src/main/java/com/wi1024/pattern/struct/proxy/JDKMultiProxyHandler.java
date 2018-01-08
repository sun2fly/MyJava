package com.wi1024.pattern.struct.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 17:14
 **/
@Slf4j
public class JDKMultiProxyHandler implements InvocationHandler {

    private Object target ;

    public JDKMultiProxyHandler(Class clazz) {
        try {
            this.target = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            log.error("Create proxy for {} failed !" , clazz.getName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch(method.getName()){
            case "hashCode":
                return ((int)method.invoke(target , args)) + 1;
            case "equals":
                return (boolean)method.invoke(target, args);
            case "toString":
                return method.invoke(target, args) + "";
            case "action":
                preAction();
                Object result = method.invoke(target, args);
                postAction();
                return result;
            default:
                return method.invoke(target, args);
        }
    }


    private void preAction(){
        log.info("========== JDKSubjectProxyHandler#preAction() ==========");
    }

    private void postAction() {
        log.info("========== JDKSubjectProxyHandler#postAction() ==========");
    }
}
