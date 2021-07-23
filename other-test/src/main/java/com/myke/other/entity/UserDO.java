package com.myke.other.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDO implements Serializable {
    /**
    * 主键
    */
    private Long id;

    /**
    * 年龄
    */
    private Integer age;

    /**
    * 密码
    */
    private String password;

    /**
    * 性别
    */
    private Integer sex;

    /**
    * 用户名
    */
    private String username;

    private static final long serialVersionUID = 1L;
}