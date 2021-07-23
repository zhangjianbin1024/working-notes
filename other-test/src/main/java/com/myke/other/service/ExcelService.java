package com.myke.other.service;

import com.myke.other.entity.UserDO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangjianbin
 * @date 2021年07月20日18:01
 */
@Service
public class ExcelService {

    public void writeExcelHead(Sheet sheet) {
        //创建 excel 表头，即列名
        String columnNames[] = {
                "用户名", "性别", "年龄", "密码"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            row.createCell(i).setCellValue(columnNames[i]);
        }
    }

    public void writeExcelCellValue(Sheet sheet, AtomicInteger excelStartNum, List<UserDO> list) {
        int excelDataCount = list.size();
        for (int j = 0; j < excelDataCount; j++) {
            int start = excelStartNum.getAndIncrement();
            int rownum = start;

            Row row = sheet.createRow(rownum);
            UserDO obj = list.get(j);
            row.createCell(0).setCellValue(obj.getUsername());
            row.createCell(1).setCellValue(obj.getSex());
            row.createCell(2).setCellValue(obj.getAge());
            row.createCell(3).setCellValue(obj.getPassword());
        }
    }

}