package com.bmos.mes.service.record.convert;

import com.bmos.mes.service.record.dto.*;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.vo.ItemBaseInfoVO;
import com.bmos.mes.service.record.vo.RecordItemVO;
import com.bmos.mes.service.record.vo.SaveSingleItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RecordItemConvert {
    RecordItemConvert INSTANCE = Mappers.getMapper(RecordItemConvert.class);


    @Mapping(target = "docxHeader", expression = "java(dto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(dto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxFooter()))")
    BatchRecordItem convertToItemDo(RecordItemSaveDTO dto);

    @Mapping(target = "docxHeader", expression = "java(dto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(dto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxFooter()))")
    BatchRecordItem convertToItemListDO(RecordComponentSaveDTO dto);

    List<BatchRecordItem> convertToItemListDO(List<RecordComponentSaveDTO> dto);

    @Mapping(target = "docxHeader", expression = "java(itemListDto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(itemListDto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(itemListDto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(itemListDto.getDocxFooter()))")
    BatchRecordItem convertToItemList(RecordItemListDTO itemListDto);

    List<BatchRecordItem> convertToItemList(List<RecordItemListDTO> itemListDto);

    @Mapping(target = "docxHeader", expression = "java(item.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.parseObject(item.getDocxHeader(), com.bmos.file.docx.model.DocxHeader.class))")
    @Mapping(target = "docxFooter", expression = "java(item.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.parseObject(item.getDocxFooter(), com.bmos.file.docx.model.DocxFooter.class))")
    RecordItemVO convertToItemVo(BatchRecordItem item);

    List<ItemBaseInfoVO> convert2ItemBaseInfo(List<BatchRecordItem> batchRecordItems);

    @Mapping(target = "docxHeader", expression = "java(dto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(dto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxFooter()))")
    BatchRecordItem convertToItemListDO(RecordItemSingleEditDTO dto);

    @Mapping(target = "docxHeader", expression = "java(dto.getDocxHeader() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxHeader()))")
    @Mapping(target = "docxFooter", expression = "java(dto.getDocxFooter() == null ? null : com.bmos.common.util.json.JsonUtils.toJsonString(dto.getDocxFooter()))")
    BatchRecordItem convertToItemDo(RecordItemSingleSaveDTO dto);

    SaveSingleItemVO convert2SaveVO(BatchRecordItem item);
}
