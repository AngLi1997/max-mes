package com.bmos.mes.service.components.mapper;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/17 16:45
 */
@Mapper
public interface BusinessComponentInstanceMapper extends BaseMapperX<BusinessComponentInstance> {

    default List<BusinessComponentInstance> listComponentInstanceByProductPlanIdAndComponentType(Long productPlanId, BusinessComponentTypeEnum... componentTypes){
        if (productPlanId == null){
            return new ArrayList<>();
        }
        if (ArrayUtil.isEmpty(componentTypes)) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<BusinessComponentInstance>()
                .eq(BusinessComponentInstance::getProductPlanId, productPlanId)
                .in(BusinessComponentInstance::getComponentType, Arrays.stream(componentTypes).collect(Collectors.toList()))
        );
    }

    default List<BusinessComponentInstance> getComponentInstances(Long productPlanId,
                                                           Long procedureStepId,
                                                           Long recordItemId,
                                                           Long recordVersionId,
                                                           Boolean reuse,
                                                           Long copyVersion){
        return selectList(new LambdaQueryWrapper<BusinessComponentInstance>()
                .eq(BusinessComponentInstance::getProductPlanId, productPlanId)
                .eq(BusinessComponentInstance::getProcedureStepId, procedureStepId)
                .eq(BusinessComponentInstance::getRecordItemId, recordItemId)
                .eq(BusinessComponentInstance::getRecordVersionId, recordVersionId)
                .eq(BusinessComponentInstance::getReuse, reuse)
                .eq(BusinessComponentInstance::getCopyVersion, copyVersion)
        );
    }

    default BusinessComponentInstance getComponentInstance(Long productPlanId, Long procedureStepModelId, Long componentId, Long copyVersion, Boolean reuse){
        return selectOne(new LambdaQueryWrapper<BusinessComponentInstance>()
                .eq(BusinessComponentInstance::getProductPlanId, productPlanId)
                .eq(BusinessComponentInstance::getProcedureStepModelId, procedureStepModelId)
                .eq(BusinessComponentInstance::getComponentId, componentId)
                .eq(BusinessComponentInstance::getCopyVersion, copyVersion)
                .eq(BusinessComponentInstance::getReuse, reuse)
        );
    }
}
