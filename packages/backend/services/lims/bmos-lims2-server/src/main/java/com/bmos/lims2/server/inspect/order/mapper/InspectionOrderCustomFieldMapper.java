package com.bmos.lims2.server.inspect.order.mapper;

import com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检验单自定义字段值Mapper接口
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface InspectionOrderCustomFieldMapper extends BaseMapperX<InspectionOrderCustomField> {

    /**
     * 根据检验单ID查询自定义字段值列表
     * @param inspectionOrderId 检验单ID
     * @return 自定义字段值列表
     */
    default List<InspectionOrderCustomField> selectByInspectionOrderId(Long inspectionOrderId) {
        return selectList(new LambdaQueryWrapperX<InspectionOrderCustomField>()
                .eq(InspectionOrderCustomField::getInspectionOrderId, inspectionOrderId)
                .orderByAsc(InspectionOrderCustomField::getSort));
    }

    /**
     * 根据检验单ID删除自定义字段值
     * @param inspectionOrderId 检验单ID
     * @return 删除数量
     */
    default int deleteByInspectionOrderId(Long inspectionOrderId) {
        return delete(new LambdaQueryWrapperX<InspectionOrderCustomField>()
                .eq(InspectionOrderCustomField::getInspectionOrderId, inspectionOrderId));
    }

    /**
     * 根据检验单ID和字段代码查询
     * @param inspectionOrderId 检验单ID
     * @param fieldCode 字段代码
     * @return 自定义字段值
     */
    default InspectionOrderCustomField selectByOrderIdAndFieldCode(Long inspectionOrderId, String fieldCode) {
        return selectOne(new LambdaQueryWrapperX<InspectionOrderCustomField>()
                .eq(InspectionOrderCustomField::getInspectionOrderId, inspectionOrderId)
                .eq(InspectionOrderCustomField::getFieldCode, fieldCode));
    }

    /**
     * 批量根据检验单ID集合查询自定义字段值，并按检验单ID分组返回
     */
    default Map<Long, List<InspectionOrderCustomField>> selectByInspectionOrderIds(List<Long> inspectionOrderIds) {
        if (inspectionOrderIds == null || inspectionOrderIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        List<InspectionOrderCustomField> list = selectList(new LambdaQueryWrapperX<InspectionOrderCustomField>()
                .in(InspectionOrderCustomField::getInspectionOrderId, inspectionOrderIds)
                .orderByAsc(InspectionOrderCustomField::getSort));
        if (list == null || list.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(InspectionOrderCustomField::getInspectionOrderId));
    }

    /**
     * 批量插入自定义字段值
     * @param customFields 自定义字段值列表
     * @return 插入数量
     */
    default int batchInsert(List<InspectionOrderCustomField> customFields) {
        if (customFields == null || customFields.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (InspectionOrderCustomField customField : customFields) {
            count += insert(customField);
        }
        return count;
    }
}