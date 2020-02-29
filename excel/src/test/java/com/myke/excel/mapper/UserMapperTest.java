package com.myke.excel.mapper;

import com.myke.excel.ExcelApplicationTest;
import com.myke.excel.entity.UserDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperTest extends ExcelApplicationTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void batchSave() {
        int count = 100000;
        for (int i = 0; i < count; i++) {
            UserDO userDO = new UserDO();
            userDO.setAge(i);
            userDO.setPassword(i + "");
            userDO.setSex(i);
            userDO.setUsername(i + "");
            userMapper.insert(userDO);
        }

    }

    @Test
    public void save() {
        UserDO userDO = new UserDO();
        userDO.setAge(0);
        userDO.setPassword("123");
        userDO.setSex(0);
        userDO.setUsername("123");
        userMapper.insert(userDO);
    }
}