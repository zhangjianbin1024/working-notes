package com.myke.other.bo;

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
    private long pageNum;
    /**
     * 数据总页数
     */
    private long totalPage;
    /**
     * 数据总条数
     */
    private long total;
    /**
     * 每页查询的结果
     */
    private List<T> data;

    public ResultBO() {
    }

    public ResultBO(long pageNum, long totalPage, long total, List<T> data) {
        this.pageNum = pageNum;
        this.totalPage = totalPage;
        this.total = total;
        this.data = data;
    }
}
