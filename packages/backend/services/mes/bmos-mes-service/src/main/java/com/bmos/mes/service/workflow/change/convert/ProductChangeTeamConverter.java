package com.bmos.mes.service.workflow.change.convert;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;
import com.bmos.mes.service.workflow.change.dto.TeamListDTO;
import com.bmos.mes.service.workflow.change.model.ProductChangeTeam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Mapper
public interface ProductChangeTeamConverter {
    ProductChangeTeamConverter INSTANCE = Mappers.getMapper(ProductChangeTeamConverter.class);

    default List<ProductChangeTeam> convertToChangeTeam(List<TeamListDTO> teamListDto, Integer changeTeamNumber,String nodeFunction) {
        return teamListDto.stream().map(item -> {
            ProductChangeTeam team = new ProductChangeTeam();
            team.setChangeTeamNumber(changeTeamNumber);
            team.setProductInstructionTeamId(item.getProductInstructionTeamId());
            team.setTeamIds(item.getTeamIds());
            team.setChangeTeamType(nodeFunction);
            return team;
        }).collect(Collectors.toList());
    }

    default List<InstructionTeamVO> covertToFreshTeamVo(List<InstructionTeamVO> vos){
        Map<Long, List<InstructionTeamVO>> teamMap = CollectionUtils.convertMultiMap(vos, InstructionTeamVO::getId);
        List<InstructionTeamVO> freshVo = new ArrayList<>();
        teamMap.forEach((key,value)->{
            freshVo.add(CollectionUtils.getFirst(value));
        });
        return freshVo;
    }
}
