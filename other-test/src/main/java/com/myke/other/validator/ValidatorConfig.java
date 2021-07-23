package com.myke.other.validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author zhangjianbin
 * @date 2021年07月21日18:07
 */
@Configuration
public class ValidatorConfig {

    /**
     * 快速失败(Fail Fast)
     * <p>
     * 开启Fali Fast模式，一旦校验失败就立即返回。
     *
     * @return
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory =
                Validation.byProvider(HibernateValidator.class)
                        .configure()
                        // 快速失败模式
                        .failFast(true)
                        .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}