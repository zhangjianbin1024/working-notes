package com.myke.other.dto.validation;

import org.hibernate.validator.HibernateValidator;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class StudentDTOTest {

    @Test
    public void testStudentSave() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setStart(new Date());
        validator(studentDTO, StudentDTO.Save.class);
    }

    @Test
    public void testStudentUpdate() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        validator(studentDTO, StudentDTO.Update.class);
    }

    @Test
    public void testStudentEmails() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(null);
        studentDTO.setName("zhang");
        studentDTO.setAge(2);//null 时 -1时 、 1 时
        // email校验：虽然是List都可以校验哦
        studentDTO.setEmails(Arrays.asList("fsx@gmail.com", "baidu@baidu.com", "aaa.com"));
        validator(studentDTO);
    }

    @Test
    public void testStudentHobbies() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(null);
        studentDTO.setName("zhang");
        studentDTO.setEmails(Arrays.asList("fsx@gmail.com", "baidu@baidu.com"));

        // 测试一
        //studentDTO.setAge(35);// 35 时 hobbies 集合大小必须在3到5之间
        //studentDTO.setHobbies(Arrays.asList("足球", "篮球"));

        // 测试二
        studentDTO.setAge(25);// 25 时 hobbies 集合大小必须在1到2之间
        studentDTO.setHobbies(Arrays.asList());

        validator(studentDTO);
    }

    @Test
    public void testStudentChildList() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setName("zhang");
        studentDTO.setAge(2);
        studentDTO.setEmails(Arrays.asList("fsx@gmail.com", "baidu@baidu.com"));

        // 设置childList
        studentDTO.setChildList(new ArrayList<StudentDTO.InnerChild>() {{
            StudentDTO.InnerChild innerChild = new StudentDTO.InnerChild();
            innerChild.setName(null);
            innerChild.setAge(-11);
            add(innerChild);
        }});

        validator(studentDTO);
    }

    @Test
    public void testStudentCustomsAnnotation() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1);
        studentDTO.setPersonGender(0);
        studentDTO.setNumbers(Arrays.asList(1));
        validator(studentDTO, StudentDTO.CustomsAnnotation.class);
    }


    public void validator(StudentDTO dto, Class<?>... classType) {
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(false)
                .buildValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<StudentDTO>> result = validator.validate(dto, classType);

        // 输出错误消息
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue())
                .forEach(System.out::println);
    }

}