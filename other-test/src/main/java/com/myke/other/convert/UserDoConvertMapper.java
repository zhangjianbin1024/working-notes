package com.myke.other.convert;

import com.myke.other.dto.QueryParamDTO;
import com.myke.other.dto.UserDTO;
import com.myke.other.entity.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDoConvertMapper {
    UserDoConvertMapper INSTANCE = Mappers.getMapper(UserDoConvertMapper.class);

    @Mappings({})
    QueryParamDTO convertQueryParamDTO(QueryParamDTO queryParamDTO);

    @Mappings({})
    UserDTO convertUserDTO(UserDO userDO);

    @Mappings({})
    UserDO convertUserDO(UserDTO userDTO);
}