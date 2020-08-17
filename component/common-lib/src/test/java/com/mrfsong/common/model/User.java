package com.mrfsong.common.model;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/30 18:35
 */
@Data
public class User {


    private Integer age;
    private String name ;
    private String address;

    private List<String> list;


}
