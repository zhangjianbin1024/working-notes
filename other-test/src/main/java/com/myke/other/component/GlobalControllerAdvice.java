package com.myke.other.component;

import com.myke.other.common.api.CommonResult;
import com.myke.other.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static com.myke.other.common.api.ResultCode.BAD_REQUEST;
import static com.myke.other.common.api.ResultCode.FAILED;

/**
 * @author zhangjianbin
 * @date 2021年07月21日15:48
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {


    /**
     * 请求方法不支持 异常处理
     *
     * @param e
     *
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return CommonResult.failed(BAD_REQUEST);
    }

    /**
     * JavaBean参数校验错误会抛出 BindException
     *
     * @param e
     *
     * @return
     */
    @ExceptionHandler(BindException.class)
    public CommonResult bindExceptionHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
                //message = fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    /**
     * 单个参数校验错误会抛出 ConstraintViolationException
     *
     * @param e
     *
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult constraintViolationExceptionHandler(ConstraintViolationException e) {

        //方式一
        String result = null;
        for (ConstraintViolation<?> error : e.getConstraintViolations()) {
            String message = error.getMessage();
            String path = error.getPropertyPath().toString();
            //result = StrUtil.toUnderlineCase(path) + message;
            result = path + message;
            break;
        }
        return CommonResult.validateFailed(result);


        // 方式二
        //Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        //List<String> collect = constraintViolations.stream()
        //        .map(ConstraintViolation::getMessage)
        //        .collect(Collectors.toList());
        //return CommonResult.validateFailed(StringUtils.join(collect.toArray(), ","));
    }

    /**
     * RequestBody为 json 的参数校验异常捕获
     *
     * @param e
     *
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
                //message = fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult exceptionHandler(Exception e) {
        log.error("全局异常:", e);
        return CommonResult.failed(FAILED);
    }


    @ExceptionHandler(value = ApiException.class)
    public CommonResult handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }
}