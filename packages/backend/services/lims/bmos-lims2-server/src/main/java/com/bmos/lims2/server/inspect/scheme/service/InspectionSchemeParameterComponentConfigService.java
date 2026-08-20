package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.inspect.scheme.dto.request.ComponentConfigDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterComponentConfigSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.ProcedureStepRecordItemQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.SchemeParameterComponentConfigListQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ProcedureStepRecordItemDTO;

import java.util.List;

/**
 * @className: InspectionSchemeParameterComponentConfigService
 * @author: yigaohui
 * @date: 2025/11/10 10:56
 * @Version: 1.0
 * @description:
 */

public interface InspectionSchemeParameterComponentConfigService {
    List<ComponentConfigDTO> getConfigList(SchemeParameterComponentConfigListQueryDTO dto);


    void saveConfig(InspectionSchemeParameterComponentConfigSaveDTO dto);


    ProcedureStepRecordItemDTO getRecordItem(ProcedureStepRecordItemQueryDTO dto);
}
