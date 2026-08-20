package com.bmos.mes.service.plan.instruction.mapper;

import com.bmos.mes.common.enums.plan.InstructionStatusEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mes.service.plan.instruction.dto.TeamDetailQueryDTO;
import com.bmos.mes.service.plan.instruction.model.Instruction;
import com.bmos.mes.service.plan.instruction.vo.InstructionPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface InstructionMapper extends BaseMapperX<Instruction> {
    /**
     * 分页查询
     * @param dto dto
     * @return List<InstructionPageVO>
     */
    List<InstructionPageVO> page(PlanPageDTO dto);

    Integer waitTaskCount(@Param("userIds") List<Long> userId,@Param("processIds") List<Long> processIdList);

    /**
     * 分页查询
     * @param dto dto
     * @return List<InstructionPageVO>
     */
    List<PlanPageVO> startPage(PlanPageDTO dto);

    default List<Instruction> selectByProductPlanId(Long productPlanId) {
        return selectList(new LambdaQueryWrapperX<Instruction>()
            .eq(Instruction::getProductPlanId, productPlanId)
        );
    }

    default Instruction selectByCondition(String nodeId, Long productPlanId) {
        return selectOne(new LambdaQueryWrapperX<Instruction>()
            .eq(Instruction::getProductPlanId, productPlanId)
            .eq(Instruction::getNodeId, nodeId)
        );
    }

    default Long countByCondition(Long productPlanId) {
        return selectCount(new LambdaQueryWrapperX<Instruction>()
            .eq(Instruction::getProductPlanId, productPlanId)
        );
    }

    default void resolve(Long id, Long principal) {
        updateById(Instruction.builder().principal(principal).id(id).build());
    }

    default void confirm(Long id, String confirmUserId) {
        updateById(Instruction.builder().status(InstructionStatusEnum.CONFIRM)
                .id(id).confirmUserId(confirmUserId).build());
    }

    default boolean existsResolve(Long productPlanId) {
        return exists(new LambdaQueryWrapperX<Instruction>()
            .eq(Instruction::getProductPlanId, productPlanId)
            .eq(Instruction::getStatus, InstructionStatusEnum.RESOLVE)
            .last(" limit 1 ")
        );
    }

    default void batchConfirm(Collection<Long> instructionIds, String userId){
        List<Instruction> collect = instructionIds.stream()
                .map(e -> Instruction.builder()
                        .status(InstructionStatusEnum.CONFIRM)
                        .id(e)
                        .confirmUserId(userId)
                        .build())
                .collect(Collectors.toList());
        updateBatch(collect);
    }

    default List<Instruction> queryListByPlanIdAndProcedureModelId(TeamDetailQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<Instruction>()
                .eq(Instruction::getProductPlanId, dto.getPlanId())
                .eq(dto.getNodeFunction().equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue()),
                        Instruction::getProcedureModelId,dto.getProcedureModelId())
        );
    }
}
