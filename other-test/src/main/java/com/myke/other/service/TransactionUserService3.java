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
public class TransactionUserService3 {

    @Autowired
    private UserMapper userMapper;

    /************************* REQUIRED **********************************/

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(UserDO userDO) {
        userMapper.insertSelective(userDO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequiredException(UserDO userDO) {
        userMapper.insertSelective(userDO);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired2(UserDO userDO) {
        userMapper.insertSelective(userDO);
    }

    /************************* REQUIRES_NEW **********************************/

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(UserDO userDO) {
        userMapper.insertSelective(userDO);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(UserDO userDO) {
        userMapper.insertSelective(userDO);
        throw new RuntimeException();
    }

    /************************* NESTED **********************************/
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(UserDO userDO) {
        userMapper.insertSelective(userDO);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addNestedException(UserDO userDO) {
        userMapper.insertSelective(userDO);
        throw new RuntimeException();
    }

}