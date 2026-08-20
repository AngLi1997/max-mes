package com.bmos.mes.service.output.weigh.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.service.output.weigh.model.OutputWeighProcess;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 15:36
 */
@Mapper
public interface IOutputWeighProcessMapper extends BaseMapperX<OutputWeighProcess> {


    default OutputWeighProcess getOutputWeighProcess(Long productPlanId, Long procedureStepModelId, Long copyVersion, Long componentId, Boolean reuse) {
        if (productPlanId == null || procedureStepModelId == null || copyVersion == null || componentId == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(OutputWeighProcess.class)
                .eq(OutputWeighProcess::getProductPlanId, productPlanId)
                .eq(OutputWeighProcess::getCopyVersion, copyVersion)
                .eq(OutputWeighProcess::getComponentId, componentId)
                .eq(reuse, OutputWeighProcess::getReuse, reuse)
                .eq(!reuse, OutputWeighProcess::getProcedureStepModelId, procedureStepModelId)
        );
    }

    default List<OutputWeighProcess> queryList(List<Long> productPlanIds, List<Long> procedureStepModelIds) {
        if (CollectionUtil.isEmpty(productPlanIds) || CollectionUtil.isEmpty(procedureStepModelIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(OutputWeighProcess.class)
                .in(OutputWeighProcess::getProductPlanId, productPlanIds)
                .in(OutputWeighProcess::getProcedureStepModelId, procedureStepModelIds)
        );
    }
}
