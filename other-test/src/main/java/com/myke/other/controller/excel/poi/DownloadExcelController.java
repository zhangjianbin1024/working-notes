package com.myke.other.controller.excel.poi;

import com.myke.other.bo.ResultBO;
import com.myke.other.common.utils.ExcelUtil;
import com.myke.other.common.utils.ThreadUtil;
import com.myke.other.convert.UserDoConvertMapper;
import com.myke.other.dto.QueryParamDTO;
import com.myke.other.entity.UserDO;
import com.myke.other.service.PoiExcelUserService;
import com.myke.other.service.UserService;
import com.myke.other.task.DbTaskResultDTO;
import com.myke.other.task.ExcelWriteTaskThreadService;
import com.myke.other.task.QueryDataTaskThreadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 下载 excel controller
 */
@Slf4j
@Controller
@RequestMapping("/poi")
public class DownloadExcelController {

    @Autowired
    private UserService userService;
    @Autowired
    private PoiExcelUserService excelService;

    /**
     * excel 导出
     *
     * @param queryDTO
     */
    //@GetMapping("/download-excel")
    //@PostMapping("/download-excel")
    @RequestMapping("/download-excel")
    public void downloadExcel(@RequestParam(required = false) QueryParamDTO queryDTO,
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
                ThreadUtil.createThreadPool("poi-excel-test");
        //创建工作薄
        SXSSFWorkbook workBook = ExcelUtil.createWorkBook();
        Sheet sheet = ExcelUtil.createSheet(workBook, "poi-excel-test");

        //写 sheet 头，列名
        excelService.writeExcelHead(sheet);

        //记录 excel写完所有数据的标志,1 代表只有一个线程负责将数据写入excel中
        CountDownLatch latch = new CountDownLatch(1);
        //启动 写excel的线程
        executorService.execute(
                new ExcelWriteTaskThreadService(latch, sheet, linkedQueue, userService, excelService)
        );

        //查询db总条数
        ResultBO<UserDO> userDOResultBO = userService.dbQueryData(queryDTO);
        long pageCount = userDOResultBO.getTotalPage();

        //提交db数据查询任务
        for (int pageNum = 1; pageNum <= pageCount; pageNum++) {
            queryDTO.setCurrentPage(pageNum);
            //使用新的对象,因为多线程使用一个对象时产生线程安全问题
            QueryParamDTO queryParamDTO =
                    UserDoConvertMapper.INSTANCE.convertQueryParamDTO(queryDTO);
            QueryDataTaskThreadService queryDataTaskThreadService =
                    new QueryDataTaskThreadService(linkedQueue, queryParamDTO, userService);
            executorService.execute(queryDataTaskThreadService);
        }

        try {
            //等待excel写任务执行完成后，再向下继续执行
            latch.await();

            // 设置下载类型,让服务器告诉浏览器它发送的数据属于什么文件类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            //设置文件的名称,当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型
            response.setHeader("content-disposition", "attachment;filename=" + sheet.getSheetName() + ".xlsx");
            //URLEncoder.encode(sheet.getSheetName(), "UTF-8")


            response.setHeader("Set-Cookie", "fileDownload=true; path=/");

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
