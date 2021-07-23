package com.myke.other.dto;

import lombok.Data;

/**
 * 查询参数类
 */
@Data
public class QueryParamDTO {

    /**
     * 默认当前页码
     */
    private Integer currentPage = 1;

    /**
     * 默认每页多少条
     */
    private Integer pageSize = 10;


    ///**
    // * 默认数据总数
    // */
    //private Integer totalCount = 10;


    /**
     * 计算db开始索引公式
     *
     * @param pageNum
     * @param pageSize
     *
     * @return
     */
    //public static int startIndex(int pageNum, int pageSize) {
    //    return (pageNum - 1) * pageSize;
    //}

    /**
     * 计算总页数公式
     *
     * @param totalCount
     * @param pageSize
     *
     * @return
     */
    //public static int pageCount(int totalCount, int pageSize) {
    //    return (totalCount + pageSize - 1) / pageSize;
    //}

}
