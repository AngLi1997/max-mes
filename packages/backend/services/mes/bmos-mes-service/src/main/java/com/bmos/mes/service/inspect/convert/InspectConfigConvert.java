package com.bmos.mes.service.inspect.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDataVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigPageVO;
import com.bmos.mes.service.inspect.model.InspectConfig;
import com.bmos.mes.service.inspect.model.InspectConfigData;
import com.bmos.mes.service.inspect.model.InspectConfigMaterial;
import com.bmos.mes.service.inspect.service.dto.InspectConfigBindMaterialDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigDataSaveDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface InspectConfigConvert {

    InspectConfigConvert INSTANCE = Mappers.getMapper(InspectConfigConvert.class);

    InspectConfig convert2InspectConfig(InspectConfigSaveDTO dto);

    default List<InspectConfigData> convert2InspectConfigDataList(List<InspectConfigDataSaveDTO> dataList, Long id){
        if (CollUtil.isEmpty(dataList)){
            return new ArrayList<>();
        }
        List<InspectConfigData> inspectConfigDataList = new ArrayList<>();
        for (InspectConfigDataSaveDTO inspectConfigDataSaveDTO : dataList) {
            InspectConfigData inspectConfigData = new InspectConfigData();
            inspectConfigData.setConfigId(id);
            inspectConfigData.setCode(inspectConfigDataSaveDTO.getCode());
            inspectConfigData.setDataName(inspectConfigDataSaveDTO.getDataName());
            inspectConfigData.setShowName(inspectConfigDataSaveDTO.getShowName());
            inspectConfigData.setRequired(inspectConfigDataSaveDTO.getRequired());
            inspectConfigData.setDefaultValue(inspectConfigDataSaveDTO.getDefaultValue());
            inspectConfigData.setSort(inspectConfigDataSaveDTO.getSort());
            inspectConfigDataList.add(inspectConfigData);
        }
        return inspectConfigDataList;
    }

    InspectConfigDetailVO convert2InspectConfigDetailVO(InspectConfig inspectConfig, List<InspectConfigData> inspectConfigDataList);

    List<InspectConfigDataVO> convert2InspectConfigDataVOList(List<InspectConfigData> inspectConfigDataList);

    List<InspectConfigPageVO> convert2ConfigPageList(List<InspectConfig> inspectConfigList);

    default List<InspectConfigMaterial> convert2InspectConfigMaterialList(InspectConfigBindMaterialDTO dto){
        if (CollUtil.isEmpty(dto.getMaterialIdList())){
            return new ArrayList<>();
        }
        List<InspectConfigMaterial> inspectConfigMaterialList = new ArrayList<>();
        for (Long materialId : dto.getMaterialIdList()) {
            InspectConfigMaterial inspectConfigMaterial = new InspectConfigMaterial();
            inspectConfigMaterial.setConfigId(dto.getId());
            inspectConfigMaterial.setMaterialId(materialId);
            inspectConfigMaterialList.add(inspectConfigMaterial);
        }
        return inspectConfigMaterialList;
    }
}
