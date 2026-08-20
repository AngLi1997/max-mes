package com.bmos.lims2.server.eln.entry.mapper;

import com.bmos.lims2.server.eln.entry.entity.ExecuteFormDataAnnotation;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: 执行表单数据-异常批注 Mapper
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Mapper
public interface ExecuteFormDataAnnotationMapper extends BaseMapperX<ExecuteFormDataAnnotation> {

    /**
     * 按历史查询条件获取批注集合（与数据历史查询条件一致）
     */
    default List<ExecuteFormDataAnnotation> selectListByField(Long inspectionOrderId,
                                                             Long parameterConfigId,
                                                             Long fieldId,
                                                             Long taskId) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormDataAnnotation>()
                .eq(ExecuteFormDataAnnotation::getInspectionOrderId, inspectionOrderId)
                .eq(ExecuteFormDataAnnotation::getParameterConfigId, parameterConfigId)
                .eq(ExecuteFormDataAnnotation::getFieldId, fieldId)
                .eq(ExecuteFormDataAnnotation::getTaskId, taskId)
                .orderByAsc(ExecuteFormDataAnnotation::getOperationTime));
    }
}


