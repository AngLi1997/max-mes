package com.bmos.lims2.server.inspect.query.service;

import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOptionDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.task.dto.SchemeParameterDTO;

import java.util.List;

/**
 * @Description: 趋势查询-联动选项服务
 * @Author: yigaohui
 * @Date: 2025/09/05 12:05
 */
public interface TrendOptionService {

    List<InspectionScheme> listSchemesByMaterial(Long materialId);

    List<SchemeVersionOptionDTO> listVersionsByScheme(Long schemeId);

    List<SchemeParameterDTO> listParametersByVersion(Long versionId);

    /**
     * 按方案ID查询检验项目/分析项列表（使用当前生效版本）
     */
    List<SchemeParameterDTO> listParametersByScheme(Long schemeId);

    List<InspectionSchemeDataPointDTO> listNumericDataPointsByParameterConfig(Long parameterConfigId);
}


