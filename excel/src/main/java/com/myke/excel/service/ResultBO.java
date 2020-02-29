package com.myke.excel.service;

import lombok.Data;

import java.util.List;

/**
 * 查询结果
 */
@Data
public class ResultBO<T> {

    /**
     * 当前页
     */
    private long currentPage;
    /**
     * 数据总页数
     */
    private long pageCount;
    /**
     * 数据总条数
     */
    private long dataTotal;
    /**
     * 每页查询的结果
     */
    private List<T> data;
}
