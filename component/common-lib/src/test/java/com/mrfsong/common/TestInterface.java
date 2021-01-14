package com.mrfsong.common;

/**
 * @Auther: songfei20
 * @Date: 2021/1/13 18:49
 * @Description:
 */
public interface TestInterface {

    //Felix: 此方法为默认实现、实现类可以自行选择是否重写该方法，不强制要求实现
    default String sayHi() {
        return "hi , I'm the interface default method!";
    }

    //Felix: 此方法为接口方法、实现类不可重写
    static String areYouOk() {
        return "I'm interface static method , i'm ok !";
    }
}