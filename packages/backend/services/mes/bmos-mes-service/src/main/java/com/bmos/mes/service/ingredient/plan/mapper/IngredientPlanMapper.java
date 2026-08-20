package com.bmos.mes.service.ingredient.plan.mapper;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.ingredient.input.dto.PendingInputPlanListQueryListDTO;
import com.bmos.mes.service.ingredient.plan.model.IngredientPlan;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientPlanItemVO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IngredientPlanMapper extends BaseMapperX<IngredientPlan> {

    default Integer selectNextSerialNo(Long productPlanId) {
        Integer max = selectMaxSerialNo(productPlanId);
        return ObjectUtil.isNull(max) ? 1 : max + 1;
    }

    Integer selectMaxSerialNo(Long productPlanId);

    default IngredientPlan selectByModelAndPlanAndComponent(ProcedureStepModel procedureStepModel, Long productPlanId, Long componentId) {
        return selectOne(new LambdaQueryWrapperX<IngredientPlan>()
                .eq(IngredientPlan::getProductPlanId, productPlanId)
                .eq(IngredientPlan::getRecordItemId, procedureStepModel.getRecordItemId())
                .eq(IngredientPlan::getComponentId, componentId)
                .eq(IngredientPlan::getProcedureStepModelId, BooleanUtil.isTrue(procedureStepModel.getReusable()) ? 0 : procedureStepModel.getId()));
    }

    /**
     * 查询待称量的配料计划
     *
     * @param productPlanId 产品计划id
     * @param batchNo       批次号
     * @return 待称量的配料计划列表
     */
    List<IngredientPlanItemVO> queryPendingIngredientPlanList(@Param("productPlanId") Long productPlanId, @Param("batchNo") String batchNo);

    /**
     * 查询已称量待投料计划
     *
     * @return 已称量待投料计划
     */
    List<IngredientPlanItemVO> queryPendingInputPlanList(PendingInputPlanListQueryListDTO dto);

    default IngredientPlan selectUnique(UniqueComponentQueryDTO dto) {
        return selectOne(new LambdaQueryWrapperX<IngredientPlan>()
                .eq(IngredientPlan::getProductPlanId, dto.getProductPlanId())
                .eq(IngredientPlan::getComponentId, dto.getComponentId())
                .eq(IngredientPlan::getCopyVersion, dto.getCopyVersion())
                .eq(IngredientPlan::getRecordItemId, dto.getRecordItemId())
                .eq(IngredientPlan::getRecordVersionId, dto.getRecordVersionId())
                .eq(IngredientPlan::getReuse, dto.getReuse())
                .eq(!dto.getReuse(), IngredientPlan::getProcedureStepModelId, dto.getProcedureStepModelId()));
    }
}
