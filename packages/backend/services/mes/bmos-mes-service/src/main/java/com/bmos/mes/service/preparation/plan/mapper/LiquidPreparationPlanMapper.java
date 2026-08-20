package com.bmos.mes.service.preparation.plan.mapper;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.preparation.measure.vo.UnmeasuredPreparationPlanVO;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LiquidPreparationPlanMapper extends BaseMapperX<LiquidPreparationPlan> {


    default LiquidPreparationPlan selectUnique(UniqueComponentQueryDTO dto){
        return selectOne(new LambdaQueryWrapperX<LiquidPreparationPlan>()
                .eq(LiquidPreparationPlan::getProductPlanId, dto.getProductPlanId())
                .eq(LiquidPreparationPlan::getComponentId, dto.getComponentId())
                .eq(LiquidPreparationPlan::getCopyVersion, dto.getCopyVersion())
                .eq(LiquidPreparationPlan::getRecordItemId, dto.getRecordItemId())
                .eq(LiquidPreparationPlan::getRecordVersionId, dto.getRecordVersionId())
                .eq(LiquidPreparationPlan::getReuse, dto.getReuse())
                .eq(!dto.getReuse(), LiquidPreparationPlan::getProcedureStepModelId, dto.getProcedureStepModelId()));
    }

    default Integer selectNextSerialNo(Long productPlanId){
        Integer max = selectMaxSerialNo(productPlanId);
        return ObjectUtil.isNull(max) ? 1 : max + 1;
    }

    Integer selectMaxSerialNo(Long productPlanId);

    List<UnmeasuredPreparationPlanVO> selectUnmeasuredPreparationPlanList(@Param("productPlanId") Long productPlanId);

    /**
     * 查询当前生产计划下的配液单
     * @param productPlanId
     * @return
     */
    default List<LiquidPreparationPlan> selectByPlanId(Long productPlanId){
        return selectList(new LambdaQueryWrapperX<LiquidPreparationPlan>()
                .eq(LiquidPreparationPlan::getProductPlanId, productPlanId));
    }
}
