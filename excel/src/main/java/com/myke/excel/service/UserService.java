package com.myke.excel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myke.excel.entity.UserDO;
import com.myke.excel.mapper.UserMapper;
import com.myke.excel.web.QueryParamDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

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


    /**
     * 查询 db 数据
     *
     * @param dto
     *
     * @return
     */
    public ResultBO<UserDO> dbQueryData(QueryParamDTO dto) {
        Integer currentPage = dto.getCurrentPage();
        Integer pageSize = dto.getPageSize();

        IPage<UserDO> page = new Page(currentPage, pageSize);

        IPage<UserDO> usersIPage = userMapper.selectPage(page, null);

        //总页数
        long pages = usersIPage.getPages();
        //当前页数据
        List<UserDO> users = usersIPage.getRecords();
        //获取总记录数
        long total = usersIPage.getTotal();
        //当前页
        long current = usersIPage.getCurrent();

        ResultBO<UserDO> boResult = new ResultBO<>();
        boResult.setData(users);
        boResult.setPageCount(pages);
        boResult.setDataTotal(total);
        boResult.setCurrentPage(current);
        return boResult;
    }


}
