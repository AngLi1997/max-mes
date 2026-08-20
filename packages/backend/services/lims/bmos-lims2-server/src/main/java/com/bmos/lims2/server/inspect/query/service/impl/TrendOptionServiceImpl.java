package com.bmos.lims2.server.inspect.query.service.impl;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.server.inspect.query.service.TrendOptionService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOptionDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeDataPointMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.task.dto.SchemeParameterDTO;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 趋势查询-联动选项服务实现
 * @Author: yigaohui
 * @Date: 2025/09/05 12:06
 */
@Service
public class TrendOptionServiceImpl implements TrendOptionService {

    private final InspectionSchemeMapper schemeMapper;
    private final InspectionSchemeVersionMapper versionMapper;
    private final TaskMapper taskMapper;
    private final InspectionSchemeDataPointMapper dataPointMapper;

    public TrendOptionServiceImpl(InspectionSchemeMapper schemeMapper,
                                  InspectionSchemeVersionMapper versionMapper,
                                  TaskMapper taskMapper,
                                  InspectionSchemeDataPointMapper dataPointMapper) {
        this.schemeMapper = schemeMapper;
        this.versionMapper = versionMapper;
        this.taskMapper = taskMapper;
        this.dataPointMapper = dataPointMapper;
    }

    @Override
    public List<InspectionScheme> listSchemesByMaterial(Long materialId) {
        if (materialId == null) { return java.util.Collections.emptyList(); }
        return schemeMapper.selectByMaterialIds(java.util.Collections.singletonList(materialId));
    }

    @Override
    public List<SchemeVersionOptionDTO> listVersionsByScheme(Long schemeId) {
        if (schemeId == null) { return java.util.Collections.emptyList(); }
        return versionMapper.listBySchemeId(schemeId);
    }

    @Override
    public List<SchemeParameterDTO> listParametersByVersion(Long versionId) {
        if (versionId == null) { return java.util.Collections.emptyList(); }
        return taskMapper.selectSchemeItemsAndParameters(versionId);
    }

    @Override
    public List<SchemeParameterDTO> listParametersByScheme(Long schemeId) {
        if (schemeId == null) { return java.util.Collections.emptyList(); }
        com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion active = versionMapper.getActiveVersion(schemeId);
        if (active == null) { return java.util.Collections.emptyList(); }
        return taskMapper.selectSchemeItemsAndParameters(active.getId());
    }

    @Override
    public List<InspectionSchemeDataPointDTO> listNumericDataPointsByParameterConfig(Long parameterConfigId) {
        if (parameterConfigId == null) { return java.util.Collections.emptyList(); }
        List<InspectionSchemeDataPointDTO> list = dataPointMapper.listByParameterConfigId(parameterConfigId);
        return list.stream()
                .filter(dp -> Objects.equals(dp.getPointType(), DataPointTypeEnum.NUMBER))
                .collect(Collectors.toList());
    }
}


