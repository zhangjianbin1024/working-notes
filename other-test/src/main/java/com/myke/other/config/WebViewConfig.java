package com.myke.other.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebViewConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //添加swaggerUi重定向配置
        registry.addRedirectViewController("/", "/swagger-ui.html#/");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //添加资源加载
        //registry.addResourceHandler("swagger-ui.html")
        //        .addResourceLocations("classpath:/META-INF/resources/");
        //registry.addResourceHandler("/webjars/**")
        //        .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}
