package com.myke.excel.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myke.excel.task.DbTaskResultDTO;

import java.util.concurrent.*;

public class ThreadUtil {

    /**
     * 创建队列，
     * <p>
     * 阻塞队列，FIFO
     *
     * @param capacity
     *
     * @return
     */
    public static LinkedBlockingQueue<DbTaskResultDTO> createLinkedQueue(Integer capacity) {
        if (null == capacity) {
            new LinkedBlockingQueue(5);
        }
        return new LinkedBlockingQueue(capacity);
    }

    /**
     * 创建线程池
     *
     * @param poolName
     *
     * @return
     */
    public static ExecutorService createThreadPool(String poolName) {
        ThreadFactory
                namedThreadFactory = new
                ThreadFactoryBuilder()
                .setNameFormat(poolName + "-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(
                5, 10,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        return pool;
    }

}
