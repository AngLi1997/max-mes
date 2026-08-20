package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.entity.SchemeParameterComponentConfig;
import com.bmos.lims2.server.eln.record.mapper.SchemeParameterComponentConfigMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentMapper;
import com.github.pagehelper.PageHelper;
import com.bmos.lims2.server.inspect.scheme.dto.*;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionAuditQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionCopyDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionAuditDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionFullConfigDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeItemService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeSamplingService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeBasicSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingUpdateDTO;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.mybatis.page.CommonPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 检验方案版本Service实现类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
@Slf4j
public class InspectionSchemeVersionServiceImpl implements InspectionSchemeVersionService {


    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private InspectionSchemeMapper inspectionSchemeMapper;

    @Autowired
    private InspectionSchemeItemService inspectionSchemeItemService;

    @Autowired
    private InspectionSchemeSamplingService inspectionSchemeSamplingService;

    @Autowired
    private InspectionSchemeService inspectionSchemeService;

    @Autowired
    private FlowAuditService flowAuditService;

    @Autowired
    private com.bmos.lims2.server.material.mapper.MaterialMapper materialMapper;

    @Autowired
    private com.bmos.lims2.server.inspect.pack.mapper.InspectPackageMapper inspectPackageMapper;

    @Autowired
    private com.bmos.unit.service.UnitCache unitCache;

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Autowired
    private SchemeParameterComponentConfigMapper schemeParameterComponentConfigMapper;

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private BatchRecordComponentMapper batchRecordComponentMapper;

    @Override
    public CommonPage<InspectionSchemeVersionDTO> pageInspectionSchemeVersion(InspectionSchemeVersionQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<InspectionSchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeVersion::getSchemeId, queryDTO.getSchemeId())
                .like(queryDTO.getVersionNo() != null, InspectionSchemeVersion::getVersionNo, queryDTO.getVersionNo())
                .eq(queryDTO.getStatus() != null, InspectionSchemeVersion::getStatus, queryDTO.getStatus())
                .orderByDesc(InspectionSchemeVersion::getCreateTime);

        // 分页查询（PageHelper）
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        java.util.List<InspectionSchemeVersion> versionList = inspectionSchemeVersionMapper.selectList(wrapper);
        java.util.List<InspectionSchemeVersionDTO> dtoList = BeanUtil.copyToList(versionList, InspectionSchemeVersionDTO.class);
        // 先用原始列表生成分页信息，再替换为DTO列表，避免分页丢失
        CommonPage<InspectionSchemeVersion> page = CommonPage.convertPage(versionList);
        return CommonPage.CommonPage(dtoList, Long.valueOf(page.getTotal()), queryDTO);
    }

    @Override
    public InspectionSchemeVersionDTO getInspectionSchemeVersion(Long id) {
        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 转换为DTO
        InspectionSchemeVersionDTO dto = BeanUtil.copyProperties(version, InspectionSchemeVersionDTO.class);

        // 查询父版本号
        if (version.getParentVersionId() != null) {
            InspectionSchemeVersion parentVersion = inspectionSchemeVersionMapper.selectById(version.getParentVersionId());
            if (parentVersion != null) {
                dto.setParentVersionNo(parentVersion.getVersionNo());
            }
        }

        return dto;
    }

    @Override
    public InspectionSchemeVersionFullConfigDTO getInspectionSchemeVersionFullConfig(Long id) {
        // 1. 查询版本基础信息
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 2. 查询方案基础信息
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_NOT_EXIST);
        }

        // 3. 组装基础信息
        InspectionSchemeVersionFullConfigDTO fullConfigDTO = new InspectionSchemeVersionFullConfigDTO();

        // 版本信息
        fullConfigDTO.setId(version.getId());
        fullConfigDTO.setSchemeId(version.getSchemeId());
        fullConfigDTO.setVersionNo(version.getVersionNo());
        fullConfigDTO.setStatus(version.getStatus());
        fullConfigDTO.setParentVersionId(version.getParentVersionId());
        fullConfigDTO.setDescription(version.getDescription());
        fullConfigDTO.setEffectiveDate(version.getEffectiveDate());
        fullConfigDTO.setCreateTime(version.getCreateTime());

        // 方案信息
        fullConfigDTO.setSchemeName(scheme.getName());

        // 查询父版本号
        if (version.getParentVersionId() != null) {
            InspectionSchemeVersion parentVersion = inspectionSchemeVersionMapper.selectById(version.getParentVersionId());
            if (parentVersion != null) {
                fullConfigDTO.setParentVersionNo(parentVersion.getVersionNo());
            }
        }

        // 设置物料信息 - 查询完整的物料信息
        InspectionSchemeVersionFullConfigDTO.MaterialInfoDTO materialInfo = new InspectionSchemeVersionFullConfigDTO.MaterialInfoDTO();
        materialInfo.setMaterialId(version.getMaterialId());
        if (version.getMaterialId() != null) {
            Material material = materialMapper.selectById(version.getMaterialId());
            if (material != null) {
                materialInfo.setMaterialName(material.getName());
                materialInfo.setMaterialCode(material.getCode());
                materialInfo.setUnitId(material.getUnitId());
                if (material.getUnitId() != null) {
                    materialInfo.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
                }
            }
        }
        fullConfigDTO.setMaterial(materialInfo);

        // 设置实验包信息 - 查询完整的实验包信息
        InspectionSchemeVersionFullConfigDTO.PackageInfoDTO packageInfo = new InspectionSchemeVersionFullConfigDTO.PackageInfoDTO();
        packageInfo.setPackageId(version.getPackageId());
        if (version.getPackageId() != null) {
            // 需要注入InspectPackageMapper来查询实验包信息
            com.bmos.lims2.server.inspect.pack.entity.InspectPackage inspectPackage =
                    inspectPackageMapper.selectById(version.getPackageId());
            if (inspectPackage != null) {
                packageInfo.setPackageName(inspectPackage.getName());
                packageInfo.setPackageCode(inspectPackage.getCode());
            }
        }
        fullConfigDTO.setPackageInfo(packageInfo);

        // 5. 查询检验项目配置
        fillInspectionItems(fullConfigDTO, version, scheme);

        // 6. 查询取样配置
        List<InspectionSchemeSamplingDTO> samplingConfigs = inspectionSchemeSamplingService.listInspectionSchemeSamplings(scheme.getId(), version.getId());
        if (samplingConfigs != null && !samplingConfigs.isEmpty()) {
            List<InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO> configDTOS = samplingConfigs.stream().map(item -> {
                InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO samplingConfig = new InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO();
                BeanUtil.copyProperties(item, samplingConfig);
                samplingConfig.setSamplingConfigId(item.getId());
                return samplingConfig;
            }).collect(Collectors.toList());
            fullConfigDTO.setSamplingConfigs(configDTOS);
        }

        return fullConfigDTO;
    }

    @Override
    public InspectionSchemeVersionFullConfigDTO getVersionBasicConfig(Long id) {
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_NOT_EXIST);
        }

        InspectionSchemeVersionFullConfigDTO dto = new InspectionSchemeVersionFullConfigDTO();
        dto.setId(version.getId());
        dto.setSchemeId(version.getSchemeId());
        dto.setSchemeName(scheme.getName());
        dto.setVersionNo(version.getVersionNo());
        dto.setStatus(version.getStatus());
        dto.setParentVersionId(version.getParentVersionId());
        dto.setDescription(version.getDescription());
        dto.setEffectiveDate(version.getEffectiveDate());
        dto.setCreateTime(version.getCreateTime());

        if (version.getParentVersionId() != null) {
            InspectionSchemeVersion parentVersion = inspectionSchemeVersionMapper.selectById(version.getParentVersionId());
            if (parentVersion != null) {
                dto.setParentVersionNo(parentVersion.getVersionNo());
            }
        }

        InspectionSchemeVersionFullConfigDTO.MaterialInfoDTO materialInfo = new InspectionSchemeVersionFullConfigDTO.MaterialInfoDTO();
        materialInfo.setMaterialId(version.getMaterialId());
        if (version.getMaterialId() != null) {
            com.bmos.lims2.server.material.entity.Material material = materialMapper.selectById(version.getMaterialId());
            if (material != null) {
                materialInfo.setMaterialName(material.getName());
                materialInfo.setMaterialCode(material.getCode());
                materialInfo.setUnitId(material.getUnitId());
                if (material.getUnitId() != null) {
                    materialInfo.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
                }
            }
        }
        dto.setMaterial(materialInfo);

        InspectionSchemeVersionFullConfigDTO.PackageInfoDTO packageInfo = new InspectionSchemeVersionFullConfigDTO.PackageInfoDTO();
        packageInfo.setPackageId(version.getPackageId());
        if (version.getPackageId() != null) {
            com.bmos.lims2.server.inspect.pack.entity.InspectPackage inspectPackage =
                    inspectPackageMapper.selectById(version.getPackageId());
            if (inspectPackage != null) {
                packageInfo.setPackageName(inspectPackage.getName());
                packageInfo.setPackageCode(inspectPackage.getCode());
            }
        }
        dto.setPackageInfo(packageInfo);

        return dto;
    }

    @Override
    public List<InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO> getVersionItemsConfig(Long id) {
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        InspectionSchemeVersionFullConfigDTO fullConfigDTO = new InspectionSchemeVersionFullConfigDTO();
        fillInspectionItems(fullConfigDTO, version, scheme);
        return fullConfigDTO.getInspectionItems() != null ? fullConfigDTO.getInspectionItems() : Collections.emptyList();
    }

    @Override
    public List<InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO> getVersionSamplingConfig(Long id) {
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            return Collections.emptyList();
        }
        List<InspectionSchemeSamplingDTO> samplingList =
                inspectionSchemeSamplingService.listInspectionSchemeSamplings(scheme.getId(), version.getId());
        if (samplingList == null || samplingList.isEmpty()) {
            return Collections.emptyList();
        }
        return samplingList.stream().map(item -> {
            InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO samplingConfig =
                    new InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO();
            BeanUtil.copyProperties(item, samplingConfig);
            samplingConfig.setSamplingConfigId(item.getId());
            return samplingConfig;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeInspectionSchemeVersion(Long id, InspectionSchemeBasicSaveDTO basicDTO,
                                                List<InspectionSchemeItemUpdateDTO> itemUpdates,
                                                List<InspectionSchemeSamplingUpdateDTO> samplingUpdates) {
        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 编辑中或已完成的版本均可重新完成
        if (InspectionSchemeVersionStatusEnum.EDITING != version.getStatus()
                && InspectionSchemeVersionStatusEnum.COMPLETED != version.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_STATE_ERROR);
        }

        // 保存基础信息（名称、版本号、物料等）
        basicDTO.setSchemeId(version.getSchemeId());
        basicDTO.setVersionId(id);
        inspectionSchemeService.saveInspectionSchemeBasic(basicDTO);

        // 保存检验项目配置（全量）
        if (CollUtil.isNotEmpty(itemUpdates)) {
            for (InspectionSchemeItemUpdateDTO item : itemUpdates) {
                if (item.getSchemeId() == null) item.setSchemeId(version.getSchemeId());
                if (item.getVersionId() == null) item.setVersionId(id);
            }
            inspectionSchemeService.updateInspectionSchemeItems(itemUpdates);
        }

        // 保存取样配置（全量）
        if (CollUtil.isNotEmpty(samplingUpdates)) {
            for (InspectionSchemeSamplingUpdateDTO sam : samplingUpdates) {
                if (sam.getSchemeId() == null) sam.setSchemeId(version.getSchemeId());
                if (sam.getVersionId() == null) sam.setVersionId(id);
            }
            inspectionSchemeService.updateInspectionSchemeSamplings(samplingUpdates);
        }

        // 校验判定条件引用的数据点配置是否仍然有效
        inspectionSchemeService.validateJudgmentConfigConsistency(id);

        // 校验：至少有一个检验项目
        List<InspectionSchemeItemDTO> schemeItems = inspectionSchemeItemService.listInspectionSchemeItems(id);
        if (CollUtil.isEmpty(schemeItems)) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NO_ITEM);
        }

        // 校验：每个检验项目至少有一个分析项，且分析项必填字段完整
        for (InspectionSchemeItemDTO item : schemeItems) {
            if (CollUtil.isEmpty(item.getParameters())) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_ITEM_NO_PARAMETER,
                        item.getInspectItemName() != null ? item.getInspectItemName() : String.valueOf(item.getId()));
            }
            for (InspectionSchemeParameterDTO parameter : item.getParameters()) {
                if (parameter.getParameterId() == null || parameter.getInspectItemId() == null) {
                    throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_PARAMETER_MISSING_REQUIRED,
                            item.getInspectItemName() != null ? item.getInspectItemName() : String.valueOf(item.getId()));
                }
            }
        }

        // 校验：至少有一条取样信息
        List<InspectionSchemeSamplingDTO> samplings =
                inspectionSchemeSamplingService.listInspectionSchemeSamplings(version.getSchemeId(), id);
        if (CollUtil.isEmpty(samplings)) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NO_SAMPLING);
        }

        // 重新加载版本（saveInspectionSchemeBasic 已更新了版本字段），再设置状态为已完成
        InspectionSchemeVersion latestVersion = inspectionSchemeVersionMapper.selectById(id);
        latestVersion.setStatus(InspectionSchemeVersionStatusEnum.COMPLETED);
        inspectionSchemeVersionMapper.updateById(latestVersion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateInspectionSchemeVersion(Long id) {
        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 查询方案
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_NOT_EXIST);
        }

        // 检查版本状态：只有已完成或已停用的版本才能启用
        if (InspectionSchemeVersionStatusEnum.COMPLETED != version.getStatus() && InspectionSchemeVersionStatusEnum.INACTIVE != version.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EDITABLE);
        }

        // 校验是否存在其他待审批的版本
        LambdaQueryWrapper<InspectionSchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeVersion::getSchemeId, version.getSchemeId())
                .eq(InspectionSchemeVersion::getStatus, InspectionSchemeVersionStatusEnum.APPROVING.getValue())
                .ne(InspectionSchemeVersion::getId, id);
        long approvingCount = inspectionSchemeVersionMapper.selectCount(wrapper);
        if (approvingCount > 0) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_HAS_APPROVING);
        }

        // 启用前校验：方案内所有检验项目均已配置班组
        List<InspectionSchemeItemDTO> schemeItems = inspectionSchemeItemService.listInspectionSchemeItems(version.getId());
        if (CollUtil.isNotEmpty(schemeItems)) {
            boolean existsItemWithoutTeam = schemeItems.stream()
                    .anyMatch(item -> item.getTeams() == null || item.getTeams().isEmpty());
            if (existsItemWithoutTeam) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_ITEM_TEAM_NOT_CONFIGURED);
            }

            // 启用前校验：当分析项执行方式为 ELN 时，要求其所有数据点均已绑定记录组件
            for (InspectionSchemeItemDTO item : schemeItems) {
                if (CollUtil.isEmpty(item.getParameters())) {
                    continue;
                }
                for (InspectionSchemeParameterDTO parameter : item.getParameters()) {
                    if (parameter.getExecuteMethod() == ExecuteMethodEnum.ELN) {
                        if (CollUtil.isEmpty(parameter.getDataPoints())) {
                            // 无数据点则不触发该校验
                            continue;
                        }
                        InspectionSchemeDataPointDTO invalid = null;
                        for (InspectionSchemeDataPointDTO dp : parameter.getDataPoints()) {
                            if (dp.getComponentId() == null) {
                                invalid = dp;
                                break;
                            }
                        }
                        if (invalid != null) {
                            String itemInfo = StrUtil.emptyToDefault(item.getInspectItemName(), StrUtil.emptyToDefault(item.getInspectItemCode(), "检验项目"));
                            String paramInfo = StrUtil.emptyToDefault(parameter.getParameterName(), StrUtil.emptyToDefault(parameter.getParameterCode(), "分析项"));
                            String dpInfo = StrUtil.emptyToDefault(invalid.getName(), "数据点");
                            throw new BmosException(LimsResponseCode.DATA_POINT_NOT_BIND_RECORD_COMPONENT,
                                    itemInfo, paramInfo, dpInfo);
                        }
                    }
                }
            }
        }

        // 发起审批流程
        FlowStartDTO startDTO = new FlowStartDTO();
        startDTO.setBusinessKey(String.valueOf(id));
        startDTO.setCode(AuditCategoryCodeEnum.SCHEME_AUDIT.getCode());
        startDTO.setCategoryCode(AuditCategoryCodeEnum.SCHEME_AUDIT.getCode());
        startDTO.setName(scheme.getName());
        startDTO.setExtField(version.getVersionNo());

        String processInstanceId = flowAuditService.flowAuditStart(startDTO);

        // 更新版本状态和审批流程ID
        version.setStatus(InspectionSchemeVersionStatusEnum.APPROVING);
        version.setProcessInstanceId(processInstanceId);
        inspectionSchemeVersionMapper.updateById(version);

        // 记录提交审批日志
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), id, OperationType.SUBMIT_AUDIT, null);

        log.info("检验方案版本审批流程已发起，版本ID: {}, 流程实例ID: {}", id, processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivateInspectionSchemeVersion(Long id) {
        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 检查版本状态
        if (InspectionSchemeVersionStatusEnum.ACTIVE != version.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_ACTIVE);
        }

        // 更新版本状态
        version.setStatus(InspectionSchemeVersionStatusEnum.INACTIVE);
        inspectionSchemeVersionMapper.updateById(version);

        // 清空方案的生效版本号（如果当前生效版本即为该版本）
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme != null && StrUtil.equals(scheme.getActiveVersionNo(), version.getVersionNo())) {
            scheme.setActiveVersionNo(null);
            inspectionSchemeMapper.updateById(scheme);
        }

        log.info("检验方案版本已停用，版本ID: {}", id);

        // 记录停用（回退到非生效状态）日志
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), id, OperationType.INVALID, null);
    }

    /**
     * 作废检验方案版本
     *
     * @param id 版本ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voidInspectionSchemeVersion(Long id) {
        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 检查版本状态
        if (InspectionSchemeVersionStatusEnum.EDITING != version.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EDITING);
        }

        // 更新版本状态
        version.setStatus(InspectionSchemeVersionStatusEnum.VOIDED);
        inspectionSchemeVersionMapper.updateById(version);

        // 清空方案的生效版本号（如果当前生效版本即为该版本）
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme != null && StrUtil.equals(scheme.getActiveVersionNo(), version.getVersionNo())) {
            scheme.setActiveVersionNo(null);
            inspectionSchemeMapper.updateById(scheme);
        }

        log.info("检验方案版本已作废，版本ID: {}", id);

        // 记录作废日志
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), id, OperationType.NULLIFY, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleApprovalPassed(Long versionId) {
        log.info("检验方案版本审批通过，版本ID: {}", versionId);

        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(versionId);
        if (version == null) {
            log.error("检验方案版本不存在，版本ID: {}", versionId);
            return;
        }

        // 检查当前状态
        if (InspectionSchemeVersionStatusEnum.APPROVING!=version.getStatus()) {
            log.warn("检验方案版本状态不是审批中，无法处理审批通过，版本ID: {}, 当前状态: {}", versionId, version.getStatus());
            return;
        }

        // 同一方案的其他活跃版本失效
        LambdaQueryWrapper<InspectionSchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeVersion::getSchemeId, version.getSchemeId())
                .eq(InspectionSchemeVersion::getStatus, InspectionSchemeVersionStatusEnum.ACTIVE.getValue())
                .ne(InspectionSchemeVersion::getId, versionId);

        List<InspectionSchemeVersion> activeVersions = inspectionSchemeVersionMapper.selectList(wrapper);
        for (InspectionSchemeVersion activeVersion : activeVersions) {
            activeVersion.setStatus(InspectionSchemeVersionStatusEnum.INACTIVE);
            inspectionSchemeVersionMapper.updateById(activeVersion);
            log.info("检验方案版本已失效，版本ID: {}", activeVersion.getId());
        }

        // 更新当前版本状态为生效
        version.setStatus(InspectionSchemeVersionStatusEnum.ACTIVE);
        inspectionSchemeVersionMapper.updateById(version);

        // 将生效版本号写入方案表 activeVersionNo 字段
        InspectionScheme schemeToUpdate = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (schemeToUpdate != null) {
            schemeToUpdate.setActiveVersionNo(version.getVersionNo());
            inspectionSchemeMapper.updateById(schemeToUpdate);
            log.info("已更新方案的生效版本号，方案ID: {}, 版本号: {}", schemeToUpdate.getId(), version.getVersionNo());
        }

        log.info("检验方案版本已生效，版本ID: {}", versionId);

        // 记录审批通过日志
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), versionId, OperationType.APPROVE_AUDIT, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleApprovalRejected(Long versionId) {
        log.info("检验方案版本审批拒绝，版本ID: {}", versionId);

        // 查询版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(versionId);
        if (version == null) {
            log.error("检验方案版本不存在，版本ID: {}", versionId);
            return;
        }

        // 检查当前状态
        if (InspectionSchemeVersionStatusEnum.APPROVING != version.getStatus()) {
            log.warn("检验方案版本状态不是审批中，无法处理审批拒绝，版本ID: {}, 当前状态: {}", versionId, version.getStatus());
            return;
        }

        // 更新版本状态为编辑中，清除流程实例ID
        version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
        version.setProcessInstanceId(null); // 修复：之前错误写成了approvalId
        inspectionSchemeVersionMapper.updateById(version);

        log.info("检验方案版本审批拒绝处理完成，版本状态已改为编辑中，版本ID: {}", versionId);

        // 记录审批不通过日志
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), versionId, OperationType.REJECT_AUDIT, null);
    }

    @Override
    public CommonPage<InspectionSchemeVersionAuditDTO> pageAuditSchemeVersions(InspectionSchemeVersionAuditQueryDTO queryDTO) {
        // 构建FlowAuditTaskDTO
        FlowAuditTaskDTO flowAuditTaskDTO = new FlowAuditTaskDTO();
        flowAuditTaskDTO.setCategory(AuditCategoryCodeEnum.SCHEME_AUDIT.getCode());
        flowAuditTaskDTO.setCurrent(queryDTO.getPageNum());
        flowAuditTaskDTO.setSize(queryDTO.getPageSize());
        flowAuditTaskDTO.setOrderBy(queryDTO.getOrderBy());
        flowAuditTaskDTO.setDir(queryDTO.getDir());

        // 先根据业务筛选条件查询版本数据
        List<InspectionSchemeVersionAuditDTO> records = getAuditTodoSchemeVersions(queryDTO);
        if (CollUtil.isEmpty(records)) {
            CommonPage<InspectionSchemeVersionAuditDTO> emptyPage = new CommonPage<>();
            emptyPage.setPageNum(queryDTO.getPageNum());
            emptyPage.setPageSize(queryDTO.getPageSize());
            emptyPage.setTotal(0);
            emptyPage.setList(new ArrayList<>());
            return emptyPage;
        }

        // 构建业务key到DTO的映射
        Map<Long, InspectionSchemeVersionAuditDTO> dataMap = records.stream()
                .collect(Collectors.toMap(InspectionSchemeVersionAuditDTO::getId, dto -> dto));

        // 设置业务key列表
        flowAuditTaskDTO.setBusinessKeyList(dataMap.keySet().stream()
                .map(String::valueOf).collect(Collectors.toList()));

        // 查询待办任务
        PageQueryResp<List<TaskListResp>> pageResult = flowAuditService.queryToDoListByCategory(flowAuditTaskDTO);
        if (pageResult.getTotal() == 0) {
            CommonPage<InspectionSchemeVersionAuditDTO> emptyPage = new CommonPage<>();
            emptyPage.setPageNum(queryDTO.getPageNum());
            emptyPage.setPageSize(queryDTO.getPageSize());
            emptyPage.setTotal(0);
            emptyPage.setList(new ArrayList<>());
            return emptyPage;
        }

        // 合并审批任务数据和业务数据
        List<InspectionSchemeVersionAuditDTO> resultList = new ArrayList<>();
        for (TaskListResp task : pageResult.getData()) {
            Long versionId = Long.valueOf(task.getBusinessKey());
            InspectionSchemeVersionAuditDTO auditDTO = dataMap.get(versionId);
            if (auditDTO != null) {
                // 设置审批任务相关信息
                auditDTO.setTaskId(task.getTaskId());
                auditDTO.setProcessInstanceId(task.getProcessInstanceId());
                auditDTO.setCurrentNodeName(task.getElementName());
                auditDTO.setInitiateTime(task.getProcessStartTime());
                auditDTO.setInitiator(task.getProcessStartBy());
                auditDTO.setDeploymentId(task.getDeploymentId());
                auditDTO.setPayload(task.getPayload());
                auditDTO.setExecutionId(task.getExecutionId());
                resultList.add(auditDTO);
            }
        }

        // 构建返回结果
        CommonPage<InspectionSchemeVersionAuditDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(queryDTO.getPageNum());
        resultPage.setPageSize(queryDTO.getPageSize());
        resultPage.setTotal(pageResult.getTotal().intValue());
        resultPage.setList(resultList);

        return resultPage;
    }

    /**
     * 根据筛选条件查询待审批的检验方案版本基础数据
     */
    private List<InspectionSchemeVersionAuditDTO> getAuditTodoSchemeVersions(InspectionSchemeVersionAuditQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<InspectionSchemeVersion> versionWrapper = new LambdaQueryWrapper<>();
        versionWrapper.eq(InspectionSchemeVersion::getStatus, InspectionSchemeVersionStatusEnum.APPROVING.getValue());

        // 如果有方案名称筛选条件
        if (StrUtil.isNotBlank(queryDTO.getSchemeName())) {
            LambdaQueryWrapper<InspectionScheme> schemeWrapper = new LambdaQueryWrapper<>();
            schemeWrapper.like(InspectionScheme::getName, queryDTO.getSchemeName());
            List<InspectionScheme> schemes = inspectionSchemeMapper.selectList(schemeWrapper);
            if (CollUtil.isEmpty(schemes)) {
                return new ArrayList<>();
            }
            List<Long> schemeIds = schemes.stream().map(InspectionScheme::getId).collect(Collectors.toList());
            versionWrapper.in(InspectionSchemeVersion::getSchemeId, schemeIds);
        }

        // 如果有物料ID筛选条件
        if (CollUtil.isNotEmpty(queryDTO.getMaterialIds())) {
            versionWrapper.in(InspectionSchemeVersion::getMaterialId, queryDTO.getMaterialIds());
        }

        // 查询版本数据
        List<InspectionSchemeVersion> versions = inspectionSchemeVersionMapper.selectList(versionWrapper);
        if (CollUtil.isEmpty(versions)) {
            return new ArrayList<>();
        }

        // 批量查询关联数据
        List<Long> schemeIds = versions.stream().map(InspectionSchemeVersion::getSchemeId).collect(Collectors.toList());
        List<InspectionScheme> schemes = inspectionSchemeMapper.selectBatchIds(schemeIds);
        Map<Long, InspectionScheme> schemeMap = schemes.stream()
                .collect(Collectors.toMap(InspectionScheme::getId, s -> s));

        // List<Long> versionIds = versions.stream().map(InspectionSchemeVersion::getId).collect(Collectors.toList());

        // 批量查询物料信息
        List<Long> materialIds = versions.stream()
                .map(InspectionSchemeVersion::getMaterialId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, Material> materialMap = new java.util.HashMap<>();
        if (CollUtil.isNotEmpty(materialIds)) {
            List<Material> materials = materialMapper.selectBatchIds(materialIds);
            materialMap = materials.stream()
                    .collect(Collectors.toMap(Material::getId, m -> m));
        }

        // 组装返回数据
        List<InspectionSchemeVersionAuditDTO> result = new ArrayList<>();
        for (InspectionSchemeVersion version : versions) {
            InspectionScheme scheme = schemeMap.get(version.getSchemeId());
            InspectionSchemeVersionAuditDTO auditDTO = new InspectionSchemeVersionAuditDTO();
            auditDTO.setId(version.getId());
            auditDTO.setVersionNo(version.getVersionNo());
            auditDTO.setDescription(version.getParentVersionId() != null ? "版本修订" : "新建版本");

            if (scheme != null) {
                auditDTO.setSchemeName(scheme.getName());
            }
            // 根据materialId获取物料信息
            Material material = materialMap.get(version.getMaterialId());
            if (material != null) {
                auditDTO.setMaterialName(material.getName());
                auditDTO.setMaterialCode(material.getCode());
            }

            result.add(auditDTO);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessSuccessCallBack(String processInstanceId, String comment, String userId) {
        log.info("检验方案版本审批成功回调，流程实例ID: {}, 审批人: {}", processInstanceId, userId);

        // 根据流程实例ID查询版本
        LambdaQueryWrapper<InspectionSchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeVersion::getProcessInstanceId, processInstanceId);
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectOne(wrapper);

        if (version == null) {
            log.error("根据流程实例ID未找到检验方案版本，流程实例ID: {}", processInstanceId);
            return;
        }

        // 检查当前状态
        if (InspectionSchemeVersionStatusEnum.APPROVING!=version.getStatus()) {
            log.warn("检验方案版本状态不是审批中，无法处理审批成功，版本ID: {}, 当前状态: {}", version.getId(), version.getStatus());
            return;
        }

        // 同一方案的其他活跃版本失效
        LambdaQueryWrapper<InspectionSchemeVersion> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(InspectionSchemeVersion::getSchemeId, version.getSchemeId())
                .eq(InspectionSchemeVersion::getStatus, InspectionSchemeVersionStatusEnum.ACTIVE.getValue())
                .ne(InspectionSchemeVersion::getId, version.getId());

        List<InspectionSchemeVersion> activeVersions = inspectionSchemeVersionMapper.selectList(activeWrapper);
        for (InspectionSchemeVersion activeVersion : activeVersions) {
            activeVersion.setStatus(InspectionSchemeVersionStatusEnum.INACTIVE);
            inspectionSchemeVersionMapper.updateById(activeVersion);
            log.info("检验方案版本已失效，版本ID: {}", activeVersion.getId());
        }

        // 更新当前版本状态为生效
        version.setStatus(InspectionSchemeVersionStatusEnum.ACTIVE);
        version.setEffectiveDate(LocalDate.now());
        inspectionSchemeVersionMapper.updateById(version);

        // 同步回写方案的生效版本号
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme != null) {
            scheme.setActiveVersionNo(version.getVersionNo());
            inspectionSchemeMapper.updateById(scheme);
        }

        log.info("检验方案版本已生效，版本ID: {}, 流程实例ID: {}", version.getId(), processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName) {
        log.info("检验方案版本审批拒绝回调，流程实例ID: {}, 审批人: {}, 节点: {}", processInstanceId, userId, nodeName);

        // 根据流程实例ID查询版本
        LambdaQueryWrapper<InspectionSchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeVersion::getProcessInstanceId, processInstanceId);
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectOne(wrapper);

        if (version == null) {
            log.error("根据流程实例ID未找到检验方案版本，流程实例ID: {}", processInstanceId);
            return;
        }

        // 检查当前状态
        if (InspectionSchemeVersionStatusEnum.APPROVING!=version.getStatus()) {
            log.warn("检验方案版本状态不是审批中，无法处理审批拒绝，版本ID: {}, 当前状态: {}", version.getId(), version.getStatus());
            return;
        }

        // 更新版本状态为编辑中，清除流程实例ID
        version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
        version.setProcessInstanceId(null);
        inspectionSchemeVersionMapper.updateById(version);

        log.info("检验方案版本审批拒绝处理完成，版本状态已改为编辑中，版本ID: {}, 流程实例ID: {}", version.getId(), processInstanceId);
        saveHistoryLog(comment, remark, userId, version.getId(), OperationType.REJECT_AUDIT, nodeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditExecutionSuccessCallBack(String businessKey, String comment, String remark, String userId, String nodeName) {
        log.info("检验方案版本审批执行成功回调，业务key: {}, 审批人: {}, 节点: {}", businessKey, userId, nodeName);
        saveHistoryLog(comment, remark, userId, Long.valueOf(businessKey), OperationType.APPROVE_AUDIT, nodeName);

    }

    private void saveHistoryLog(String comment, String remark, String userId, Long id, OperationType operationType, String nodeName) {
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.INSPECT_SCHEME.name())
                .businessId(id)
                .operationType(operationType.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(userId)
                .build());
    }

    /**
     * 判断选择类型判定值是否存在于组件选项中
     */
    private boolean isOptionValueInvalid(ExecuteMethodEnum executeMethod,
                                         InspectionSchemeDataPointDTO dp,
                                         String standardValue,
                                         Map<Long, Set<String>> componentOptionCache) {
        if (!ExecuteMethodEnum.ELN.equals(executeMethod) || dp == null || dp.getPointType() != DataPointTypeEnum.OPTION) {
            return false;
        }
        if (StrUtil.isBlank(standardValue)) {
            return false;
        }
        if (dp.getComponentId() == null) {
            return true;
        }
        Set<String> optionFields = loadComponentOptionFields(dp.getComponentId(), componentOptionCache);
        if (CollUtil.isEmpty(optionFields)) {
            return true;
        }
        List<String> values = parseStandardValues(standardValue);
        for (String value : values) {
            if (StrUtil.isBlank(value) || !optionFields.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取组件选项字段集合
     */
    private Set<String> loadComponentOptionFields(Long componentId, Map<Long, Set<String>> cache) {
        if (componentId == null) {
            return Collections.emptySet();
        }
        if (cache.containsKey(componentId)) {
            return cache.get(componentId);
        }
        Set<String> fields = new HashSet<>();
        BatchRecordComponent component = batchRecordComponentMapper.selectWithDetailById(componentId);
        if (component != null && StrUtil.isNotBlank(component.getComponentDetail())) {
            try {
                JSONArray array = JSON.parseArray(component.getComponentDetail());
                if (array != null) {
                    array.forEach(obj -> {
                        try {
                            String field = JSON.parseObject(JSON.toJSONString(obj)).getString("field");
                            if (StrUtil.isNotBlank(field)) {
                                fields.add(field);
                            }
                        } catch (Exception ignored) {
                            // 单条解析失败忽略
                        }
                    });
                }
            } catch (Exception ignored) {
                // 解析失败交由上层处理
            }
        }
        cache.put(componentId, fields);
        return fields;
    }

    /**
     * 解析判定标准值，支持 JSON 数组或逗号分隔字符串
     */
    private List<String> parseStandardValues(String standardValue) {
        if (StrUtil.isBlank(standardValue)) {
            return Collections.emptyList();
        }
        try {
            JSONArray array = JSON.parseArray(standardValue);
            if (array != null) {
                List<String> values = new ArrayList<>();
                array.forEach(obj -> {
                    if (obj == null) {
                        return;
                    }
                    if (obj instanceof JSONObject) {
                        String field = ((JSONObject) obj).getString("field");
                        if (StrUtil.isNotBlank(field)) {
                            values.add(StrUtil.trim(field));
                            return;
                        }
                    }
                    values.add(StrUtil.trim(obj.toString()));
                });
                if (CollUtil.isNotEmpty(values)) {
                    return values;
                }
            }
        } catch (Exception ignored) {
            // 非 JSON 数组时，走逗号分隔解析
        }
        List<String> split = StrUtil.splitTrim(standardValue, ',');
        if (CollUtil.isEmpty(split)) {
            return Collections.singletonList(StrUtil.trim(standardValue));
        }
        return split;
    }

    private void fillInspectionItems(InspectionSchemeVersionFullConfigDTO fullConfigDTO,
                                     InspectionSchemeVersion version,
                                     InspectionScheme scheme) {
        List<InspectionSchemeItemDTO> inspectionItems = inspectionSchemeItemService.listInspectionSchemeItems(version.getId());
        if (inspectionItems == null || inspectionItems.isEmpty()) {
            return;
        }
        // 检验项目排序，项目内分析项排序
        inspectionItems.sort((a, b) -> {
            String ca = a.getInspectItemCode();
            String cb = b.getInspectItemCode();
            if (ca == null && cb == null) return 0;
            if (ca == null) return 1;
            if (cb == null) return -1;
            return ca.compareTo(cb);
        });
        for (InspectionSchemeItemDTO item : inspectionItems) {
            if (item.getParameters() != null && !item.getParameters().isEmpty()) {
                item.getParameters().sort((p1, p2) -> {
                    String c1 = p1.getParameterCode();
                    String c2 = p2.getParameterCode();
                    if (c1 == null && c2 == null) return 0;
                    if (c1 == null) return 1;
                    if (c2 == null) return -1;
                    return c1.compareTo(c2);
                });
            }
        }

        List<InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO> itemConfigs = new ArrayList<>();
        for (InspectionSchemeItemDTO inspectionItem : inspectionItems) {
            InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO itemConfig = new InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO();
            BeanUtil.copyProperties(inspectionItem, itemConfig);
            itemConfig.setItemConfigId(inspectionItem.getId());

            if (inspectionItem.getParameters() != null && !inspectionItem.getParameters().isEmpty()) {
                List<InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO> analyzeItemConfigs = new ArrayList<>();
                for (InspectionSchemeParameterDTO parameter : inspectionItem.getParameters()) {
                    InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO parameterConfigDTO = new InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO();
                    BeanUtil.copyProperties(parameter, parameterConfigDTO);
                    parameterConfigDTO.setParameterConfigId(parameter.getId());
                    // 数据点
                    if (parameter.getDataPoints() != null && !parameter.getDataPoints().isEmpty()) {
                        List<InspectionSchemeVersionFullConfigDTO.DataPointConfigDTO> dataPointConfigs = new ArrayList<>();
                        for (InspectionSchemeDataPointDTO dataPoint : parameter.getDataPoints()) {
                            InspectionSchemeVersionFullConfigDTO.DataPointConfigDTO dataPointConfig = new InspectionSchemeVersionFullConfigDTO.DataPointConfigDTO();
                            BeanUtil.copyProperties(dataPoint, dataPointConfig);
                            dataPointConfig.setDataPointConfigId(dataPoint.getId());
                            dataPointConfigs.add(dataPointConfig);
                        }
                        parameterConfigDTO.setDataPoints(dataPointConfigs);
                    }
                    computeJudgmentConsistency(parameter, parameterConfigDTO);
                    analyzeItemConfigs.add(parameterConfigDTO);
                }
                itemConfig.setInspectionParameters(analyzeItemConfigs);
            }
            itemConfigs.add(itemConfig);
        }
        fullConfigDTO.setInspectionItems(itemConfigs);
    }

    private void computeJudgmentConsistency(InspectionSchemeParameterDTO parameter,
                                            InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO parameterConfigDTO) {
        if (parameter.getJudgments() == null || parameter.getJudgments().isEmpty()) {
            parameterConfigDTO.setJudgmentConfigError(false);
            parameterConfigDTO.setJudgmentDataPointDeleted(false);
            parameterConfigDTO.setJudgmentDataPointBindingMissing(false);
            parameterConfigDTO.setJudgmentDataPointTypeChanged(false);
            parameterConfigDTO.setJudgmentDataPointOptionInvalid(false);
            return;
        }
        parameterConfigDTO.setJudgments(new ArrayList<>());
        java.util.Set<Long> aliveDpConfigIds = new java.util.HashSet<>();
        java.util.Map<Long, InspectionSchemeDataPointDTO> configIdToDp = new java.util.HashMap<>();
        java.util.Map<Long, InspectionSchemeDataPointDTO> originalIdToDp = new java.util.HashMap<>();
        Map<Long, Set<String>> componentOptionCache = new HashMap<>();
        if (parameter.getDataPoints() != null) {
            for (InspectionSchemeDataPointDTO dp : parameter.getDataPoints()) {
                if (dp.getId() != null) {
                    aliveDpConfigIds.add(dp.getId());
                    configIdToDp.put(dp.getId(), dp);
                }
                if (dp.getDataPointId() != null) {
                    originalIdToDp.put(dp.getDataPointId(), dp);
                }
            }
        }

        boolean judgmentConfigError = false;
        boolean anyPointDeleted = false;
        boolean anyPointBindingMissing = false;
        boolean anyPointTypeChanged = false;
        boolean anyPointOptionInvalid = false;
        for (InspectionSchemeJudgmentDTO j : parameter.getJudgments()) {
            InspectionSchemeVersionFullConfigDTO.JudgmentConfigDTO judgmentConfig = new InspectionSchemeVersionFullConfigDTO.JudgmentConfigDTO();
            BeanUtil.copyProperties(j, judgmentConfig);
            parameterConfigDTO.getJudgments().add(judgmentConfig);

            judgmentConfig.setDataPointDeleted(false);
            judgmentConfig.setDataPointBindingMissing(false);
            judgmentConfig.setDataPointOptionInvalid(false);

            if (j.getDataPointConfigId() != null && !aliveDpConfigIds.contains(j.getDataPointConfigId())) {
                judgmentConfig.setDataPointDeleted(true);
                judgmentConfigError = true;
                anyPointDeleted = true;
                continue;
            }
            InspectionSchemeDataPointDTO dp = resolveJudgmentDataPoint(j, configIdToDp, originalIdToDp);
            if (dp != null) {
                if (j.getPointType() != null && dp.getPointType() != null && !j.getPointType().equals(dp.getPointType())) {
                    judgmentConfigError = true;
                    anyPointTypeChanged = true;
                }
                if (ExecuteMethodEnum.ELN.equals(parameter.getExecuteMethod())
                        && (dp.getComponentId() == null || dp.getFieldId() == null)) {
                    judgmentConfig.setDataPointBindingMissing(true);
                    judgmentConfigError = true;
                    anyPointBindingMissing = true;
                }
                if (isOptionValueInvalid(parameter.getExecuteMethod(), dp, j.getStandardValue(), componentOptionCache)) {
                    judgmentConfig.setDataPointOptionInvalid(true);
                    judgmentConfigError = true;
                    anyPointOptionInvalid = true;
                }
            }
        }
        parameterConfigDTO.setJudgmentConfigError(judgmentConfigError);
        parameterConfigDTO.setJudgmentDataPointDeleted(anyPointDeleted);
        parameterConfigDTO.setJudgmentDataPointBindingMissing(anyPointBindingMissing);
        parameterConfigDTO.setJudgmentDataPointTypeChanged(anyPointTypeChanged);
        parameterConfigDTO.setJudgmentDataPointOptionInvalid(anyPointOptionInvalid);
    }

    private InspectionSchemeDataPointDTO resolveJudgmentDataPoint(InspectionSchemeJudgmentDTO j,
                                                                  Map<Long, InspectionSchemeDataPointDTO> configIdToDp,
                                                                  Map<Long, InspectionSchemeDataPointDTO> originalIdToDp) {
        if (j.getDataPointConfigId() != null) {
            return configIdToDp.get(j.getDataPointConfigId());
        } else if (j.getDataPointId() != null) {
            return originalIdToDp.get(j.getDataPointId());
        }
        return null;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditExecutionRejectCallBack(String businessKey, String comment, String userId) {
        log.info("检验方案版本审批执行拒绝回调，业务key: {}, 审批人: {}", businessKey, userId);
        // 回退到上一节点（执行拒绝）记录操作日志
        saveHistoryLog(comment, null, userId, Long.valueOf(businessKey), OperationType.BACK_AUDIT, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyVersionFromSource(InspectionSchemeVersionCopyDTO dto) {
        // 1. 校验源版本
        InspectionSchemeVersion source = inspectionSchemeVersionMapper.selectById(dto.getSourceVersionId());
        if (source == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 2. 校验新版本号在同一方案下唯一
        Integer dup = inspectionSchemeVersionMapper.checkVersionNoDuplicate(source.getSchemeId(), dto.getNewVersionNo(), null);
        if (dup != null && dup > 0) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_EXISTS);
        }

        // 3. 创建新版本（继承物料、包等基础信息）
        InspectionSchemeVersion newVersion = new InspectionSchemeVersion();
        newVersion.setSchemeId(source.getSchemeId());
        newVersion.setVersionNo(dto.getNewVersionNo());
        newVersion.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
        newVersion.setParentVersionId(source.getId());
        newVersion.setDescription(dto.getDescription());
        newVersion.setMaterialId(source.getMaterialId());
        newVersion.setMaterialCode(source.getMaterialCode());
        newVersion.setPackageId(source.getPackageId());
        newVersion.setPackageCode(source.getPackageCode());
        inspectionSchemeVersionMapper.insert(newVersion);

        // 4. 读取源版本的完整配置
        InspectionSchemeVersionFullConfigDTO full = getInspectionSchemeVersionFullConfig(source.getId());

        // 5. 复制检验项目配置到新版本
        if (full.getInspectionItems() != null && !full.getInspectionItems().isEmpty()) {
            List<com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemSaveDTO> items = new java.util.ArrayList<>();
            for (InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO itemConfig : full.getInspectionItems()) {
                com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemSaveDTO itemSave = new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemSaveDTO();
                itemSave.setInspectItemId(itemConfig.getInspectItemId());
                itemSave.setIsRequired(itemConfig.getIsRequired());
                itemSave.setSort(itemConfig.getSort());
                itemSave.setDuration(itemConfig.getDuration());
                itemSave.setTimeUnit(itemConfig.getTimeUnit());
                itemSave.setRemark(itemConfig.getRemark());
                itemSave.setTeams(itemConfig.getTeams());

                if (itemConfig.getInspectionParameters() != null && !itemConfig.getInspectionParameters().isEmpty()) {
                    List<com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO> params = new java.util.ArrayList<>();
                    for (InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO p : itemConfig.getInspectionParameters()) {
                        com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO ps = new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO();
                        ps.setParameterId(p.getParameterId());
                        ps.setStandardRule(p.getStandardRule());
                        ps.setRecordId(p.getRecordId());
                        ps.setRecordCode(p.getRecordCode());
                        ps.setRecordVersionId(p.getRecordVersionId());
                        ps.setIsReportable(p.getIsReportable());
                        ps.setIsExecutable(p.getIsExecutable());
                        ps.setFinalExpression(p.getFinalExpression());
                        ps.setExecuteMethod(p.getExecuteMethod());
                        ps.setRecordItemId(p.getRecordItemId());
                        // ensure relational field for parameter-level record
                        ps.setInspectItemId(itemConfig.getInspectItemId());
                        HashMap<Long, Long> dataPointConfigIdMap = new HashMap<>();
                        // 数据点
                        if (p.getDataPoints() != null && !p.getDataPoints().isEmpty()) {
                            List<com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointSaveDTO> dps = new java.util.ArrayList<>();
                            for (InspectionSchemeVersionFullConfigDTO.DataPointConfigDTO dp : p.getDataPoints()) {
                                com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointSaveDTO d = new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointSaveDTO();
                                d.setDataPointId(dp.getDataPointId());
                                d.setName(dp.getName());
                                d.setPointType(dp.getPointType());
                                d.setTrendLineConfig(dp.getTrendLineConfig());
                                d.setOptions(dp.getOptions());
                                d.setReportDisplay(dp.getReportDisplay());
                                d.setDateStyle(dp.getDateStyle());
                                d.setTimeFormat(dp.getTimeFormat());
                                // 补齐新增字段复制
                                d.setRoundingUp(dp.getRoundingUp());
                                // ELN 绑定字段透传
                                d.setRecordId(dp.getRecordId());
                                d.setRecordVersionId(dp.getRecordVersionId());
                                d.setComponentId(dp.getComponentId());
                                d.setRecordItemId(dp.getRecordItemId());
                                d.setFieldId(dp.getFieldId());
                                // ensure relational fields are set
                                d.setInspectParameterId(dp.getParameterId());
                                d.setInspectItemId(itemConfig.getInspectItemId());
                                d.setId(IdUtils.getSnowflake());
                                dps.add(d);
                                dataPointConfigIdMap.put(dp.getDataPointConfigId(), d.getId());
                            }
                            ps.setDataPoints(dps);
                        }
                        // 判定
                        if (p.getJudgments() != null && !p.getJudgments().isEmpty()) {
                            List<com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentSaveDTO> jds = new java.util.ArrayList<>();
                            for (InspectionSchemeVersionFullConfigDTO.JudgmentConfigDTO j : p.getJudgments()) {
                                com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentSaveDTO jd = new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentSaveDTO();
                                jd.setJudgmentType(j.getJudgmentType());
                                jd.setDefaultResult(j.getDefaultResult());
                                jd.setMinValue(j.getMinValue());
                                jd.setMaxValue(j.getMaxValue());
                                jd.setMinOperator(j.getMinOperator());
                                jd.setMaxOperator(j.getMaxOperator());
                                jd.setStandardValue(j.getStandardValue());
                                jd.setExpression(j.getExpression());
                                jd.setDataPointId(j.getDataPointId());
                                jd.setDataPointConfigId(j.getDataPointConfigId());
                                jd.setPointType(j.getPointType());
                                jd.setJudgementConfigName(j.getJudgementConfigName());
                                jd.setMaxTime(j.getMaxTime());
                                jd.setMinTime(j.getMinTime());
                                // ensure relational fields are set
                                jd.setInspectParameterId(p.getParameterId());
                                jd.setDataPointConfigId(dataPointConfigIdMap.get(j.getDataPointConfigId()));
                                jd.setInspectItemId(itemConfig.getInspectItemId());
                                jds.add(jd);
                            }
                            ps.setJudgments(jds);
                        }
                        params.add(ps);
                    }
                    itemSave.setParameters(params);
                }
                items.add(itemSave);
            }
            // 保存（会清除旧的versionId对应配置，不影响新建）
            inspectionSchemeItemService.saveInspectionSchemeItems(source.getSchemeId(), newVersion.getId(), source.getPackageId(), items);

            // 复制组件配置
            List<InspectionSchemeParameter> newParams = inspectionSchemeParameterMapper.selectByVersionId(newVersion.getId());
            Map<String, Long> newParamIdMap = newParams.stream().collect(
                    Collectors.toMap(
                            np -> np.getInspectItemId() + "_" + np.getParameterId(),
                            InspectionSchemeParameter::getId,
                            (a, b) -> a));
            for (InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO itemConfig : full.getInspectionItems()) {
                if (itemConfig.getInspectionParameters() == null) continue;
                for (InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO p : itemConfig.getInspectionParameters()) {
                    Long newParamConfigId = newParamIdMap.get(itemConfig.getInspectItemId() + "_" + p.getParameterId());
                    if (newParamConfigId == null) continue;
                    List<SchemeParameterComponentConfig> srcConfigs = schemeParameterComponentConfigMapper.selectByParameterConfigId(p.getParameterConfigId());
                    if (srcConfigs.isEmpty()) continue;
                    List<SchemeParameterComponentConfig> newConfigs = new ArrayList<>();
                    for (SchemeParameterComponentConfig sc : srcConfigs) {
                        SchemeParameterComponentConfig nc = new SchemeParameterComponentConfig();
                        nc.setParameterId(sc.getParameterId());
                        nc.setParameterConfigId(newParamConfigId);
                        nc.setSchemeId(source.getSchemeId());
                        nc.setSchemeVersionId(String.valueOf(newVersion.getId()));
                        nc.setRecordItemId(sc.getRecordItemId());
                        nc.setRecordVersionId(sc.getRecordVersionId());
                        nc.setConfigInfo(sc.getConfigInfo());
                        nc.setComponentId(sc.getComponentId());
                        nc.setFieldId(sc.getFieldId());
                        newConfigs.add(nc);
                    }
                    schemeParameterComponentConfigMapper.insertBatch(newConfigs);
                }
            }
        }

        // 6. 复制取样配置
        if (full.getSamplingConfigs() != null && !full.getSamplingConfigs().isEmpty()) {
            List<com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingSaveDTO> sams = new java.util.ArrayList<>();
            for (InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO s : full.getSamplingConfigs()) {
                com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingSaveDTO sd = new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingSaveDTO();
                sd.setSchemeId(source.getSchemeId());
                sd.setVersionId(newVersion.getId());
                sd.setInspectItemId(s.getInspectItemId());
                sd.setSamplingAmount(s.getSamplingAmount());
                sd.setSamplingUnit(s.getSamplingUnit());
                sd.setSamplingCount(s.getSamplingCount());
                sams.add(sd);
            }
            inspectionSchemeSamplingService.saveInspectionSchemeSamplings(source.getSchemeId(), newVersion.getId(), sams);
        }

        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.INSPECT_SCHEME.name())
                .businessId(newVersion.getId())
                .operationType(OperationType.SAVE.getValue())
                .createBy(SysUserHolder.getUser().getUserId())
                .build());

        return newVersion.getId();
    }

    @Override
    public java.util.List<com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOperateRuleDTO> listOperateRulesByVersionId(Long schemeVersionId) {
        if (schemeVersionId == null) {
            return java.util.Collections.emptyList();
        }
        return inspectionSchemeVersionMapper.listOperateRulesByVersionId(schemeVersionId);
    }
} 