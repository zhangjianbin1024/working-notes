package com.myke.other.easyexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.myke.other.easyexcel.ExcelPatternMsg;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * excel数据实体类
 */
@Data
public class UserExcelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //名称
    @ExcelProperty(index = 0, value = "名称")
    @ColumnWidth(30)
    @Length(max = 10)//Length代表的是字符串长度，max代表的是最长允许多长
    private String name;

    //性别
    @ExcelProperty(index = 1, value = "性别")
    @ColumnWidth(30)
    @Length(max = 2)
    private String sex;

    //年龄
    @ExcelProperty(index = 2, value = "年龄")
    @ColumnWidth(30)
    @Pattern(regexp = ExcelPatternMsg.NUMBER, message = ExcelPatternMsg.NUMBER_MSG)//Pattern就是正则表达式注解
    private String age;


    //生日
    @ExcelProperty(index = 3, value = "生日")
    @Pattern(regexp = ExcelPatternMsg.DATE_TIME1, message = ExcelPatternMsg.DATE_TIME1_MSG)
    private String birthday;
}