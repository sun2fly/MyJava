package com.mrfsong.cache.ehcache.vo;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/28
 */
public class User implements Serializable {

    private String name;
    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("age=" + age)
                .toString();
    }
}
