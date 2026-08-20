package com.bmos.lims2.server.inspect.scheme.convert;

import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.eln.record.entity.SchemeParameterComponentConfig;
import com.bmos.lims2.server.inspect.scheme.dto.request.ComponentConfigDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterComponentConfigSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ComponentConfigDetailDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ProcedureStepRecordItemDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: InspectionParameterConfigConverter
 * @author: yigaohui
 * @date: 2025/11/10 15:42
 * @Version: 1.0
 * @description:
 */
@Mapper
public interface InspectionParameterConfigConverter {
    InspectionParameterConfigConverter INSTANCE = Mappers.getMapper(InspectionParameterConfigConverter.class);

    default List<SchemeParameterComponentConfig> convertConfigLit(InspectionSchemeParameterComponentConfigSaveDTO dto) {
        return dto.getComponents()
                .stream()
                .filter(e -> {
                    String configInfo = e.getConfigInfo();
                    return StrUtil.isNotEmpty(configInfo
                            .replace(StrUtil.DELIM_START, StrUtil.EMPTY)
                            .replace(StrUtil.DELIM_END, StrUtil.EMPTY));
                })
                .map(e -> {
                    SchemeParameterComponentConfig config = new SchemeParameterComponentConfig();
                    config.setComponentId(e.getComponentId());
                    config.setConfigInfo(e.getConfigInfo());
                    config.setFieldId(e.getFieldId());
                    return convertConfig(dto, config);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "parameterId", source = "dto.parameterId")
    @Mapping(target = "parameterConfigId", source = "dto.parameterConfigId")
    @Mapping(target = "schemeId", source = "dto.schemeId")
    @Mapping(target = "schemeVersionId", source = "dto.schemeVersionId")
    @Mapping(target = "recordItemId", source = "dto.recordItemId")
    @Mapping(target = "recordVersionId", source = "dto.recordVersionId")
    SchemeParameterComponentConfig convertConfig(InspectionSchemeParameterComponentConfigSaveDTO dto, SchemeParameterComponentConfig config);

    List<ComponentConfigDTO> convertComponentVO(List<SchemeParameterComponentConfig> configs);

    default ProcedureStepRecordItemDTO convert(InspectionSchemeParameter inspectionSchemeParameter, List<ComponentConfigDetailDTO> configs) {
        ProcedureStepRecordItemDTO recordItemDTO = new ProcedureStepRecordItemDTO();
        recordItemDTO.setRecordVersionId(inspectionSchemeParameter.getRecordVersionId());
        recordItemDTO.setRecordItemId(inspectionSchemeParameter.getRecordItemId());
        recordItemDTO.setComponentConfigs(configs);

        recordItemDTO.setParameterId(inspectionSchemeParameter.getParameterId());
        recordItemDTO.setParameterConfigId(inspectionSchemeParameter.getId());
        return recordItemDTO;
    }
}
