package com.bmos.mes.service.workflow.change.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.change.model.ProductChangeTeam;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductChangeTeamMapper extends BaseMapperX<ProductChangeTeam> {

    List<ProductChangeTeam> queryByPlanIdAndProcedureModelId(ChangeTeamDTO teamDTO);

    List<InstructionTeamVO> selectListByPlanId(@Param("planId") Long planId,@Param("nodeFunction") String nodeFunction,
                                               @Param("changeNumber") Integer changeNumber);

    default List<Long> queryByInstructionIdAndChangeType(Long instructionId, Integer procedureNumber,String changeType){
        List<ProductChangeTeam> productChangeTeams = selectList(new LambdaQueryWrapperX<ProductChangeTeam>()
                .eq(ProductChangeTeam::getProductInstructionTeamId, instructionId)
                .eq(ProductChangeTeam::getChangeTeamType,changeType)
                .eq(ProductChangeTeam::getChangeTeamNumber,procedureNumber));
        ProductChangeTeam first = CollectionUtils.getFirst(productChangeTeams);
        return ObjectUtil.isEmpty(first) || CollUtil.isEmpty(first.getTeamIds()) ? Collections.emptyList() : first.getTeamIds();
    }

    default List<ProductChangeTeam> selectListByInstructionId(List<Long> instructionId){
        return selectList(new LambdaQueryWrapperX<ProductChangeTeam>()
                    .in(ProductChangeTeam::getProductInstructionTeamId,instructionId)
                    .orderByAsc(ProductChangeTeam::getChangeTeamType)
                    .orderByDesc(ProductChangeTeam::getChangeTeamNumber));
    }

    default List<ProductChangeTeam> selectByIds(List<Long> changeTeamId){
        return selectList(new LambdaQueryWrapperX<ProductChangeTeam>().in(ProductChangeTeam::getId,changeTeamId));
    }

    default ProductChangeTeam selectOneByChangeNumberAndChangeType(Long id, Integer changeNumber, String nodeFunction){
        return selectOne(new LambdaQueryWrapperX<ProductChangeTeam>()
                .eq(ProductChangeTeam::getChangeTeamNumber,changeNumber)
                .eq(ProductChangeTeam::getChangeTeamType,nodeFunction)
                .eq(ProductChangeTeam::getProductInstructionTeamId,id)
                .last("limit 1"));
    }
}
