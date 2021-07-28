package com.myke.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zhangjianbin
 * @date 2021年07月28日11:31
 */
@Slf4j
public class DateUtils {
    /**
     * JDK 8 之前
     */
    @Test
    public void test1() {
        //SimpleDateFormat 是非线程安全的

        // 定义时间格式化对象和定义格式化样式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 格式化时间对象
        String date = dateFormat.format(new Date());

        log.info("date:[{}]", date);

    }

    /**
     * JDK 8 之后
     */
    @Test
    public void test2() {
        //DateTimeFormatter 是线程安全的

        // 定义时间格式化对象
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化时间对象
        String date = dateFormat.format(LocalDateTime.now());

        log.info("date:[{}]", date);

    }

}