package com.bmos.lims2.server.inspect.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.lims2.server.inspect.document.dto.*;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfig;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfigField;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfigMaterial;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface InspectionDocumentConfigConvert {

    InspectionDocumentConfigConvert INSTANCE = Mappers.getMapper(InspectionDocumentConfigConvert.class);

    CommonPage<DocumentConfigDTO> convert2Page(CommonPage<DocumentConfig> inspectionConfigCommonPage);

    DocumentConfig convert2DO(DocumentConfigSaveDTO dto);

    default List<DocumentConfigField> convert2InspectConfigDataList(List<DocumentConfigFieldSaveDTO> dataList, Long id) {
        if (CollUtil.isEmpty(dataList)) {
            return new ArrayList<>();
        }
        List<DocumentConfigField> inspectConfigDataList = new ArrayList<>();
        for (DocumentConfigFieldSaveDTO inspectConfigDataSaveDTO : dataList) {
            DocumentConfigField inspectConfigData = new DocumentConfigField();
            inspectConfigData.setConfigId(id);
            inspectConfigData.setCode(inspectConfigDataSaveDTO.getCode());
            inspectConfigData.setDataName(inspectConfigDataSaveDTO.getDataName());
            inspectConfigData.setShowName(inspectConfigDataSaveDTO.getShowName());
            inspectConfigData.setRequired(inspectConfigDataSaveDTO.getRequired());
            inspectConfigData.setDefaultValue(inspectConfigDataSaveDTO.getDefaultValue());
            inspectConfigData.setFieldSource(inspectConfigDataSaveDTO.getFieldSource());
            inspectConfigData.setSort(inspectConfigDataSaveDTO.getSort());
            inspectConfigDataList.add(inspectConfigData);
        }
        return inspectConfigDataList;
    }

    default DocumentConfigWithFieldDTO convert2ConfigDetail(DocumentConfig documentConfig, List<DocumentConfigField> dataList) {
        DocumentConfigWithFieldDTO result = convert2ConfigDetail(documentConfig);
        result.setDataList(convert2DetailDataList(dataList));
        return result;
    }

    DocumentConfigWithFieldDTO convert2ConfigDetail(DocumentConfig inspectionConfig);

    List<DocumentConfigFieldDTO> convert2DetailDataList(List<DocumentConfigField> dataList);

    default List<DocumentConfigMaterial> convert2InspectionConfigProductList(DocumentConfigBindProductDTO dto) {
        if (CollUtil.isEmpty(dto.getMaterialIdList())) {
            return new ArrayList<>();
        }
        // 去重，避免重复插入
        List<Long> distinctIds = dto.getMaterialIdList().stream().distinct().collect(java.util.stream.Collectors.toList());
        List<DocumentConfigMaterial> list = new ArrayList<>(distinctIds.size());
        for (Long productId : distinctIds) {
            DocumentConfigMaterial inspectConfigMaterial = new DocumentConfigMaterial();
            inspectConfigMaterial.setConfigId(dto.getId());
            inspectConfigMaterial.setProductId(productId);
            list.add(inspectConfigMaterial);
        }
        return list;
    }

    default List<DocumentConfigWithFieldDTO> convert2VOList(List<DocumentConfig> configs) {
        List<DocumentConfigWithFieldDTO> voList = new ArrayList<>();
        if (CollUtil.isEmpty(configs)) {
            return voList;
        }
        for (DocumentConfig config : configs) {
            DocumentConfigWithFieldDTO vo = new DocumentConfigWithFieldDTO();
            vo.setId(config.getId());
            vo.setName(config.getName());
            vo.setStatus(config.getStatus());
            voList.add(vo);
        }
        return voList;
    }
}
