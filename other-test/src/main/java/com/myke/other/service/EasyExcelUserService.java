package com.myke.other.service;

import com.myke.other.easyexcel.ExcelCheckErrDto;
import com.myke.other.easyexcel.ExcelCheckManager;
import com.myke.other.easyexcel.ExcelCheckResult;
import com.myke.other.easyexcel.dto.UserExcelDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjianbin
 * @date 2021年07月27日14:34
 */
@Service
public class EasyExcelUserService implements ExcelCheckManager<UserExcelDto> {

    //不合法名字
    public static final String ERR_NAME = "史珍香";


    @Override
    public ExcelCheckResult checkImportExcel(List<UserExcelDto> userExcelDtos) {
        //成功结果集
        List<UserExcelDto> successList = new ArrayList<>();
        //错误数组
        List<ExcelCheckErrDto<UserExcelDto>> errList = new ArrayList<>();
        for (UserExcelDto userExcelDto : userExcelDtos) {
            //错误信息
            StringBuilder errMsg = new StringBuilder();
            //根据自己的业务去做判断
            if (ERR_NAME.equals(userExcelDto.getName())) {
                errMsg.append("请输入正确的名字").append(";");
            }
            if (StringUtils.isBlank(errMsg.toString())) {
                //这里有两个选择，
                // 1、一个返回成功的对象信息，
                // 2、进行持久化操作
                successList.add(userExcelDto);
            } else {
                //添加错误信息
                errList.add(new ExcelCheckErrDto(userExcelDto, errMsg.toString()));
            }
        }
        return new ExcelCheckResult(successList, errList);
    }
}