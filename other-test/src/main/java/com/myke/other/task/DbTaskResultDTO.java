package com.myke.other.task;

import com.myke.other.bo.ResultBO;
import com.myke.other.dto.QueryParamDTO;
import lombok.Data;

@Data
public class DbTaskResultDTO {
    private ResultBO resultBO;
    private QueryParamDTO queryParamDTO;

}