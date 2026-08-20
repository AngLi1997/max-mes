package com.bmos.mes.service.preparation.plan.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.preparation.measure.vo.LiquidPreparationDetailBatchVO;
import com.bmos.mes.service.preparation.plan.dto.LiquidPreparationBoundBatchQueryDTO;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatch;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatchDetailInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LiquidPreparationMaterialBatchMapper extends BaseMapperX<LiquidPreparationMaterialBatch> {

    List<LiquidPreparationMaterialBatchDetailInfo> selectDetailListByPreparationPlanId(LiquidPreparationBoundBatchQueryDTO dto);

    default void deleteBoundBatch(LiquidPreparationBoundBatchQueryDTO build){
        delete(new LambdaUpdateWrapper<LiquidPreparationMaterialBatch>()
                .eq(LiquidPreparationMaterialBatch::getLiquidPreparationPlanId, build.getPreparationPlanId())
                .eq(LiquidPreparationMaterialBatch::getFormulaMaterialId, build.getFormulaMaterialId()));
    }

    List<LiquidPreparationDetailBatchVO> selectMeasureInfoByPlanId(@Param("preparationPlanId") Long id);
}
