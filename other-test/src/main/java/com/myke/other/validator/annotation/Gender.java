package com.myke.other.validator.annotation;

import com.myke.other.validator.constraint.GenderConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {GenderConstraintValidator.class})
public @interface Gender {

    // 三个必备的基本属性
    String message() default "{com.customs.gender.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int gender() default 0; //0：男生  1：女生
}
