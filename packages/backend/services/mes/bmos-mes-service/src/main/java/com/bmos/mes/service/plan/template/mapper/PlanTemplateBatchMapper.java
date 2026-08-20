package com.bmos.mes.service.plan.template.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.plan.template.model.PlanTemplateBatch;
import com.bmos.mes.service.plan.template.vo.PlanTemplateDetailBatchVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanTemplateBatchMapper extends BaseMapperX<PlanTemplateBatch> {

    List<PlanTemplateDetailBatchVO> selectDetailByPlanTemplateId(@Param("planTemplateId") Long id);

    default void deleteByPlanTemplateId(Long id) {
        delete(new LambdaUpdateWrapper<PlanTemplateBatch>().eq(PlanTemplateBatch::getPlanTemplateId, id));
    }

    default List<PlanTemplateBatch> selectByProcessIdList(List<Long> longs){
        return selectList(new LambdaQueryWrapperX<PlanTemplateBatch>()
                .in(PlanTemplateBatch::getProcessId, longs));
    }

    default List<PlanTemplateBatch> selectByPlanTemplateId(Long id){
        return selectList(new LambdaQueryWrapperX<PlanTemplateBatch>()
                .eq(PlanTemplateBatch::getPlanTemplateId, id));
    }
}
