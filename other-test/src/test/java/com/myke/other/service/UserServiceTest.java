package com.myke.other.service;

import com.myke.other.OtherApplicationTest;
import com.myke.other.entity.UserDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class UserServiceTest extends OtherApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void batchSave() {
        int count = 100000;
        for (int i = 0; i < count; i++) {
            UserDO userDO = new UserDO();
            userDO.setAge(i);
            userDO.setPassword(i + "");
            userDO.setSex(i);
            userDO.setUsername(i + "");
            userService.insertSelective(userDO);
        }
    }

    @Transactional
    @Test
    public void save() { //事务会回滚
        UserDO userDO = new UserDO();
        userDO.setAge(0);
        userDO.setPassword("123");
        userDO.setSex(0);
        userDO.setUsername("123");
        userService.insertSelective(userDO);
    }

    @Test
    public void save2() { //事务不会回滚
        UserDO userDO = new UserDO();
        userDO.setAge(0);
        userDO.setPassword("123");
        userDO.setSex(0);
        userDO.setUsername("123");
        userService.insertSelective(userDO);
    }

    @Test
    public void save3() { //手动回滚事务
        UserDO userDO = new UserDO();
        userDO.setAge(0);
        userDO.setPassword("123");
        userDO.setSex(0);
        userDO.setUsername("123");
        userService.insertSelective2(userDO);
    }
}