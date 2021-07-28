package com.myke.other.service;

import com.myke.other.entity.UserDO;
import com.myke.other.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @author zhangjianbin
 * @date 2021年07月28日14:03
 */
@Service
public class TransactionUserService1 {

    @Autowired
    private UserMapper userMapper;

    public List<UserDO> queryByAll(UserDO userDO) {
        return userMapper.queryByAll(userDO);
    }


    @Transactional(rollbackFor = {Exception.class})
    public int insertSelective2(UserDO userDO) {
        int count = 0;
        try {
            count = userMapper.insertSelective(userDO);
            throw new RuntimeException("测试事务异常时,手动回滚事务");
        } catch (Exception e) {
            e.printStackTrace();
            //手动设置 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return count;
    }
}