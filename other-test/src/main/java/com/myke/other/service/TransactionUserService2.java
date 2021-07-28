package com.myke.other.service;

import com.myke.other.entity.UserDO;
import com.myke.other.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangjianbin
 * @date 2021年07月28日14:03
 */
@Service
public class TransactionUserService2 {

    @Autowired
    private UserMapper userMapper;

    /************************* REQUIRED **********************************/

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addRequired(UserDO userDO) {
        return userMapper.insertSelective(userDO);
    }

    /************************* REQUIRES_NEW **********************************/

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer addRequiresNew(UserDO userDO) {
        return userMapper.insertSelective(userDO);
    }

    /************************* NESTED **********************************/
    @Transactional(propagation = Propagation.NESTED)
    public Integer addNested(UserDO userDO) {
        return userMapper.insertSelective(userDO);
    }

}