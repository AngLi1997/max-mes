package com.bmos.lims2.server.inspect.order.converter;

import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleSaveDTO;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 样品对象转换器
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface SampleConverter {

    SampleConverter INSTANCE = Mappers.getMapper(SampleConverter.class);

    /**
     * Entity转DTO
     * @param entity 实体对象
     * @return DTO对象
     */
    SampleDTO toDTO(Sample entity);

    /**
     * Entity列表转DTO列表
     * @param entityList 实体对象列表
     * @return DTO对象列表
     */
    List<SampleDTO> toDTOList(List<Sample> entityList);

    /**
     * SaveDTO转Entity
     * @param saveDTO 保存DTO对象
     * @return 实体对象
     */
    Sample toEntity(SampleSaveDTO saveDTO);

    // 注意：Service层converter只处理Entity ↔ DTO转换
    // VO ↔ DTO转换由Controller层处理
}