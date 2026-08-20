package com.bmos.mes.service.plan.team.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.instruction.model.Instruction;
import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDetailDTO;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailItemVO;
import com.bmos.mes.service.process.vo.ProcedureModelDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Mapper
public interface InstructionTeamConverter {
    InstructionTeamConverter INSTANCE = Mappers.getMapper(InstructionTeamConverter.class);

    default List<InstructionTeam> convertList(InstructionTeamConfirmDTO dto) {
        return dto.getDetails().stream()
            .map(detail -> {
                InstructionTeam instructionTeam = convertDO(dto);
                fillInstructionTeamField(instructionTeam, detail);
                return instructionTeam;
            })
            .collect(Collectors.toList());
    }

    InstructionTeam convertDO(InstructionTeamConfirmDTO dto);

    void fillInstructionTeamField(@MappingTarget InstructionTeam instructionTeam, InstructionTeamConfirmDetailDTO dto);

    List<InstructionTeamDetailItemVO> convertList(List<InstructionTeam> instructionTeams);

    List<InstructionTeamVO> convertChangeTeamList(List<InstructionTeam> instructionTeams);

    /**
     * 换班班组信息数据
     * @param instructions 生产计划指令单信息
     * @param procedureModel 工序模型信息
     * @param plan 计划信息
     * @return teamList
     */
    default List<InstructionTeam> convertToSaveTeamList(List<Instruction> instructions, List<ProcedureModelDetailVO> procedureModel,
                                                        Plan plan){
        if (CollUtil.isEmpty(instructions)){
            return Collections.emptyList();
        }
        Map<Long, ProcedureModelDetailVO> modelDetail = CollectionUtils.convertMap(procedureModel, ProcedureModelDetailVO::getId);
        List<InstructionTeam> teams = new ArrayList<>();
        instructions.forEach(item->{
            ProcedureModelDetailVO modelDetailVO = modelDetail.get(item.getProcedureModelId());
            modelDetailVO.getSteps().forEach(step->{
                InstructionTeam team = new InstructionTeam();
                team.setInstructionId(item.getId());
                team.setProductPlanId(plan.getId());
                team.setNodeId(modelDetailVO.getNodeId());
                team.setProcedureModelId(modelDetailVO.getId());
                team.setProcedureId(modelDetailVO.getProcedureId());
                AtomicInteger sort = new AtomicInteger(1);
                team.setId(IdUtils.getSnowflake());
                team.setSort(sort.getAndIncrement());
                team.setProcedureStepId(step.getProcedureStepId());
                team.setProcedureStepModelId(step.getId());
                team.setProcedureStepModelName(step.getName());
                team.setProcedureStepTime(step.getDuration());
                team.setProcedureStepTimeUnit(step.getTimeUnit());
                team.setNodeStepId(step.getNodeId());
                team.setTeamIds(CollUtil.isEmpty(step.getGroupIds()) ? modelDetailVO.getGroupIds() : step.getGroupIds());
                teams.add(team);
            });
        });
        return teams;
    }
}
