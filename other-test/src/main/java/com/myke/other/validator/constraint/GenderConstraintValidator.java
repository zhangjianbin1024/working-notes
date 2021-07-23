package com.myke.other.validator.constraint;

import com.myke.other.validator.annotation.Gender;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 提示消息中使用自定义的占位字符串
 */
public class GenderConstraintValidator implements ConstraintValidator<Gender, Integer> {

    int genderValue;

    @Override
    public void initialize(Gender constraintAnnotation) {
        genderValue = constraintAnnotation.gender();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);

        //添加参数  校验失败的时候可用, 友好展示
        hibernateContext.addMessageParameter("zhGenderValue", genderValue == 0 ? "男" : "女");

        if (value == null) {
            return false; // null is not valid
        }
        return value == genderValue;
    }
}
