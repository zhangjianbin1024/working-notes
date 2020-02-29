package com.myke.excel.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 开启 mybatis-plus 分页功能
     *
     * @return
     */
    /**
     * 分页查询拦截器
     */

    @Bean

    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();

    }

    /**
     * SQL输出拦截器
     */

    @Bean

    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //sql格式化
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }


}