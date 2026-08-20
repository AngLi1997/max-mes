package com.bmos.lims2.server.eln.entry.mapper;
import com.bmos.lims2.server.eln.entry.dto.RecordItemLatestDataQueryDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface ExecuteFormDataMapper extends BaseMapperX<ExecuteFormData> {


    /**
     * 根据生产计划id和记录项id列表查询数据
     * 包含作废数据
     *
     * @param inspectOrderId
     * @param recordItemIds
     * @return
     */
    default List<ExecuteFormData> selectByProductPlanIdAndItemIdsWithDiscard(Long inspectOrderId, Collection<Long> recordItemIds) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getInspectionOrderId, inspectOrderId)
                .in(ExecuteFormData::getRecordItemId, recordItemIds)
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default Long getNextRev(Long productPlanId, Collection<Long> fields) {
        Long maxRev = selectMaxRev(productPlanId, new HashSet<>(fields));
        return maxRev == null ? 0 : maxRev + 1;
    }


    Long selectMaxRev(@Param("inspectionOrderId") Long inspectionOrderId, @Param("fields") Set<Long> fields);


    default Integer countModifyFieldByPlanId(Long inspectOrderId) {
        List<ExecuteFormData> list = selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getInspectionOrderId, inspectOrderId)
                .eq(ExecuteFormData::getOperationType, ExecuteFormDataType.MODIFY.getValue())
                .groupBy(ExecuteFormData::getFieldId)
                .groupBy(ExecuteFormData::getRecordId)
        );
        return list.size();
    }

    /**
     * 按任务ID与记录项ID列表查询数据（不含作废），按fieldId/operationTime倒序
     * 用于打印/预览填充任务数据
     */
    default List<ExecuteFormData> selectByTaskIdAndItemIds(Long taskId) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getTaskId, taskId)
                .eq(ExecuteFormData::getDiscard, false)
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    /**
     * 查询任务下指定组件的最新一条数据（按 operation_time 倒序）
     */
    default ExecuteFormData selectLatestByTaskIdAndFieldId(Long taskId, Long fieldId) {
        return selectOne(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getTaskId, taskId)
                .eq(ExecuteFormData::getFieldId, fieldId)
                .eq(ExecuteFormData::getDiscard, false)
                .orderByDesc(ExecuteFormData::getOperationTime)
                .last("limit 1"));
    }

    default List<ExecuteFormData> selectByQueryDTO(RecordItemLatestDataQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getInspectionOrderId, dto.getInspectionOrderId())
                .eq(ExecuteFormData::getParameterConfigId, dto.getParameterConfigId())
                .eq(ExecuteFormData::getRecordItemId, dto.getRecordItemId())
                .eqIfPresent(ExecuteFormData::getDiscard, dto.getDiscard())
                .inIfPresent(ExecuteFormData::getFieldId, dto.getFieldIdList())
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }
}
