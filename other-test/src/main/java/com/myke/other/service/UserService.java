package com.myke.other.service;

import com.github.pagehelper.PageHelper;
import com.myke.other.bo.ResultBO;
import com.myke.other.common.api.CommonPage;
import com.myke.other.entity.UserDO;
import com.myke.other.mapper.UserMapper;
import com.myke.other.dto.QueryParamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = {Exception.class})
    public int insertSelective(UserDO userDO) {
        return userMapper.insertSelective(userDO);
    }

    @Transactional(rollbackFor = {Exception.class})
    public int insertSelective2(UserDO userDO) {
        int count = 0;
        try {
            count = userMapper.insertSelective(userDO);
            throw new RuntimeException("测试事务异常");
        } catch (Exception e) {
            e.printStackTrace();
            //手动设置 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return count;
    }


    /**
     * 查询 db 数据
     *
     * @param dto
     *
     * @return
     */
    public ResultBO<UserDO> dbQueryData(QueryParamDTO dto) {
        Integer currentPage = dto.getCurrentPage();
        Integer pageSize = dto.getPageSize();

        PageHelper.startPage(currentPage, pageSize);
        List<UserDO> userDOS = userMapper.queryByAll(new UserDO());
        CommonPage<UserDO> userDOCommonPage = CommonPage.restPage(userDOS);

        return new ResultBO(userDOCommonPage.getPageNum(), userDOCommonPage.getTotalPage(),
                userDOCommonPage.getTotal(), userDOCommonPage.getList());
    }


}
