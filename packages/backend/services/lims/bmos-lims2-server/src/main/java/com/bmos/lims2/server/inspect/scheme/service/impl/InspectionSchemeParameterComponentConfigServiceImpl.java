package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponentDetail;
import com.bmos.lims2.server.eln.record.entity.SchemeParameterComponentConfig;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentDetailMapper;
import com.bmos.lims2.server.eln.record.mapper.SchemeParameterComponentConfigMapper;
import com.bmos.lims2.server.inspect.scheme.convert.InspectionParameterConfigConverter;
import com.bmos.lims2.server.inspect.scheme.dto.request.ComponentConfigDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterComponentConfigSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.ProcedureStepRecordItemQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.SchemeParameterComponentConfigListQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ComponentConfigDetailDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ProcedureStepRecordItemDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeParameterComponentConfigService;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @className: InspectionSchemeParameterComponentConfigServiceImpl
 * @author: yigaohui
 * @date: 2025/11/10 10:56
 * @Version: 1.0
 * @description:
 */

@Service
public class InspectionSchemeParameterComponentConfigServiceImpl implements InspectionSchemeParameterComponentConfigService {

    @Autowired
    private SchemeParameterComponentConfigMapper schemeParameterComponentConfigMapper;

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private StabilitySchemeParameterMapper stabilitySchemeParameterMapper;

    @Autowired
    private BatchRecordComponentDetailMapper batchRecordComponentDetailMapper;


    @Override
    public List<ComponentConfigDTO> getConfigList(SchemeParameterComponentConfigListQueryDTO dto) {
        Long parameterConfigId = dto.getParameterConfigId();
        InspectionSchemeParameter inspectionSchemeParameter = resolveParameter(parameterConfigId);
        List<SchemeParameterComponentConfig> configs = schemeParameterComponentConfigMapper.getListByInspectionSchemeParameter(inspectionSchemeParameter);
        if (CollUtil.isEmpty(configs)) {
            return Collections.emptyList();
        }
        return InspectionParameterConfigConverter.INSTANCE.convertComponentVO(configs);
    }

    @Override
    public void saveConfig(InspectionSchemeParameterComponentConfigSaveDTO dto) {
        if (CollUtil.isEmpty(dto.getComponents())) {
            return;
        }
        Long parameterConfigId = dto.getParameterConfigId();
        InspectionSchemeParameter inspectionSchemeParameter = resolveParameter(parameterConfigId);
        if (inspectionSchemeParameter == null) {
            throw new BmosException(LimsResponseCode.SCHEME_PARAMETER_NOT_EXIST);
        }
        dto.setRecordVersionId(inspectionSchemeParameter.getRecordVersionId());
        schemeParameterComponentConfigMapper.deleteByInspectionSchemeParameterConfigId(parameterConfigId);
        List<SchemeParameterComponentConfig> configs = InspectionParameterConfigConverter.INSTANCE.convertConfigLit(dto);
        schemeParameterComponentConfigMapper.insertBatch(configs);
    }

    @Override
    public ProcedureStepRecordItemDTO getRecordItem(ProcedureStepRecordItemQueryDTO dto) {
        InspectionSchemeParameter inspectionSchemeParameter = resolveParameter(dto.getParameterConfigId());
        // 查询生产计划
        // 关联批记录组件表查询数据
        List<ComponentConfigDetailDTO> configs = this.getComponentsByInspectionSchemeParameter(inspectionSchemeParameter);
        return InspectionParameterConfigConverter.INSTANCE.convert(inspectionSchemeParameter, configs);
    }

    /**
     * 按 parameterConfigId 优先查常规检验分析项配置，未找到则尝试稳定性方案分析项配置，
     * 并将稳定性配置字段映射到 InspectionSchemeParameter 对象返回（字段名完全对齐）。
     */
    private InspectionSchemeParameter resolveParameter(Long parameterConfigId) {
        InspectionSchemeParameter param = inspectionSchemeParameterMapper.selectById(parameterConfigId);
        if (param != null) {
            return param;
        }
        StabilitySchemeParameter sp = stabilitySchemeParameterMapper.selectById(parameterConfigId);
        if (sp == null) {
            return null;
        }
        InspectionSchemeParameter mapped = new InspectionSchemeParameter();
        mapped.setId(sp.getId());
        mapped.setSchemeId(sp.getSchemeId());
        mapped.setVersionId(sp.getVersionId());
        mapped.setItemConfigId(sp.getItemConfigId());
        mapped.setInspectItemId(sp.getInspectItemId());
        mapped.setParameterId(sp.getParameterId());
        mapped.setRecordId(sp.getRecordId());
        mapped.setRecordCode(sp.getRecordCode());
        mapped.setRecordVersionId(sp.getRecordVersionId());
        mapped.setRecordItemId(sp.getRecordItemId());
        mapped.setStandardRule(sp.getStandardRule());
        mapped.setIsReportable(sp.getIsReportable());
        mapped.setIsExecutable(sp.getIsExecutable());
        mapped.setFinalExpression(sp.getFinalExpression());
        mapped.setExecuteMethod(sp.getExecuteMethod());
        return mapped;
    }

    private List<ComponentConfigDetailDTO> getComponentsByInspectionSchemeParameter(InspectionSchemeParameter inspectionSchemeParameter) {
        boolean isStability = inspectionSchemeParameterMapper.selectById(inspectionSchemeParameter.getId()) == null;
        List<ComponentConfigDetailDTO> vos = isStability
            ? schemeParameterComponentConfigMapper.selectStabilityComponentWithComponentConfig(inspectionSchemeParameter)
            : schemeParameterComponentConfigMapper.selectComponentWithComponentConfig(inspectionSchemeParameter);
        if (CollUtil.isEmpty(vos)) {
            return new ArrayList<>();
        }
        List<BatchRecordComponentDetail> details =
                batchRecordComponentDetailMapper.selectBatchIds(CollectionUtils.convertList(vos,
                        ComponentConfigDetailDTO::getId));
        Map<Long, BatchRecordComponentDetail> map = CollectionUtils.convertMap(details, BatchRecordComponentDetail::getId);
        vos.forEach(e->{
            BatchRecordComponentDetail detail = map.getOrDefault(e.getId(), new BatchRecordComponentDetail());
            e.setFormulaField(detail.getFormulaField());
            e.setComponentDetail(detail.getComponentDetail());
        });
        return TreeUtil.buildTree(vos, false);
    }
}
