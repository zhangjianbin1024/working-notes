package com.myke.other.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Set;

public class EasyExcelValiHelper {

    private EasyExcelValiHelper() {
    }

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 该方法会根据实体类中的注解来通过正则表达式判断当前单元格内的数据是否符合标准
     * <p>
     * 例如只能是数字之类的，返回的是检查的错误信息
     *
     * @param obj
     * @param <T>
     *
     * @return
     *
     * @throws NoSuchFieldException
     */
    public static <T> String validateEntity(T obj) throws NoSuchFieldException {
        StringBuilder result = new StringBuilder();
        Set<ConstraintViolation<T>> set = validator.validate(obj);
        if (set != null && !set.isEmpty()) {
            for (ConstraintViolation<T> cv : set) {
                Field declaredField = obj.getClass().getDeclaredField(cv.getPropertyPath().toString());
                ExcelProperty annotation = declaredField.getAnnotation(ExcelProperty.class);
                //拼接错误信息，包含当前出错数据的标题名字+错误信息
                result.append(annotation.value()[0] + cv.getMessage()).append(";");
            }
        }
        return result.toString();
    }
}