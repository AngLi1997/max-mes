package com.bmos.lims2.server.inspect.team.mapper;

import com.bmos.lims2.server.inspect.team.dto.InspectionTeamDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamPageReqDTO;
import com.bmos.lims2.server.inspect.team.entity.InspectionTeam;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InspectionTeamMapper extends BaseMapperX<InspectionTeam> {


    default InspectionTeam selectByCode(String code) {
        return selectOne(new LambdaQueryWrapperX<InspectionTeam>()
                .eq(InspectionTeam::getCode, code));
    }

    List<InspectionTeamDTO> queryPage(InspectionTeamPageReqDTO dto);

    List<InspectionTeamDTO> queryList(InspectionTeamPageReqDTO dto);
}
