package com.bmos.lims2.server.eln.record.convert;

import com.bmos.lims2.server.eln.record.dto.ComponentListDTO;
import com.bmos.lims2.server.eln.record.dto.SaveFormulaDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponentBO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponentDetail;
import com.bmos.lims2.server.eln.record.entity.BatchRecordItem;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;
import com.bmos.lims2.server.eln.record.vo.ParseComponentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface RecordComponentConvert {
    RecordComponentConvert INSTANCE = Mappers.getMapper(RecordComponentConvert.class);


    List<ComponentListVO> convertToVoList(List<BatchRecordComponent> list);

    List<BatchRecordComponent> convertToDoList(List<ComponentListDTO> list);

    @Mapping(target = "docxHeader", expression = "java(com.bmos.common.util.json.JsonUtils.parseObject(item.getDocxHeader(), com.bmos.file.docx.model.DocxHeader.class))")
    @Mapping(target = "docxFooter", expression = "java(com.bmos.common.util.json.JsonUtils.parseObject(item.getDocxFooter(), com.bmos.file.docx.model.DocxFooter.class))")
    ParseComponentVO convertToVo(BatchRecordItem item);

    BatchRecordComponent converToFormulaList(SaveFormulaDTO list);

    List<BatchRecordComponentBO> convertToBO(List<BatchRecordComponent> batchRecordComponents);

    Collection<BatchRecordComponent> convertToComponent(List<BatchRecordComponentBO> result);


    ComponentListVO convertToComponentVo(BatchRecordComponent batchRecordComponent);

    List<BatchRecordComponentDetail> convertToComponentDetail(List<BatchRecordComponent> componentList);

    BatchRecordComponentDetail convertToComponentDetail(BatchRecordComponent componentList);
}
