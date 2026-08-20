package com.bmos.lims2.server.eln.record.convert;

import com.bmos.lims2.server.eln.record.dto.*;
import com.bmos.lims2.server.eln.record.entity.BatchRecordParse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface RecordParseConvert {
    RecordParseConvert INSTANCE = Mappers.getMapper(RecordParseConvert.class);

    @Mapping(target = "docxHeader", expression = "java(itemListDto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(itemListDto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(itemListDto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(itemListDto.getDocxFooter()))")
    BatchRecordParse convertToParseList(RecordItemListDTO itemListDto);

    List<BatchRecordParse> convertToParseList(List<RecordItemListDTO> dtos);

    @Mapping(target = "docxHeader", expression = "java(saveDTO.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(saveDTO.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(saveDTO.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(saveDTO.getDocxFooter()))")
    BatchRecordParse convertToParse(RecordItemSaveDTO saveDTO);


    @Mapping(target = "docxHeader", expression = "java(saveDTO.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(saveDTO.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(saveDTO.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(saveDTO.getDocxFooter()))")
    BatchRecordParse convertToParseComponent(RecordComponentSaveDTO saveDTO);

    List<BatchRecordParse> convertToParseComponent(List<RecordComponentSaveDTO> dto);

    @Mapping(target = "docxHeader", expression = "java(dto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(dto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxFooter()))")
    BatchRecordParse convertToParseComponent(RecordItemSingleEditDTO dto);

    @Mapping(target = "docxHeader", expression = "java(dto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(dto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxFooter()))")
    BatchRecordParse convertToParse(RecordItemSingleSaveDTO dto);
}
