package com.myke.other.service;

import com.myke.other.entity.UserDO;
import com.myke.other.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangjianbin
 * @date 2021年07月28日14:03
 */
@Slf4j
@Service
public class TransactionUserService1 {

    @Autowired
    private UserMapper userMapper;

    /**
     * transactionManager 事务名：DataSourceTransactionManagerConfiguration#transactionManager
     */
    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;

    /**
     * bean 定义：TransactionAutoConfiguration.TransactionTemplateConfiguration#transactionTemplate
     */
    @Autowired
    private TransactionTemplate transactionTemplate;


    public List<UserDO> queryByAll(UserDO userDO) {
        return userMapper.queryByAll(userDO);
    }


    /**
     * 注解式事务管理
     *
     * @param userDO
     *
     * @return
     */
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

    /**
     * 方式一：编程式事务管理
     *
     * @param userDO
     */
    public void insertSelective3(UserDO userDO) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        // 返回事务对象
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            userMapper.insertSelective(userDO);
            int i = 1 / 0;
        } catch (Exception ex) {
            log.error("事务回滚", ex);
            transactionManager.rollback(status);
            throw ex;
        } finally {
            transactionManager.commit(status);
        }
    }

    /**
     * 方式二：编程式事务管理
     *
     * @param userDO
     */
    public void insertSelective4(UserDO userDO) {
        //开启事务保存数据
        boolean result = transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    // TODO something
                    userMapper.insertSelective(userDO);

                    int i = 1 / 0;//异常了
                } catch (Exception ex) {
                    log.error("事务回滚", ex);

                    //方式一：手动开启事务回滚
                    //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

                    //方式二：手动开启事务回滚
                    status.setRollbackOnly();

                    return false;
                }
                return true;
            }
        });
    }
}