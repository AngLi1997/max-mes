package com.bmos.mes.service.plan.team.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.audit.model.FlowAuditCategory;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface InstructionTeamMapper extends BaseMapperX<InstructionTeam> {
    void deleteByInstructionId(@Param("instructionId") Long instructionId);

    void deleteByProductPlanId(@Param("productPlanId") Long productPlanId);

    default List<InstructionTeam> selectByInstructionId(Long instructionId) {
        return selectList(new LambdaQueryWrapperX<InstructionTeam>()
                .eq(InstructionTeam::getInstructionId, instructionId)
                .orderByAsc(InstructionTeam::getSort)
        );
    }

    default List<InstructionTeam> selectByInstructionIds(List<Long> instructionIds) {
        if (CollUtil.isEmpty(instructionIds)) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<InstructionTeam>()
                .in(InstructionTeam::getInstructionId, instructionIds)
        );
    }

    default List<Long> selectTeamIds(Long productPlanId, String nodeStepId) {
        return selectList(new LambdaQueryWrapperX<InstructionTeam>()
                .eq(InstructionTeam::getProductPlanId, productPlanId)
                .eq(InstructionTeam::getNodeStepId, nodeStepId)
                .select(Collections.singletonList(InstructionTeam::getTeamIds)))
                .stream().map(InstructionTeam::getTeamIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    default List<InstructionTeam> selectListByPlanIdS(List<Long> planIds){
        return selectList(new LambdaQueryWrapperX<InstructionTeam>()
                .in(InstructionTeam::getProductPlanId, planIds));
    }

    default InstructionTeam selectListByPlanIdAndNodeStepId(Long planId, String nodeStepId){
        return selectOne(new LambdaQueryWrapperX<InstructionTeam>()
                .eq(InstructionTeam::getProductPlanId,planId)
                .eq(InstructionTeam::getNodeStepId,nodeStepId));
    }

    default List<InstructionTeam> selectByPlanIdAndStepIds(Long productPlanId, List<Long> stepIds){
        return selectList(new LambdaQueryWrapperX<InstructionTeam>()
                .eq(InstructionTeam::getProductPlanId,productPlanId)
                .in(InstructionTeam::getProcedureStepId,stepIds));
    }

    List<InstructionTeam> getInstructionDetailByUserTeamId(@Param("planIds") List<Long> planId);

}
