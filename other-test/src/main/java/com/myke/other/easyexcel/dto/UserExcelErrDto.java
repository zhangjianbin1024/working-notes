package com.myke.other.easyexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * excel错误信息实体，继承excel数据实体、多了一个错误信息errMsg属性
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserExcelErrDto extends UserExcelDto {

    //错误信息
    @ExcelProperty(index = 4, value = "错误信息")
    @ColumnWidth(50)
    private String errMsg;


}