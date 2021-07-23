package com.myke.other.validator.group;

import com.myke.other.dto.validation.StudentDTO;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 根据字段的值动态添加相应的分组进行校验
 */
public class StudentGroupSequenceProvider implements DefaultGroupSequenceProvider<StudentDTO> {

    @Override
    public List<Class<?>> getValidationGroups(StudentDTO bean) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        // 这一步不能省,否则Default分组都不会执行了，会抛错的
        defaultGroupSequence.add(StudentDTO.class);

        if (bean != null) { // 这块判空请务必要做
            Integer age = bean.getAge();
            if (Objects.nonNull(age)) {
                if (age >= 20 && age < 30) {
                    System.err.println("年龄为：" + age + "，执行对应校验逻辑 WhenAge20And30Group");
                    defaultGroupSequence.add(StudentDTO.WhenAge20And30Group.class);
                } else if (age >= 30 && age < 40) {
                    System.err.println("年龄为：" + age + "，执行对应校验逻辑 WhenAge30And40Group");
                    defaultGroupSequence.add(StudentDTO.WhenAge30And40Group.class);
                }
            }

        }
        return defaultGroupSequence;
    }
}
