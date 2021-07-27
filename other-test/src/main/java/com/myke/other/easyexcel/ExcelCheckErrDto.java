package com.myke.other.easyexcel;

import lombok.Data;

/**
 * 失败结果ExcelCheckErrDto，里面存放的是校验失败的excel实体+错误信息
 *
 * @param <T>
 */
@Data
public class ExcelCheckErrDto<T> {

    private T t;

    private String errMsg;

    public ExcelCheckErrDto() {
    }

    public ExcelCheckErrDto(T t, String errMsg) {
        this.t = t;
        this.errMsg = errMsg;
    }
}