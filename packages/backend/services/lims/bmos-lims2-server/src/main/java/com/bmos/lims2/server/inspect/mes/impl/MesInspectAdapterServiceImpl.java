package com.bmos.lims2.server.inspect.mes.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFeignVO;
import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFieldFeignVO;
import com.bmos.lims2.feign.mes.dto.MesInitiateInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesRetryInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesSchemeFeignVO;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldDTO;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigWithFieldDTO;
import com.bmos.lims2.server.inspect.document.service.DocumentConfigService;
import com.bmos.lims2.server.inspect.mes.MesInspectAdapterService;
import com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderSaveDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingSaveDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeSamplingDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeSamplingService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeService;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MesInspectAdapterServiceImpl implements MesInspectAdapterService {

    @Autowired
    private DocumentConfigService documentConfigService;
    @Autowired
    private InspectionSchemeService inspectionSchemeService;
    @Autowired
    private InspectionOrderService inspectionOrderService;
    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private InspectionSchemeSamplingService inspectionSchemeSamplingService;

    /**
     * 平台物料id → LIMS 内部检品id。
     * 平台物料id 为全系统统一标识；LIMS 各业务接口使用的是 lm_inspect_material 主键。
     *
     * @param platformMaterialId 平台物料id
     * @param required           为 true 时找不到检品则抛异常；为 false 时返回 null
     */
    private Long resolveLimsMaterialId(Long platformMaterialId, boolean required) {
        Material material = materialMapper.selectByPlatformMaterialId(platformMaterialId);
        if (material == null) {
            if (required) {
                throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "LIMS未找到对应检品，平台物料id=" + platformMaterialId);
            }
            return null;
        }
        return material.getId();
    }

    @Override
    public List<MesDocumentConfigFeignVO> queryDocumentConfig(Long platformMaterialId) {
        Long materialId = resolveLimsMaterialId(platformMaterialId, false);
        if (materialId == null) {
            return Collections.emptyList();
        }
        List<DocumentConfigWithFieldDTO> list = documentConfigService.getEnabledInspectionConfigListByProductId(materialId);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<MesDocumentConfigFeignVO> result = new ArrayList<>(list.size());
        for (DocumentConfigWithFieldDTO cfg : list) {
            // 列表查询只返回请验单基础信息、不含字段；按配置id查详情拿到关联字段 dataList
            DocumentConfigWithFieldDTO detail = documentConfigService.queryDetail(cfg.getId());
            result.add(toFeignVO(detail));
        }
        return result;
    }

    private MesDocumentConfigFeignVO toFeignVO(DocumentConfigWithFieldDTO cfg) {
        MesDocumentConfigFeignVO vo = new MesDocumentConfigFeignVO();
        vo.setId(cfg.getId());
        vo.setName(cfg.getName());
        vo.setRemark(cfg.getRemark());
        List<MesDocumentConfigFieldFeignVO> fields = new ArrayList<>();
        if (CollUtil.isNotEmpty(cfg.getDataList())) {
            for (DocumentConfigFieldDTO f : cfg.getDataList()) {
                MesDocumentConfigFieldFeignVO fv = new MesDocumentConfigFieldFeignVO();
                fv.setId(f.getId());
                fv.setCode(f.getCode());
                fv.setShowName(f.getShowName());
                fv.setDataName(f.getDataName());
                fv.setRequired(f.getRequired());
                fv.setDefaultValue(f.getDefaultValue());
                fv.setSort(f.getSort());
                fields.add(fv);
            }
        }
        vo.setDataList(fields);
        return vo;
    }

    @Override
    public List<MesSchemeFeignVO> querySchemes(Long platformMaterialId) {
        Long materialId = resolveLimsMaterialId(platformMaterialId, false);
        if (materialId == null) {
            return Collections.emptyList();
        }
        List<InspectionSchemeDropdownDTO> list = inspectionSchemeService.getInspectionSchemeDropdownByMaterialId(materialId);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<MesSchemeFeignVO> result = new ArrayList<>();
        for (InspectionSchemeDropdownDTO d : list) {
            MesSchemeFeignVO vo = new MesSchemeFeignVO();
            vo.setSchemeId(d.getId());
            vo.setSchemeVersionId(d.getActiveVersionId());
            vo.setName(d.getName());
            vo.setDisplayName(d.getDisplayName());
            result.add(vo);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createInspectOrder(MesInitiateInspectFeignDTO dto) {
        Long materialId = resolveLimsMaterialId(dto.getPlatformMaterialId(), true);
        List<InspectionSamplingSaveDTO> samplingList = buildSamplingListFromScheme(dto.getSchemeId(), dto.getSchemeVersionId());
        InspectionOrderSaveDTO saveDTO = toSaveDTO(materialId, dto.getInspectConfigId(),
                dto.getSchemeId(), dto.getSchemeVersionId(), dto.getMaterialBatchNo(),
                dto.getFields(), dto.getFieldNames(), dto.getFieldRequired(), samplingList);
        Long id = inspectionOrderService.saveInspectionOrder(saveDTO);
        inspectionOrderService.confirmInspectionOrder(id);
        InspectionOrder order = inspectionOrderMapper.selectById(id);
        if (order == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单创建后未找到，id=" + id);
        }
        // 落上游来源（默认 MES，向后兼容旧调用方）
        order.setSourceSystem(resolveSourceSystem(dto.getSourceSystem()));
        inspectionOrderMapper.updateById(order);
        return order.getOrderNo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String retryInspectOrder(MesRetryInspectFeignDTO dto) {
        Long materialId = resolveLimsMaterialId(dto.getPlatformMaterialId(), true);
        if (dto.getOriginOrderNo() != null) {
            InspectionOrder origin = inspectionOrderMapper.selectByOrderNo(dto.getOriginOrderNo());
            if (origin != null) {
                inspectionOrderService.terminateInspectionOrder(origin.getId(), "MES重新发起请验，作废原单");
            }
        }
        List<InspectionSamplingSaveDTO> samplingList = buildSamplingListFromScheme(dto.getSchemeId(), dto.getSchemeVersionId());
        InspectionOrderSaveDTO saveDTO = toSaveDTO(materialId, dto.getInspectConfigId(),
                dto.getSchemeId(), dto.getSchemeVersionId(), dto.getMaterialBatchNo(),
                dto.getFields(), dto.getFieldNames(), dto.getFieldRequired(), samplingList);
        Long id = inspectionOrderService.saveInspectionOrder(saveDTO);
        inspectionOrderService.confirmInspectionOrder(id);
        InspectionOrder order = inspectionOrderMapper.selectById(id);
        if (order == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单创建后未找到，id=" + id);
        }
        order.setSourceSystem(resolveSourceSystem(dto.getSourceSystem()));
        inspectionOrderMapper.updateById(order);
        return order.getOrderNo();
    }

    /** 来源系统兜底：null / 空白 → MES，避免 LIMS 端 null 字段。 */
    private static String resolveSourceSystem(String raw) {
        return (raw == null || raw.trim().isEmpty()) ? "MES" : raw.trim().toUpperCase();
    }

    /**
     * 根据方案配置的取样信息生成检验单的取样计划（MES路径不允许编辑，直接来源于方案）。
     */
    private List<InspectionSamplingSaveDTO> buildSamplingListFromScheme(Long schemeId, Long schemeVersionId) {
        if (schemeId == null || schemeVersionId == null) {
            return Collections.emptyList();
        }
        List<InspectionSchemeSamplingDTO> schemeSamplings =
                inspectionSchemeSamplingService.listInspectionSchemeSamplings(schemeId, schemeVersionId);
        if (CollUtil.isEmpty(schemeSamplings)) {
            return Collections.emptyList();
        }
        List<InspectionSamplingSaveDTO> samplingList = new ArrayList<>(schemeSamplings.size());
        for (InspectionSchemeSamplingDTO s : schemeSamplings) {
            InspectionSamplingSaveDTO sample = new InspectionSamplingSaveDTO();
            sample.setInspectItemId(s.getInspectItemId());
            sample.setPlannedQuantity(s.getSamplingAmount());
            sample.setUnitId(parseLong(s.getSamplingUnit()));
            sample.setSampleCount(s.getSamplingCount());
            samplingList.add(sample);
        }
        return samplingList;
    }

    /** 安全把字符串单位id转 Long；解析失败返回 null（不阻断主流程）。 */
    private Long parseLong(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(s.trim());
        } catch (NumberFormatException e) {
            log.warn("方案取样配置 samplingUnit 非数字，忽略：{}", s);
            return null;
        }
    }

    /** 扁平字段 → InspectionOrderSaveDTO（纯函数，便于单测） */
    static InspectionOrderSaveDTO toSaveDTO(Long materialId, Long inspectConfigId, Long schemeId,
                                            Long schemeVersionId, String materialBatchNo,
                                            Map<String, String> fields, Map<String, String> fieldNames,
                                            Map<String, Boolean> fieldRequired,
                                            List<InspectionSamplingSaveDTO> samplingList) {
        InspectionOrderSaveDTO saveDTO = new InspectionOrderSaveDTO();
        saveDTO.setMaterialId(materialId);
        saveDTO.setSchemeId(schemeId);
        saveDTO.setSchemeVersionId(schemeVersionId);
        saveDTO.setTemplateId(inspectConfigId);
        saveDTO.setBatchNo(materialBatchNo);
        saveDTO.setSamplingList(samplingList);
        List<CustomFieldValueDTO> customFields = new ArrayList<>();
        if (fields != null) {
            for (Map.Entry<String, String> e : fields.entrySet()) {
                CustomFieldValueDTO cf = new CustomFieldValueDTO();
                cf.setFieldCode(e.getKey());
                cf.setFieldValue(e.getValue());
                if (fieldNames != null) {
                    cf.setFieldName(fieldNames.get(e.getKey()));
                }
                if (fieldRequired != null) {
                    cf.setRequired(fieldRequired.get(e.getKey()));
                }
                customFields.add(cf);
            }
        }
        saveDTO.setCustomFields(customFields);
        return saveDTO;
    }
}
