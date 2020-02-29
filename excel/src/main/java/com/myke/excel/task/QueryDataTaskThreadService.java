package com.myke.excel.task;

import com.myke.excel.entity.UserDO;
import com.myke.excel.service.ResultBO;
import com.myke.excel.service.UserService;
import com.myke.excel.web.QueryParamDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * db 数据查询服务
 */
@Slf4j
public class QueryDataTaskThreadService implements Runnable {
    private LinkedBlockingQueue<DbTaskResultDTO> linkedQueue;
    private QueryParamDTO queryParamDTO;
    private UserService userService;

    public QueryDataTaskThreadService(LinkedBlockingQueue<DbTaskResultDTO> linkedQueue,
                                      QueryParamDTO queryParamDTO,
                                      UserService userService) {
        this.linkedQueue = linkedQueue;
        this.queryParamDTO = queryParamDTO;
        this.userService = userService;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Integer currentPage = queryParamDTO.getCurrentPage();
        try {
            // 查询数据
            ResultBO<UserDO> userDOResultBO =
                    userService.dbQueryData(queryParamDTO);

            //db任务结果
            DbTaskResultDTO dbTaskResultDTO = new DbTaskResultDTO();
            dbTaskResultDTO.setResultBO(userDOResultBO);

            long end = System.currentTimeMillis() - start;
            log.info("第[{}]页查询db数据结束,耗时:[{}]秒", currentPage, end / 1000);

            // put 该方法没有返回值，当队列已满时，会阻塞当前线程
            linkedQueue.put(dbTaskResultDTO);

        } catch (Exception e) {
            log.info("第[{}] 页查询db数据异常", currentPage, e);
        }
    }
}
