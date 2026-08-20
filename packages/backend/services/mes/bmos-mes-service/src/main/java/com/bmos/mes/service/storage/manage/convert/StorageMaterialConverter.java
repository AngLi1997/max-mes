package com.bmos.mes.service.storage.manage.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.service.product.model.MaterialField;
import com.bmos.mes.service.requisition.vo.BatchAvailableMaterialVO;
import com.bmos.mes.service.storage.manage.dto.MaterialBatchFieldDTO;
import com.bmos.mes.service.storage.manage.entity.MaterialBatchField;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialChargeRecycle;
import com.bmos.mes.service.storage.manage.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface StorageMaterialConverter {

    StorageMaterialConverter INSTANCE = Mappers.getMapper(StorageMaterialConverter.class);

    List<MaterialBatchListVO> convertToListVO(List<StorageMaterialBatch> batches);

    List<BatchReservedAvailableMaterialVO> convertToReservedAvailableVO(List<BatchReservedMaterialVO> batchReservedMaterialVOS);

    List<ChargeRecycleListVO> convertToChargeRecycleListVO(List<StorageMaterialChargeRecycle> list);

    List<ComponentChargeListVO> convertToComponentChargeList(List<StorageMaterialChargeRecycle> chargeRecycleList);

    List<BatchAvailableMaterialVO> convertToAvailableVO(List<BatchReservedAvailableMaterialVO> availableList);

    List<MaterialBatchFieldVO> convert2BatchFieldVOList(List<MaterialBatchField> materialBatchFields);

    default List<MaterialBatchField> convert2BatchField(List<MaterialBatchFieldDTO> materialBatchFieldVOList, Long materialBatchId){
        if (CollUtil.isEmpty(materialBatchFieldVOList)){
            return new ArrayList<>();
        }
        List<MaterialBatchField> materialBatchFields = new ArrayList<>();
        for (MaterialBatchFieldDTO materialBatchFieldVO : materialBatchFieldVOList) {
            MaterialBatchField materialBatchField = new MaterialBatchField();
            materialBatchField.setMaterialBatchId(materialBatchId);
            materialBatchField.setFieldType(materialBatchFieldVO.getFieldType());
            materialBatchField.setFieldTypeName(materialBatchFieldVO.getFieldTypeName());
            materialBatchField.setField(materialBatchFieldVO.getField());
            materialBatchField.setFieldName(materialBatchFieldVO.getFieldName());
            materialBatchField.setFieldValue(materialBatchFieldVO.getFieldValue());
            materialBatchFields.add(materialBatchField);
        }
        return materialBatchFields;
    }

    MaterialBatchFieldVO convert2BatchFieldVO(MaterialBatchField field);


    default List<CustomFieldDetailInfo> convert2CustomFieldDetailInfo(List<MaterialBatchField> batchFields){
        return batchFields.stream().map(this::convert2CustomFieldDetailInfo).collect(Collectors.toList());
    }

    @Mapping(target = "keyId", source = "materialBatchId")
    CustomFieldDetailInfo convert2CustomFieldDetailInfo(MaterialBatchField batchField);

}
