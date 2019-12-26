package com.mrfsong.spring.aop;

import com.mrfsong.spring.bean.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/12/25
 */
@Slf4j
@Aspect
@Component
public class TestAspect {

    @Autowired
    private UserService userService;

    @Pointcut("execution(* com.mrfsong.spring.bean.PrototypeService.sayHello(..))")
    public void pointCall() {
    }


    /**
     * 返回通知，在连接点方法执行并正常返回后调用，要求连接点方法在执行过程中没有发生异常
     * @param joinPoint
     * @param result
     */
    @AfterReturning(returning="result",value="pointCall()")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        log.warn("==================== TestAspect AfterReturning execute ====================");
        log.info("AfterReturning JoinPoint params : {} , result : {}" , joinPoint.getArgs() , result);
        userService.sayHello("afterReturning");



    }


    /**
     * 在执行目标方法过程中，如果方法抛出异常则会走此方法
     * @param joinPoint
     * @param expt
     */
    @AfterThrowing(throwing="expt",value="pointCall()")
    public void AfterThrowing(JoinPoint joinPoint,Throwable expt) {
        log.warn("==================== TestAspect AfterThrowing execute ====================");
        log.info("AfterThrowing JoinPoint params : {} ,exception : {}" ,joinPoint.getArgs() , expt.toString());
        userService.sayHello("AfterThrowing");
    }


    /**
     * 前置通知，在连接点方法前调用
     */
    @Before("pointCall()")
    public void before(JoinPoint joinPoint){
        log.warn("==================== TestAspect Before execute ====================");
        log.info("Before JoinPoint params : {} " ,joinPoint.getArgs());

        userService.sayHello("before");

    }

    /**
     * 后置通知，在连接点方法后调用
     */
    @After("pointCall()")
    public void after(JoinPoint joinPoint){
        log.warn("==================== TestAspect After execute ====================");
        log.info("After JoinPoint params : {} " ,joinPoint.getArgs());

        userService.sayHello("after");

    }


    /**
     * 环绕通知，它将覆盖原有方法，但是允许你通过反射调用原有方法(！！！该方式慎用！！！)
     * @param joinPoint
     * @return
     */
    @Around("pointCall()")
    public Object around(ProceedingJoinPoint joinPoint){
        userService.sayHello("around");
        log.warn("==================== TestAspect Around execute enter ====================");
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        log.warn("==================== TestAspect Around execute leave ====================");
        return result;
    }





}
