package com.myke.other.spi.impl;

import com.myke.other.spi.SPI_IService;

// 服务提供商的具体实现2：Local实现
public class LocalService implements SPI_IService {
    @Override
    public String sayHello() {
        return "Hello LocalService";
    }

    @Override
    public String getScheme() {
        return "local";
    }
}