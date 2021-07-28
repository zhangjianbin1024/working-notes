package com.myke.other.service;

import com.myke.other.OtherApplicationTest;
import com.myke.other.entity.UserDO;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransactionUserService1Test extends OtherApplicationTest {

    @Autowired
    private TransactionUserService1 userService11;

    @Autowired
    private TransactionUserService2 userService12;


    @Autowired
    private TransactionUserService3 userService13;

    /**
     * 手动回滚事务测试
     */
    @Test
    public void save1() {
        UserDO userDO = new UserDO();
        userDO.setUsername("123");
        userService11.insertSelective2(userDO);

        UserDO query = new UserDO();
        query.setUsername("123");
        List<UserDO> result = userService11.queryByAll(query);
        Assertions.assertThat(result).as("集合为空则正确").isEmpty();
    }

}