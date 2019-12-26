package com.mrfsong.spring.bean;

import lombok.Data;
import org.springframework.context.annotation.Scope;
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
@Scope(value = "prototype")
public class PrototypeService {


    public String sayHello(String name) {
        Integer.valueOf(name);
        return "Prototype say : hi " + name;

    }
}
