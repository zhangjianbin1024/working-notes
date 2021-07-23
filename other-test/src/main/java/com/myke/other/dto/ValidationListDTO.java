package com.myke.other.dto;

import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 如果请求体直接传递了json数组给后台，并希望对数组中的每一项都进行参数校验，需要自定义集合类来处理
 * <p>
 * 自定义list集合来接收参数实现集合校验效果
 * <p>
 * 如果校验不通过，会抛出NotReadablePropertyException
 *
 * @param <E>
 */
public class ValidationListDTO<E> implements List<E> {

    @Delegate // @Delegate是lombok注解
    @Valid // 一定要加@Valid注解
    public List<E> list = new ArrayList<>();

    // 一定要记得重写toString方法
    @Override
    public String toString() {
        return list.toString();
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
