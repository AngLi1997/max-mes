package com.bmos.lims2.server.inspect.entry.convert;

import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.lims2.server.inspect.entry.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 检验录入转换器
 *
 * @author system
 * @since 2025/01/30
 */
@Mapper
public interface InspectionEntryConvert {

    InspectionEntryConvert INSTANCE = Mappers.getMapper(InspectionEntryConvert.class);

    /**
     * VO转DTO - 分析项录入查询
     */
    AnalysisItemEntryQueryDTO toDTO(AnalysisItemEntryQueryVO vo);

    /**
     * VO转DTO - 检验单录入查询
     */
    InspectionOrderEntryQueryDTO toDTO(InspectionOrderEntryQueryVO vo);

    /**
     * VO转DTO - 批量录入
     */
    BatchEntryDTO toDTO(BatchEntryVO vo);

    /**
     * VO转DTO - 批量设置检验时间
     */
    BatchTestTimeDTO toDTO(BatchTestTimeVO vo);

    /**
     * 批量录入项VO转DTO
     */
    BatchEntryDTO.EntryItemDTO toDTO(BatchEntryVO.EntryItemVO vo);

    /**
     * 批量录入项VO转DTO列表
     */
    List<BatchEntryDTO.EntryItemDTO> toDTOList(List<BatchEntryVO.EntryItemVO> voList);
}
