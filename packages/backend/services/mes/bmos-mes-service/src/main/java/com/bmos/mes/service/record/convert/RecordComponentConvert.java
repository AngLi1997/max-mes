package com.bmos.mes.service.record.convert;

import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.ingredient.plan.dto.IngredientPlanCompleteDTO;
import com.bmos.mes.service.output.finished.dto.SaveFinishedProductOutputDTO;
import com.bmos.mes.service.record.dto.ComponentListDTO;
import com.bmos.mes.service.record.dto.SaveFormulaDTO;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordComponentBO;
import com.bmos.mes.service.record.model.BatchRecordComponentDetail;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.record.vo.ParseComponentVO;
import com.bmos.mes.service.requisition.dto.RequisitionCompleteDTO;
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

    BusinessComponentBatchSaveDTO convertToBusinessComponentBatchSaveDTO(RequisitionCompleteDTO dto);
    BusinessComponentBatchSaveDTO convertToBusinessComponentBatchSaveDTO(IngredientPlanCompleteDTO dto);

    BusinessComponentBatchSaveDTO convertToBusinessComponentBatchSaveDTO(SaveFinishedProductOutputDTO dto);

    BusinessComponentBatchSaveDTO convertToBusinessComponentBatchSaveDTO(BusinessDataHandleBaseDTO dto);

    ComponentListVO convertToComponentVo(BatchRecordComponent batchRecordComponent);

    List<BatchRecordComponentDetail> convertToComponentDetail(List<BatchRecordComponent> componentList);

    BatchRecordComponentDetail convertToComponentDetail(BatchRecordComponent componentList);
}
