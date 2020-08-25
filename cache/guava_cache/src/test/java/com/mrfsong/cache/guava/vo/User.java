package com.mrfsong.cache.guava.vo;

import com.google.common.base.Objects;
import com.google.common.hash.Hashing;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(name, user.name) &&
                Objects.equal(age, user.age);
    }

    @Override
    public int hashCode() {
        String contactStr = name + "|" + age;
        return Math.abs(Hashing.murmur3_32().hashString(contactStr, StandardCharsets.UTF_8).asInt());
//        return Objects.hashCode(name, age);
    }
}
