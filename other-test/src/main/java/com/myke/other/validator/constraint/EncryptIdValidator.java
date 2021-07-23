package com.myke.other.validator.constraint;

import com.myke.other.validator.annotation.EncryptId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义一个约束注解，并定义默认的校验错误信息
 * 实现一个校验器(实现接口：ConstraintValidator)
 * <p>
 * 加密校验注解
 */
public class EncryptIdValidator implements ConstraintValidator<EncryptId, String> {

    //自定义加密id（由数字或者a-f的字母组成，4-256长度）
    private static final Pattern PATTERN = Pattern.compile("^[a-f\\d]{4,256}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 不为null才进行校验
        if (value != null) {
            Matcher matcher = PATTERN.matcher(value);
            return matcher.find();
        }
        return true;
    }

    @Override
    public void initialize(EncryptId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
