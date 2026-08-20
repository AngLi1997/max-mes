package com.bmos.mes.service.plan.team.convert;

import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDetailDTO;
import com.bmos.mes.service.plan.team.dto.ProductPlanTeamSaveDTO;
import com.bmos.mes.service.plan.team.dto.ProductPlanTeamUpdateDTO;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.model.ProductPlanTeam;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailItemVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductPlanTeamConverter {
    ProductPlanTeamConverter INSTANCE = Mappers.getMapper(ProductPlanTeamConverter.class);

    ProductPlanTeam convertDO(ProductPlanTeamSaveDTO dto);

    ProductPlanTeam convertDO(ProductPlanTeamUpdateDTO dto);

    ProductPlanTeamDetailVO convertVO(ProductPlanTeam productPlanTeam);
}
