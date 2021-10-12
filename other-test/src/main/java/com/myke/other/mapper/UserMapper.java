package com.myke.other.mapper;

import com.myke.other.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insertSelective(UserDO record);

    List<UserDO> queryAllByUsername(@Param("username") String username);

    List<UserDO> queryByAll(UserDO userDO);

    int deleteByUsername(@Param("username")String username);



}