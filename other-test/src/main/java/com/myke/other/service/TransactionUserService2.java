package com.myke.other.service;

import com.myke.other.entity.UserDO;
import com.myke.other.mapper.UserDaoWithJdbcTemplate;
import com.myke.other.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangjianbin
 * @date 2021年07月28日14:03
 */
@Slf4j
@Service
public class TransactionUserService2 {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDaoWithJdbcTemplate userDaoWithJdbcTemplate;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

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

    /**
     * 测试注解@Transactional的超时timeout---jdbcTemlate实现dao
     * <p>
     */
    @Transactional(timeout = 1)
    public void addUserDoAnnotationTransactionalTimeOutWithJdbcTemplate(UserDO userDO) {
        try {
            userDaoWithJdbcTemplate.insertUserDo(userDO);

            Thread.sleep(3000);
            log.info("sleep 3s end");
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

    @Transactional(timeout = 2)
    public Integer addUserDoAnnotationTransactionalTimeOutWithMybatis(UserDO userDO) {
        try {
            //TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
            //String connection = dataSourceTransactionManager.getDataSource().getConnection().toString();
            Thread.sleep(5000);
            log.info("sleep 3s end");
        } catch (Exception e) {
            log.error("异常", e);
        }
        return userMapper.insertSelective(userDO);
    }
}