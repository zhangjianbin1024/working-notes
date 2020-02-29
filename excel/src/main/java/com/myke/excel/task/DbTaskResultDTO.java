package com.myke.excel.task;

import com.myke.excel.service.ResultBO;
import com.myke.excel.web.QueryParamDTO;
import lombok.Data;

@Data
public class DbTaskResultDTO {
    private ResultBO resultBO;
    private QueryParamDTO queryParamDTO;

}