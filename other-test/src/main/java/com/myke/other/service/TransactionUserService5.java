package com.myke.other.service;

import com.myke.other.entity.UserDO;
import com.myke.other.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

/**
 * @author zhangjianbin
 * @date 2021年07月28日14:03
 */
@Slf4j
@Service
public class TransactionUserService5 {

    @Autowired
    private UserMapper userMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addRequired(UserDO userDO) {
        return userMapper.insertSelective(userDO);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Integer deleteRequired(String userName) {
        return userMapper.deleteByUsername(userName);
    }

    /**
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Transactional(rollbackFor = Exception.class)
    public void test_thread2() throws ExecutionException, InterruptedException {
        UserDO user = new UserDO("201");
        addRequired(user);//同一类中的添加方法

        // add和delete未标注事务
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                UserDO user = new UserDO("202");
                addRequired(user);
                deleteRequired("zhang");//删除,同一类中的删除方法
                throw new Exception("模拟异常操作");//抛异常
            }
        };
        ExecutorService service = Executors.newCachedThreadPool();
        Future<String> submit = service.submit(callable);
        service.shutdown();
        log.info("线程执行结果:[{}]", submit.get());
    }
}