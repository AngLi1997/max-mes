package com.bmos.mes.service.utils;

import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDetailDTO;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailItemVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    BaseUserDO convertVO(UserInfoVO dto);
}
