package com.myke.other.easyexcel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author zhangjianbin
 * @date 2021年07月27日14:41
 */
public class EasyExcelUtils {

    @SuppressWarnings("rawtypes")
    public static void webWriteExcel(HttpServletResponse response, List objects, Class clazz, String fileName) throws IOException {
        String sheetName = fileName;
        webWriteExcel(response, objects, clazz, fileName, sheetName);
    }


    @SuppressWarnings("rawtypes")
    public static void webWriteExcel(HttpServletResponse response, List objects, Class clazz, String fileName, String sheetName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为白
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        // 设置样式,头是头的样式 内容是内容的样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        ServletOutputStream outputStream = response.getOutputStream();
        try {
            EasyExcelFactory
                    .write(outputStream, clazz)
                    //自定义表格样式
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet(sheetName)
                    .doWrite(objects);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }

    //public static void webWriteExcel(HttpServletResponse response, List excelList, Class excelClass, String sheetName) {
    //    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    //    response.setContentType("application/vnd.ms-excel");
    //    response.setCharacterEncoding("utf-8");
    //
    //    // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
    //    String fileName = null;
    //    try {
    //        fileName = URLEncoder.encode("测试", "UTF-8");
    //        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    //    } catch (UnsupportedEncodingException e) {
    //        e.printStackTrace();
    //    }
    //
    //    try {
    //        EasyExcel.write(response.getOutputStream(), excelClass).sheet(sheetName).doWrite(excelList);
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}
}