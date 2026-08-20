package com.bmos.platform.service.utils;

import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    BaseUserDO convertVO(UserInfoVO dto);
}
