package com.myke.other.controller;

import com.myke.other.bo.ResultBO;
import com.myke.other.common.api.CommonResult;
import com.myke.other.component.GlobalResponseAdvice;
import com.myke.other.convert.UserDoConvertMapper;
import com.myke.other.dto.QueryParamDTO;
import com.myke.other.dto.UserDTO;
import com.myke.other.exception.ApiException;
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
    @PostMapping(value = "/user/createUserresponse1")
    @ResponseBody
    public CommonResult createUserresponse1(@RequestBody UserDTO userDTO) {
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

    /**
     * @param userDTO
     *
     * @return
     *
     * @see GlobalResponseAdvice
     */
    @ApiOperation(value = "添加用户")
    @PostMapping(value = "/user/createUserresponse2")
    @ResponseBody
    public Integer createUserresponse2(@RequestBody UserDTO userDTO) {
        int count = userService.insertSelective(UserDoConvertMapper.INSTANCE.convertUserDO(userDTO));
        if (count == 1) {
            log.debug("createUser success:{}", userDTO);
        } else {
            log.debug("createUser failed:{}", userDTO);
            throw new ApiException("添加用户失败");
        }
        return count;
    }

    @ApiOperation(value = "异常返回值测试")
    @PostMapping(value = "/user/apiException")
    @ResponseBody
    public Integer userApiException(Integer id) {
        if (id == 0) {
            throw new ApiException("异常测试");
        }
        return id;
    }

}