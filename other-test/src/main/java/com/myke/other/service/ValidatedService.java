package com.myke.other.service;

import com.myke.other.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author zhangjianbin
 * @date 2021年07月21日16:56
 */
@Service
@Validated
public class ValidatedService {

    @Autowired
    Validator validate;

    public Integer checkAge(@Valid @NotNull(message = "age 字段不能为空") Integer age) {
        return age;
    }

    public UserDTO checkUserDto(@Valid UserDTO userDTO) {
        return userDTO;
    }


    public UserDTO save(UserDTO userDTO) {
        // 主键为空

        Set<ConstraintViolation<UserDTO>> violations = this.validate.validate(userDTO, UserDTO.Save.class);
        // 输出异常信息
        violations.forEach(constraintViolation -> System.out.println(constraintViolation.getPropertyPath() + constraintViolation.getMessage()));
        return userDTO;
    }

    public UserDTO update(UserDTO userDTO) {
        // 主键不能为空

        Set<ConstraintViolation<UserDTO>> violations = this.validate.validate(userDTO, UserDTO.Update.class);
        // 输出异常信息

        violations.forEach(constraintViolation -> System.out.println(constraintViolation.getPropertyPath() + constraintViolation.getMessage()));
        return userDTO;
    }


}