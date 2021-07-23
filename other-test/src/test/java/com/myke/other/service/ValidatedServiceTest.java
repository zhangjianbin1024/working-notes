package com.myke.other.service;

import com.myke.other.OtherApplicationTest;
import com.myke.other.dto.UserDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class ValidatedServiceTest extends OtherApplicationTest {

    @Autowired
    private ValidatedService validatedService;

    @Test
    public void testCheckAge() {
        try {
            validatedService.checkAge(null);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                // 输出异常信息
                String result = constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ": " + constraintViolation.getInvalidValue();
                System.out.println(result);
            }
        }
    }

    @Test
    public void testCheckUserDto() {
        try {
            UserDTO userDTO = new UserDTO();
            validatedService.checkUserDto(userDTO);
        } catch (ConstraintViolationException e) {
            // 输出异常信息
            e.getConstraintViolations().forEach(constraintViolation ->
                    System.out.println(constraintViolation.getMessage()));
        }
    }

    @Test
    public void testSave() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(null);
        userDTO.setAge(20);
        userDTO.setPassword("1");
        //userDTO.setPassword(null);////Password 不能为空
        userDTO.setSex(2);
        userDTO.setUsername("1");
        userDTO.setEncryptId("289");//加密id不符合规则

        UserDTO.Job job = new UserDTO.Job();
        //job.setJobId(0L);//id不能小于1
        userDTO.setJob(job);
        validatedService.save(userDTO);
    }

    @Test
    public void testUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(2L);
        userDTO.setAge(0);
        userDTO.setPassword("1");
        userDTO.setSex(0);
        userDTO.setUsername("1");

        validatedService.update(userDTO);
    }


}