package com.myke.other.easyexcel;

import java.util.List;

/**
 * excel数据业务校验接口ExcelCheckManager
 * <p>
 * 需要校验excel业务的service接口可以继承这个接口，
 * 并在实现类中实现自己的方法，
 * <p>
 * 返回的是ExcelCheckResult，里面包含成功+失败的结果集
 *
 * @param <T>
 */
public interface ExcelCheckManager<T> {


    /**
     * 校验方法
     *
     * @param objects
     *
     * @return
     */
    ExcelCheckResult checkImportExcel(List<T> objects);
}