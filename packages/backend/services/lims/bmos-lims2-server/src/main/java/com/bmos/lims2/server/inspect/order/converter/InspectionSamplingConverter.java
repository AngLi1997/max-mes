package com.bmos.lims2.server.inspect.order.converter;

import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingSaveDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionSampling;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 检验取样信息对象转换器
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface InspectionSamplingConverter {

    InspectionSamplingConverter INSTANCE = Mappers.getMapper(InspectionSamplingConverter.class);

    /**
     * Entity转DTO
     * @param entity 实体对象
     * @return DTO对象
     */
    InspectionSamplingDTO toDTO(InspectionSampling entity);

    /**
     * Entity列表转DTO列表
     * @param entityList 实体对象列表
     * @return DTO对象列表
     */
    List<InspectionSamplingDTO> toDTOList(List<InspectionSampling> entityList);

    /**
     * SaveDTO转Entity
     * @param saveDTO 保存DTO对象
     * @return 实体对象
     */
    InspectionSampling toEntity(InspectionSamplingSaveDTO saveDTO);

    // 注意：Service层converter只处理Entity ↔ DTO转换
    // VO ↔ DTO转换由Controller层处理

    // 取样计划不涉及状态管理，状态由后续功能维护
}