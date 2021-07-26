package com.myke.other.spi.impl;

import com.myke.other.spi.SPI_IService;

// 服务提供商的具体实现1：HDFS实现
public class HDFSService implements SPI_IService {

    @Override
    public String sayHello() {
        return "Hello HDFSService";
    }

    @Override
    public String getScheme() {
        return "hdfs";
    }
}


