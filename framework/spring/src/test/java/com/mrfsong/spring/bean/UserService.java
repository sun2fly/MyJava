package com.mrfsong.spring.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/10/30
 */
@Data
@Component
public class UserService {



    public String sayHello(String name) {

        return "UserService say : hi " + name;

    }






}
