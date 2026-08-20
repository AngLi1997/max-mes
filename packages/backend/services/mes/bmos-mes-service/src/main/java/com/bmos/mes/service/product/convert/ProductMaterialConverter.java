package com.bmos.mes.service.product.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.common.enums.TimeUnitEnum;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.material.vo.MaterialFieldInfoFeignVO;
import com.bmos.mes.service.output.weigh.vo.OutputMaterialItem;
import com.bmos.mes.service.product.dto.*;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.mes.service.product.model.MaterialField;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.product.vo.*;
import com.bmos.platform.facade.dict.vo.DictDataFeignVO;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.google.common.collect.Lists;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductMaterialConverter {

    ProductMaterialConverter INSTANCE = Mappers.getMapper(ProductMaterialConverter.class);

    ProductMaterial convertToProductMaterial(ProductMaterialSaveDTO dto);

    ProductMaterial convertToProductMaterial(ProductMaterialUpdateDTO dto);
    ProductMaterial convertToProductMaterial(ProductMaterialUpdateExtensionDTO dto);


    ProductMaterialDetailVO ConvertToDetail(ProductMaterial productMaterial);

    ProductMaterialUpdateExtensionDTO convertToExtension(ProductMaterialUpdateDTO dto);

    ProductMaterial convertToProductMaterial(MaterialIssueDTO material);
    List<ProductMaterial> convertToProductMaterial(List<MaterialIssueDTO> materials);

    List<PrincipalMaterialVO> convertPrincipalList(List<ProductMaterial> productMaterials);

    ProductMaterialSaveDTO convertToRemoteSaveDTO(ProductMaterialSaveDTO dto);

    List<ProductCategoryTreeNodeVO> convertToTreeNodeVO(List<ProductMaterialCategory> categories);

    @Mapping(target = "timeUnit", expression = "java(com.bmos.common.base.enums.CommonEnum.getEnumByValue(com.bmos.mes.common.enums.TimeUnitEnum.class, expandInfo.getTimeUnit()))")
    MaterialExpandInfoVO convertToExpandVO(MaterialExpandInfo expandInfo);

    OutputMaterialItem convertToOutputItemVO(ProductMaterial productMaterial);

    default List<MaterialFieldTypeVO> convert2MaterialFieldTypeVO(List<DictDetailFeignVO> data){
        List<MaterialFieldTypeVO> result = Lists.newArrayList();
        if (CollUtil.isEmpty(data)){
            return result;
        }
        for (DictDetailFeignVO dictDetailFeignVO : data) {
            MaterialFieldTypeVO vo = new MaterialFieldTypeVO();
            vo.setFieldType(dictDetailFeignVO.getDictCode());
            vo.setFieldTypeName(dictDetailFeignVO.getDictName());
            List<MaterialFieldVO> fieldList = new ArrayList<>();
            vo.setFieldList(fieldList);
            if (CollUtil.isEmpty(dictDetailFeignVO.getDictDataList())){
                continue;
            }
            for (DictDataFeignVO dictDataFeignVO : dictDetailFeignVO.getDictDataList()) {
                MaterialFieldVO fieldVO = new MaterialFieldVO();
                fieldVO.setField(dictDataFeignVO.getDictValue());
                fieldVO.setFieldName(dictDataFeignVO.getDictLabel());
                fieldList.add(fieldVO);
            }
            result.add(vo);
        }
        return result;
    }

    List<MaterialFieldInfoVO> convertMaterialFieldInfoVOList(List<MaterialField> fieldList);

    List<MaterialFieldInfoFeignVO> convertMaterialFieldInfoFeignVOList(List<MaterialField> fieldList);

    default List<MaterialField> convert2MaterialField(List<MaterialFieldSaveDTO> fieldSaveDTOList, Long materialId){
        if (CollUtil.isEmpty(fieldSaveDTOList)){
            return Lists.newArrayList();
        }
        List<MaterialField> result = Lists.newArrayList();
        for (MaterialFieldSaveDTO fieldSaveDTO : fieldSaveDTOList) {
            MaterialField field = new MaterialField();
            field.setMaterialId(materialId);
            field.setField(fieldSaveDTO.getField());
            field.setFieldName(fieldSaveDTO.getFieldName());
            field.setFieldType(fieldSaveDTO.getFieldType());
            field.setFieldTypeName(fieldSaveDTO.getFieldTypeName());
            field.setFieldValue(fieldSaveDTO.getFieldValue());
            result.add(field);
        }
        return result;
    }


    default List<CustomFieldDetailInfo> convert2CustomFieldDetailInfo(List<MaterialField> materialFields){
        return materialFields.stream().map(this::convert2CustomFieldDetailInfo).collect(Collectors.toList());
    }

    @Mapping(target = "keyId", source = "materialId")
    CustomFieldDetailInfo convert2CustomFieldDetailInfo(MaterialField materialField);
}
