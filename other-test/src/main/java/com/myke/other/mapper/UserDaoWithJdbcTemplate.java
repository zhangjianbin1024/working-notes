package com.myke.other.mapper;

import com.myke.other.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author zhangjianbin
 * @date 2021年07月29日13:09
 */
@Repository
public class UserDaoWithJdbcTemplate {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertUserDo(UserDO userDO) {
        jdbcTemplate.update(" insert into t_user (username) values(?)",
                new Object[]{userDO.getUsername()});
    }
}