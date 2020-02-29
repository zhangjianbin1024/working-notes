package com.myke.excel.web;

import com.myke.excel.convert.UserDoConvertMapper;
import com.myke.excel.entity.UserDO;
import com.myke.excel.service.ResultBO;
import com.myke.excel.service.UserService;
import com.myke.excel.task.DbTaskResultDTO;
import com.myke.excel.task.ExcelWriteTaskThreadService;
import com.myke.excel.task.QueryDataTaskThreadService;
import com.myke.excel.util.ExcelUtil;
import com.myke.excel.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 下载 excel controller
 */
@Slf4j
@RestController
public class DownloadExcelController {

    @Autowired
    private UserService userService;

    /**
     * excel 导出
     *
     * @param queryDTO
     */
    @RequestMapping("/download-excel")
    public void downloadExcel(@RequestBody(required = false) QueryParamDTO queryDTO,
                              HttpServletResponse response) {

        OutputStream os = null;

        if (null == queryDTO) {
            queryDTO = new QueryParamDTO();
        }

        //设置分页信息
        queryDTO.setCurrentPage(1);
        queryDTO.setPageSize(5000);

        //创建阻塞队列，FIFO
        LinkedBlockingQueue<DbTaskResultDTO> linkedQueue =
                ThreadUtil.createLinkedQueue(10);

        //创建线程池
        ExecutorService executorService =
                ThreadUtil.createThreadPool("download-excel");
        //创建工作薄
        SXSSFWorkbook workBook = ExcelUtil.createWorkBook();
        Sheet sheet = ExcelUtil.createSheet(workBook, "demo-test");

        //写 sheet 头，列名
        userService.writeExcelHead(sheet);

        //记录 excel写完所有数据的标志,1 代表只有一个线程负责将数据写入excel中
        CountDownLatch latch = new CountDownLatch(1);
        //启动 写excel的线程
        executorService.execute(
                new ExcelWriteTaskThreadService(latch, sheet, linkedQueue, userService)
        );

        //查询db总条数
        ResultBO<UserDO> userDOResultBO = userService.dbQueryData(queryDTO);
        long pageCount = userDOResultBO.getPageCount();

        //提交db数据查询任务
        for (int pageNum = 1; pageNum <= pageCount; pageNum++) {
            queryDTO.setCurrentPage(pageNum);
            //使用新的对象,因为多线程使用一个对象时产生线程安全问题
            QueryParamDTO queryParamDTO =
                    UserDoConvertMapper.INSTANCE.convert(queryDTO);
            QueryDataTaskThreadService queryDataTaskThreadService =
                    new QueryDataTaskThreadService(linkedQueue, queryParamDTO, userService);
            executorService.execute(queryDataTaskThreadService);
        }

        try {
            //等待excel写任务执行完成后，再向下继续执行
            latch.await();

            // 设置下载类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            //设置文件的名称
            response.setHeader("content-disposition", "attachment;filename=" + sheet.getSheetName() + ".xlsx");

            os = response.getOutputStream();
            workBook.write(os);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                //  处理SXSSFWorkbook导出excel时，删除产生的临时文件
                workBook.dispose();
            }
            try {
                if (os != null) {
                    // 关闭输出流
                    os.close();
                }
            } catch (Exception e) {
                log.error("excel 导出异常", e);
            }
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }


}
