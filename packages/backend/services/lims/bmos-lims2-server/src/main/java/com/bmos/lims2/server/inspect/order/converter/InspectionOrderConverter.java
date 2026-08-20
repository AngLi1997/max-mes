package com.bmos.lims2.server.inspect.order.converter;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderSaveDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 检验单对象转换器
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface InspectionOrderConverter {

    InspectionOrderConverter INSTANCE = Mappers.getMapper(InspectionOrderConverter.class);

    /**
     * Entity列表转DTO列表
     * @param entityList 实体对象列表
     * @return DTO对象列表
     */
    List<InspectionOrderDTO> toDTOList(List<InspectionOrder> entityList);

    /**
     * SaveDTO转Entity
     * @param saveDTO 保存DTO对象
     * @return 实体对象
     */
    InspectionOrder toEntity(InspectionOrderSaveDTO saveDTO);

    // 注意：Service层converter只处理Entity ↔ DTO转换
    // VO ↔ DTO转换由Controller层处理

    /**
     * 单据状态枚举转描述
     * @param orderStatus 单据状态枚举
     * @return 状态描述
     */
    @Named("orderStatusToDesc")
    default String orderStatusToDesc(InspectionOrderStatusEnum orderStatus) {
        return orderStatus != null ? orderStatus.getName() : null;
    }

    // 注意：其他状态转换方法由对应的功能模块实现
    // 请验阶段只需要处理单据状态转换
}