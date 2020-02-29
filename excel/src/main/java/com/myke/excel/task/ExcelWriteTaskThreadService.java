package com.myke.excel.task;

import com.myke.excel.service.ResultBO;
import com.myke.excel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 写 excel 任务
 */
@Slf4j
public class ExcelWriteTaskThreadService implements Runnable {

    /**
     * 记录 excel写的行号
     * <p>
     * 去掉表头,所以从 1开始计
     */
    private AtomicInteger excelRowNum = new AtomicInteger(1);

    private Sheet sheet;
    private CountDownLatch latch;

    private LinkedBlockingQueue<DbTaskResultDTO> linkedQueue;

    private UserService userService;


    public ExcelWriteTaskThreadService(CountDownLatch latch, Sheet sheet,
                                       LinkedBlockingQueue<DbTaskResultDTO> linkedQueue,
                                       UserService userService) {
        this.latch = latch;
        this.sheet = sheet;
        this.linkedQueue = linkedQueue;
        this.userService = userService;
    }

    @Override
    public void run() {
        long excelStartTime = System.currentTimeMillis();

        try {
            while (true) {
                //若队列为空，发生阻塞，等待有元素,take: 获取并移除此队列的头部
                DbTaskResultDTO dbTaskResultDTO = linkedQueue.take();

                long excelWriteStartTime = System.currentTimeMillis();

                //db查询结果数据
                ResultBO dbResult = dbTaskResultDTO.getResultBO();
                long currentPage = dbResult.getCurrentPage();
                List data = dbResult.getData();
                long dataTotal = dbResult.getDataTotal();

                //数据写入 excel中
                userService.writeExcelCellValue(sheet, excelRowNum, data);

                long excelWriteEndTime = System.currentTimeMillis();
                log.info("excel 第 [{}] 页写入完毕;数据总条数:[{}],耗时:[{}]秒",
                        currentPage,
                        data.size(),
                        (excelWriteEndTime - excelWriteStartTime) / 1000);


                //excel写入的行和db中总行数一致时，说明excel已写完所有db数据
                int rowNum = excelRowNum.get() - 1;
                if (rowNum == dataTotal) {
                    log.info("excel已将数据全部写入完毕,总条数:[{}];总耗时:[{}]秒",
                            dataTotal,
                            (System.currentTimeMillis() - excelStartTime) / 1000);

                    //将excel数据返给页面
                    latch.countDown();
                }
            }
        } catch (Exception e) {
            log.error("[{}] excel导出异常", sheet.getSheetName(), e);
            latch.countDown();
        }
    }

}
