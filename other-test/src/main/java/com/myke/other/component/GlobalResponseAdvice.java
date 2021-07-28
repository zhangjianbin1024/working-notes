package com.myke.other.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myke.other.common.api.CommonResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 参考：https://mp.weixin.qq.com/s/wLLATZqArx9Sl9xX8G0irQ
 * <p>
 * ResponseBodyAdvice的作用：拦截Controller方法的返回值，统一处理返回值/响应体，
 * 一般用来统一返回格式，加解密，签名等等。
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.myke"})
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * @param o                  controller 返回值
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     *
     * @return
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.debug("controller 响应返回值统一包装处理,uri-path:[{}]", serverHttpRequest.getURI().getPath());
        //String类型判断
        if (o instanceof String) {
            return objectMapper.writeValueAsString(CommonResult.success(o));
        }

        //是响应标准格式时直接返回
        if (o instanceof CommonResult) {
            return o;
        }
        // 其他类型时对返回值进行包装
        return CommonResult.success(o);
    }
}