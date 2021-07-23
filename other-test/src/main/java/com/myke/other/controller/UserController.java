package com.myke.other.controller;

import com.myke.other.bo.ResultBO;
import com.myke.other.common.api.CommonResult;
import com.myke.other.convert.UserDoConvertMapper;
import com.myke.other.dto.QueryParamDTO;
import com.myke.other.dto.UserDTO;
import com.myke.other.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangjianbin
 * @date 2021年07月21日14:40
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * list
     *
     * @param queryParamDTO
     *
     * @return
     */
    @GetMapping("/list")
    public ResultBO list(QueryParamDTO queryParamDTO) {
        return userService.dbQueryData(queryParamDTO);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping(value = "/user/create")
    @ResponseBody
    public CommonResult createUser(@RequestBody UserDTO userDTO) {
        CommonResult commonResult;
        int count = userService.insertSelective(UserDoConvertMapper.INSTANCE.convertUserDO(userDTO));
        if (count == 1) {
            commonResult = CommonResult.success(userDTO);
            log.debug("createUser success:{}", userDTO);
        } else {
            commonResult = CommonResult.failed("操作失败");
            log.debug("createUser failed:{}", userDTO);
        }
        return commonResult;
    }
}