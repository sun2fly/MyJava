package com.mrfsong.common.model;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/30 18:35
 */
//@Data
public class User {
    private Integer age;
    private String name ;
    private String address;
    private List<String> list;

    public User() {
    }

    public User(Integer age, String name, String address, List<String> list) {
        this.age = age;
        this.name = name;
        this.address = address;
        this.list = list;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(age, user.age) &&
                Objects.equals(name, user.name) &&
                Objects.equals(address, user.address) &&
                Objects.equals(list, user.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, address, list);
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", list=" + list +
                '}';
    }
}
