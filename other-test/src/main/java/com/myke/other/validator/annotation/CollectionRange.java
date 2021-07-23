package com.myke.other.validator.annotation;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Size;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 通达定义析的注解覆盖原来注解提供的默认值
 */
@Documented
@Constraint(validatedBy = {})
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(value = CollectionRange.List.class)
@Size // 校验动作委托给Size去完成  所以它自己并不需要校验器~~~
@ReportAsSingleViolation // 组合组件一般建议标注上
public @interface CollectionRange {

    // 三个必备的基本属性
    String message() default "{com.customs.collection.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 自定义属性  @OverridesAttribute这里有点方法覆盖的意思~~~~~~ 子类属性覆盖父类的默认值嘛
    @OverridesAttribute(constraint = Size.class, name = "min")
    int min() default 0;

    @OverridesAttribute(constraint = Size.class, name = "max")
    int max() default Integer.MAX_VALUE;

    // 重复注解
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CollectionRange[] value();
    }
}
