package com.myke.other;

import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://www.cnblogs.com/thisiswhy/p/12690630.html
 *
 * @author zhangjianbin
 * @date 2021年08月09日10:48
 */
@Slf4j
public class ThreadChangeDemo {

    public static void main(String[] args) throws InterruptedException {
        dynamicModifyExecutor();
    }

    /**
     * 先提交任务给线程斌，并修改池程池参数
     *
     * @throws InterruptedException
     */
    private static void dynamicModifyExecutor() throws InterruptedException {
        ThreadPoolExecutor executor = buildThreadPoolExecutor();
        for (int i = 0; i < 15; i++) {
            executor.submit(() -> {
                threadPooolStatus(executor, "创建线程");
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("************************************************");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        threadPooolStatus(executor, "改变之前");
        executor.setCorePoolSize(10);
        executor.setMaximumPoolSize(10);
        threadPooolStatus(executor, "改变之后");

        executor.prestartCoreThread();//启动所有的核心线程数

        Thread.currentThread().join();
    }

    /**
     * 自定义线程池
     *
     * @return
     */
    private static ThreadPoolExecutor buildThreadPoolExecutor() {
        return new ThreadPoolExecutor(2,
                5,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                new NamedThreadFactory("测试POOL"));
    }

    /**
     * 打印线程池状态
     *
     * @param executor
     * @param name
     */
    private static void threadPooolStatus(ThreadPoolExecutor executor, String name) {
        LinkedBlockingDeque<Runnable> queue = (LinkedBlockingDeque) executor.getQueue();

        log.info("线程:{} " +
                        "核心线程数:{} " +
                        "活动线程数:{} " +
                        "最大线程数:{} " +
                        "线程池活跃度:{} " +
                        "任务完成数:{} " +
                        "队列大小:{} " +
                        "当前排队线程数:{} '" +
                        "队列剩余大小:{}" +
                        "队列使用度:{}",
                new Object[]{Thread.currentThread().getName() + "-" + name,
                        executor.getCorePoolSize(),
                        executor.getActiveCount(),
                        executor.getMaximumPoolSize(),
                        divide(executor.getActiveCount(), executor.getMaximumPoolSize()),
                        executor.getCompletedTaskCount(),
                        (queue.size() + queue.remainingCapacity()),
                        queue.size(),
                        queue.remainingCapacity(),
                        divide(queue.size(), queue.size() + queue.remainingCapacity())});

    }

    /**
     * 保留两位小数
     *
     * @param num1
     * @param num2
     *
     * @return
     */
    private static String divide(int num1, int num2) {
        return String.format("%1.2f%%",
                Double.parseDouble(String.valueOf(num1)) / Double.parseDouble(String.valueOf(num2)) * 100);
    }

}