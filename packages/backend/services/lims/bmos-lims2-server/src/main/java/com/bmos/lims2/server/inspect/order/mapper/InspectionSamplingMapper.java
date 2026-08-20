package com.bmos.lims2.server.inspect.order.mapper;

import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionSampling;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验取样信息Mapper
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface InspectionSamplingMapper extends BaseMapperX<InspectionSampling> {

    /**
     * 根据检验单ID查询取样信息（包含关联信息）
     * @param inspectionOrderId 检验单ID
     * @return 取样信息列表
     */
    List<InspectionSamplingDTO> selectByInspectionOrderIdWithRelation(@Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 根据检验单ID查询取样信息
     * @param inspectionOrderId 检验单ID
     * @return 取样信息列表
     */
    default List<InspectionSampling> selectByInspectionOrderId(Long inspectionOrderId) {
        return selectList(new LambdaQueryWrapperX<InspectionSampling>()
                .eq(InspectionSampling::getInspectionOrderId, inspectionOrderId));
    }

    /**
     * 根据检验项目ID查询取样信息
     * @param inspectItemId 检验项目ID
     * @return 取样信息列表
     */
    default List<InspectionSampling> selectByInspectItemId(Long inspectItemId) {
        return selectList(new LambdaQueryWrapperX<InspectionSampling>()
                .eq(InspectionSampling::getInspectItemId, inspectItemId));
    }

    /**
     * 根据检验单ID删除取样信息
     * @param inspectionOrderId 检验单ID
     * @return 删除数量
     */
    default int deleteByInspectionOrderId(Long inspectionOrderId) {
        return delete(new LambdaQueryWrapperX<InspectionSampling>()
                .eq(InspectionSampling::getInspectionOrderId, inspectionOrderId));
    }
}