package com.myke.other.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Transient
    private Date date1 = new Date();
    @Transient
    private LocalDateTime date2 = LocalDateTime.now();
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date date3 = new Date();
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private LocalDateTime date4 = LocalDateTime.now();


    public UserDO() {
    }

    public UserDO(String username) {
        this.username = username;
    }
}