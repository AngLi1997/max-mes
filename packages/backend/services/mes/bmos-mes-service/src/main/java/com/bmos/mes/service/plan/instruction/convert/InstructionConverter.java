package com.bmos.mes.service.plan.instruction.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.plan.instruction.dto.InstructionSaveDTO;
import com.bmos.mes.service.plan.instruction.dto.InstructionUpdateDTO;
import com.bmos.mes.service.plan.instruction.model.Instruction;
import com.bmos.mes.service.plan.instruction.vo.InstructionProcedureVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionVO;
import com.bmos.mes.service.utils.QueryProcessConfigSortUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface InstructionConverter {
    InstructionConverter INSTANCE = Mappers.getMapper(InstructionConverter.class);

    Instruction convertDO(InstructionSaveDTO dto);

    Instruction convertDO(InstructionUpdateDTO dto);

    InstructionVO convertVO(Instruction instruction);

    List<InstructionVO> convertVOs(List<Instruction> instructions);

    default List<InstructionProcedureVO> convertProcedureTeamVO(List<Instruction> instructions) {
        if (CollUtil.isEmpty(instructions)) {
            return new ArrayList<>();
        }
        Map<Long, Integer> procedureModelMap = QueryProcessConfigSortUtils.queryProcedureModelSortByIdList(
                CollectionUtils.convertList(instructions, Instruction::getProcedureModelId));
        return instructions.stream().map(item -> {
            InstructionProcedureVO instructionProcedureVO = new InstructionProcedureVO();
            instructionProcedureVO.setId( item.getId());
            instructionProcedureVO.setProcedureModelName( item.getProcedureModelName());
            instructionProcedureVO.setProcedureModelCode( item.getProcedureModelCode());
            instructionProcedureVO.setSort( procedureModelMap.get(item.getProcedureModelId()));
            return instructionProcedureVO;
        }).collect(Collectors.toList());
    }

    default List<InstructionProcedureVO> convertChangeTeamVo(List<InstructionProcedureVO> vos, List<InstructionTeamVO> teamVOS, Long productionLineId) {
        Map<Long, List<InstructionTeamVO>> teamVoMap = CollectionUtils.convertMultiMap(teamVOS, InstructionTeamVO::getInstructionId);
        vos.forEach(item -> {
            item.setTeams(teamVoMap.get(item.getId()));
            item.setLineIds(productionLineId);
        });
        return vos;
    }
}
