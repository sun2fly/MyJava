package com.mrfsong.pattern.creat.builder;

import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 11:16
 **/
public class Person {
    //Required Fields
    private final String name;
    private final String sex ;
    private final Date date ;
    private final String email;

    //Optional Fields
    private final int height;
    private final String edu;
    private final String nickName;
    private final int weight;
    private final String addr;

    private Person(Builder builder) {
        this.name = builder.name;
        this.sex = builder.sex;
        this.date = builder.date;
        this.email = builder.email;
        this.height = builder.height;
        this.edu = builder.edu;
        this.nickName = builder.nickName;
        this.weight = builder.weight;
        this.addr = builder.addr;
    }



    public static class Builder {
        //Required Fields
        private final String name;
        private final String sex ;
        private final Date date ;
        private final String email;

        //Optional Fields
        private int height;
        private String edu;
        private String nickName;
        private int weight;
        private String addr;


        public Builder(String name, String sex, Date date, String email) {
            this.name = name;
            this.sex = sex;
            this.date = date;
            this.email = email;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder edu(String edu) {
            this.edu = edu;
            return this;
        }

        public Builder nickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder addr(String addr) {
            this.addr = addr;
            return this ;
        }

        public Person builder() {
            return new Person(this);
        }

    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", date=" + date +
                ", email='" + email + '\'' +
                ", height=" + height +
                ", edu='" + edu + '\'' +
                ", nickName='" + nickName + '\'' +
                ", weight=" + weight +
                ", addr='" + addr + '\'' +
                '}';
    }
}
