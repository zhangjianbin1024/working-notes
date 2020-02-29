package com.myke.excel.convert;

import com.myke.excel.web.QueryParamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDoConvertMapper {
    UserDoConvertMapper INSTANCE = Mappers.getMapper(UserDoConvertMapper.class);

    @Mappings({})
    QueryParamDTO convert(QueryParamDTO queryParamDTO);
}