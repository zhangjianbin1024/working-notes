package com.myke.other.common.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtil {
    /**
     * 创建sheet
     *
     * @param sxssfWorkbook
     * @param sheetName
     *
     * @return
     */
    public static Sheet createSheet(SXSSFWorkbook sxssfWorkbook, String sheetName) {
        return sxssfWorkbook.createSheet(sheetName);
    }

    /**
     * 创建工作薄
     *
     * @return
     */
    public static SXSSFWorkbook createWorkBook() {
        // 内存中保留 1000 条数据，以免内存溢出，其余写入 硬盘
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        //产生的临时文件进行压缩
        workbook.setCompressTempFiles(true);
        return workbook;
    }
}
