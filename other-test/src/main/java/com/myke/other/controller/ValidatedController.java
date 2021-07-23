package com.myke.other.controller;

import com.myke.other.dto.UserDTO;
import com.myke.other.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.Set;

/**
 * @author zhangjianbin
 * @date 2021年07月21日15:19
 */
@Slf4j
@RestController
@RequestMapping("/api/check")
@Validated
public class ValidatedController {

    @Autowired
    private UserService userService;

    /**
     * requestBody参数校验
     * <p>
     * 使用@Valid和@Validated都可以。
     * <p>
     * MethodArgumentNotValidException异常
     *
     * @param userDTO
     *
     * @return
     */
    @PostMapping("/requestBody")
    public ResponseEntity<UserDTO> requestBody(@RequestBody @Validated UserDTO userDTO) {
        return ResponseEntity.ok().body(userDTO);
    }

    /**
     * PathVariable参数校验
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/pathVariable/{id}")
    public ResponseEntity<Integer> pathVariable(@Valid @PathVariable("id")
                                                @Max(value = 5) Integer id) {
        return ResponseEntity.ok().body(id);
    }

    /**
     * requestParam参数校验
     *
     * <p>
     * 推荐将一个个参数平铺到方法入参中。在这种情况下，
     * 必须在Controller类上标注@Validated注解，
     * 并在入参上声明约束注解(如@Min等)
     * <p>
     * 抛出ConstraintViolationException异常
     *
     * @param id
     *
     * @return
     */
    @PutMapping("/requestParam")
    public ResponseEntity<Integer> requestParam(@Valid @RequestParam("id")
                                                @Max(value = 5) Integer id) {
        return ResponseEntity.ok().body(id);
    }

    /**
     * @param userDTO
     *
     * @return
     *
     * @Validated 注解上指定校验分组
     */
    @PostMapping("/save")
    public ResponseEntity saveUser(@RequestBody @Validated(UserDTO.Save.class) UserDTO userDTO) {
        // 校验通过，才会执行业务逻辑处理
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO) {
        // 校验通过，才会执行业务逻辑处理
        return ResponseEntity.ok().body(null);
    }

    /**
     * 抛出BindException异常
     * <p>
     * JavaBean方式接收参数
     *
     * @param userDTO
     *
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity delete(@Validated UserDTO userDTO) {
        // 校验通过，才会执行业务逻辑处理
        return ResponseEntity.ok().body(null);
    }

    /**
     * 手动来处理错误，加上一个BindingResult来接收验证结果
     *
     * @param userDTO
     * @param bindingResult
     *
     * @return
     */
    @DeleteMapping("/delete2")
    public ResponseEntity delete2(@Validated UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = "";
            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                if (fieldError != null) {
                    message = fieldError.getField() + fieldError.getDefaultMessage();
                    //message = fieldError.getDefaultMessage();
                }
            }
            return ResponseEntity.ok(message);
        }


        // 校验通过，才会执行业务逻辑处理
        return ResponseEntity.ok().body(null);
    }


    /**
     * 集合校验
     *
     * @param userList
     *
     * @return
     */
    @PostMapping("/saveList")
    public ResponseEntity saveList(@RequestBody List<@Valid UserDTO> userList) {
        // 校验通过，才会执行业务逻辑处理
        return ResponseEntity.ok().body(null);
    }


    @Autowired
    private javax.validation.Validator globalValidator;

    /**
     * 编程式校验
     *
     * @param userDTO
     *
     * @return
     */
    @PostMapping("/saveWithCodingValidate")
    public ResponseEntity saveWithCodingValidate(@RequestBody UserDTO userDTO) {
        Set<ConstraintViolation<UserDTO>> validate = globalValidator.validate(userDTO, UserDTO.Save.class);
        // 如果校验通过，validate为空；否则，validate包含未校验通过项
        if (validate.isEmpty()) {
            // 校验通过，才会执行业务逻辑处理

        } else {
            for (ConstraintViolation<UserDTO> userDTOConstraintViolation : validate) {
                // 校验失败，做其它逻辑
                //System.out.println(userDTOConstraintViolation);
                String result = userDTOConstraintViolation.getPropertyPath() + userDTOConstraintViolation.getMessage();
                return ResponseEntity.ok().body(result);
            }
        }
        return ResponseEntity.ok().body(null);
    }

}