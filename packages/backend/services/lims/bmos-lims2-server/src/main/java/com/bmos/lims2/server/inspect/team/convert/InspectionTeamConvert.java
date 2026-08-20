package com.bmos.lims2.server.inspect.team.convert;

import com.bmos.lims2.server.inspect.team.dto.InspectionTeamSaveDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamUpdateDTO;
import com.bmos.lims2.server.inspect.team.entity.InspectionTeam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InspectionTeamConvert {

    InspectionTeamConvert INSTANCE = Mappers.getMapper(InspectionTeamConvert.class);

    InspectionTeam convert2TeamDO(InspectionTeamSaveDTO dto);

    InspectionTeam convert2TeamDO(InspectionTeamUpdateDTO dto);
}
