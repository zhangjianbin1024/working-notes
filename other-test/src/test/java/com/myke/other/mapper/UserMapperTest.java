package com.myke.other.mapper;

import com.myke.other.OtherApplicationTest;
import com.myke.other.entity.UserDO;
import com.myke.other.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperTest extends OtherApplicationTest {
    @Autowired
    private UserMapper userMapper;

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
            userMapper.insertSelective(userDO);
        }
    }

    @Test
    public void save() {
        UserDO userDO = new UserDO();
        userDO.setAge(0);
        userDO.setPassword("123");
        userDO.setSex(0);
        userDO.setUsername("123");
        userMapper.insertSelective(userDO);
    }
}