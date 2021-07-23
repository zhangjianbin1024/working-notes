package com.myke.other.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 *
 * @author zhangjianbin
 * @date 2021年07月20日15:48
 */
@Configuration
//扫描mapper的所在位置
@MapperScan(basePackages = "com.myke.other.mapper")
public class MybatisConfig {
}