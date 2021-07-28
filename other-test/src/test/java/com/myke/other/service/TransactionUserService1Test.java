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
    public void testInsertSelective2() {
        UserDO userDO = new UserDO();
        userDO.setUsername("123");
        userService11.insertSelective2(userDO);

        UserDO query = new UserDO();
        query.setUsername("124");
        List<UserDO> result = userService11.queryByAll(query);
        Assertions.assertThat(result).as("集合为空则正确").isEmpty();
    }

    @Test(expected = Exception.class)
    public void testInsertSelective3() {
        UserDO userDO = new UserDO();
        userDO.setUsername("125");
        userService11.insertSelective3(userDO);

        UserDO query = new UserDO();
        query.setUsername("125");
        List<UserDO> result = userService11.queryByAll(query);
        Assertions.assertThat(result).as("集合为空则正确").isEmpty();
    }

    @Test
    public void testInsertSelective4() {
        UserDO userDO = new UserDO();
        userDO.setUsername("127");
        userService11.insertSelective4(userDO);

        UserDO query = new UserDO();
        query.setUsername("127");
        List<UserDO> result = userService11.queryByAll(query);
        Assertions.assertThat(result).as("集合为空则正确").isEmpty();
    }

}