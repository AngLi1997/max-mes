package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.github.pagehelper.PageHelper;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.scheme.dto.*;
import com.bmos.lims2.server.inspect.scheme.dto.request.*;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeCopyFromVersionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSaveFusionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeBasicSaveRespDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeItemService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeSamplingService;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.eln.record.entity.SchemeParameterComponentConfig;
import com.bmos.lims2.server.eln.record.mapper.SchemeParameterComponentConfigMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.lims2.server.inspect.pack.entity.InspectPackage;
import com.bmos.lims2.server.inspect.pack.mapper.InspectPackageMapper;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 检验方案Service实现类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
public class InspectionSchemeServiceImpl implements InspectionSchemeService {

    @Autowired
    private InspectionSchemeMapper inspectionSchemeMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private InspectionSchemeItemService inspectionSchemeItemService;

    @Autowired
    private InspectionSchemeSamplingService inspectionSchemeSamplingService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private InspectPackageMapper inspectPackageMapper;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private com.bmos.lims2.server.inspect.pack.service.InspectPackageService inspectPackageService;

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Autowired
    private SchemeParameterComponentConfigMapper schemeParameterComponentConfigMapper;

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveInspectionScheme(InspectionSchemeSaveDTO saveDTO) {
        // 检查名称是否重复
        checkNameDuplicate(saveDTO.getName(), null);

        // 保存检验方案（包含物料和实验包信息）
        InspectionScheme scheme = BeanUtil.copyProperties(saveDTO, InspectionScheme.class);
        if (saveDTO.getMaterial() != null) {
            scheme.setMaterialId(saveDTO.getMaterial().getMaterialId());
            scheme.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
        }
        if (saveDTO.getPackageInfo() != null) {
            scheme.setPackageId(saveDTO.getPackageInfo().getPackageId());
            scheme.setPackageCode(saveDTO.getPackageInfo().getPackageCode());
        }
        inspectionSchemeMapper.insert(scheme);

        // 检查版本号是否在方案内重复（对于新方案，这个检查实际上总是通过的，但保持一致性）
        Integer versionCount = inspectionSchemeVersionMapper.checkVersionNoDuplicate(scheme.getId(), saveDTO.getVersionNo(), null);
        if (versionCount > 0) {
            throw new RuntimeException("版本号在该方案中已存在");
        }

        // 保存初始版本（包含物料和实验包信息）
        InspectionSchemeVersion version = new InspectionSchemeVersion();
        version.setSchemeId(scheme.getId());
        version.setVersionNo(saveDTO.getVersionNo());
        version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
        version.setParentVersionId(saveDTO.getParentVersionId());
        version.setDescription(saveDTO.getDescription());
        if (saveDTO.getMaterial() != null) {
            version.setMaterialId(saveDTO.getMaterial().getMaterialId());
            version.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
        }
        if (saveDTO.getPackageInfo() != null) {
            version.setPackageId(saveDTO.getPackageInfo().getPackageId());
            version.setPackageCode(saveDTO.getPackageInfo().getPackageCode());
        }
        inspectionSchemeVersionMapper.insert(version);

        // 记录新增版本日志
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.INSPECT_SCHEME.name())
                .businessId(version.getId())
                .operationType(OperationType.SAVE.getValue())
                .createBy(SysUserHolder.getUser().getUserId())
                .build());

        // 保存检验项目配置和取样配置（不再保存明细记录）
        if (saveDTO.getInspectionItems() != null || saveDTO.getSamplingConfigs() != null) {
            saveSchemeDetails(scheme,version.getId(), saveDTO);
        }

        // 注意：父版本关系已在version中记录，配置复制由前端处理

        return scheme.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InspectionSchemeBasicSaveRespDTO saveInspectionSchemeBasic(InspectionSchemeBasicSaveDTO saveDTO) {
        // 如果传入了schemeId和versionId，则走编辑逻辑；否则按新增逻辑处理
        if (saveDTO.getSchemeId() != null && saveDTO.getVersionId() != null) {
            // 加载并校验版本与方案
            InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(saveDTO.getVersionId());
            if (version == null) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
            }
            if (!version.getSchemeId().equals(saveDTO.getSchemeId())) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
            }

            InspectionScheme scheme = inspectionSchemeMapper.selectById(saveDTO.getSchemeId());
            if (scheme == null) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_NOT_EXIST);
            }

            revertToEditingIfCompleted(saveDTO.getVersionId());
            // 仅允许编辑“编辑中”的版本
            if (version.getStatus() != InspectionSchemeVersionStatusEnum.EDITING) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_STATE_ERROR);
            }

            // 名称唯一性（排除自身）
            checkNameDuplicate(saveDTO.getName(), scheme.getId());
            // 版本号在同方案下唯一（排除当前版本）
            Integer dup = inspectionSchemeVersionMapper.checkVersionNoDuplicate(scheme.getId(), saveDTO.getVersionNo(), version.getId());
            if (dup != null && dup > 0) {
                throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_EXISTS);
            }

            // 更新方案基础信息
            scheme.setName(saveDTO.getName());
            if (saveDTO.getMaterial() != null) {
                scheme.setMaterialId(saveDTO.getMaterial().getMaterialId());
                scheme.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            inspectionSchemeMapper.updateById(scheme);

            // 更新版本基础信息
            version.setVersionNo(saveDTO.getVersionNo());
            if (saveDTO.getParentVersionId() != null) {
                version.setParentVersionId(saveDTO.getParentVersionId());
            }
            version.setDescription(saveDTO.getDescription());
            if (saveDTO.getMaterial() != null) {
                version.setMaterialId(saveDTO.getMaterial().getMaterialId());
                version.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            inspectionSchemeVersionMapper.updateById(version);

            // 记录编辑版本日志
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.INSPECT_SCHEME.name())
                    .businessId(version.getId())
                    .operationType(OperationType.REDACT.getValue())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build());

            // 返回结果
            InspectionSchemeBasicSaveRespDTO respDTO = new InspectionSchemeBasicSaveRespDTO();
            respDTO.setSchemeId(scheme.getId());
            respDTO.setVersionId(version.getId());
            respDTO.setName(scheme.getName());
            respDTO.setVersionNo(version.getVersionNo());
            respDTO.setDescription(version.getDescription());
            return respDTO;
        } else {
            // 新增逻辑
            // 检查名称是否重复
            checkNameDuplicate(saveDTO.getName(), null);

            // 保存检验方案基础信息
            InspectionScheme scheme = BeanUtil.copyProperties(saveDTO, InspectionScheme.class);
            if (saveDTO.getMaterial() != null) {
                scheme.setMaterialId(saveDTO.getMaterial().getMaterialId());
                scheme.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            inspectionSchemeMapper.insert(scheme);

            // 检查版本号是否在方案内重复
            Integer versionCount = inspectionSchemeVersionMapper.checkVersionNoDuplicate(scheme.getId(), saveDTO.getVersionNo(), null);
            if (versionCount > 0) {
                throw new RuntimeException("版本号在该方案中已存在");
            }

            // 保存初始版本
            InspectionSchemeVersion version = new InspectionSchemeVersion();
            version.setSchemeId(scheme.getId());
            version.setVersionNo(saveDTO.getVersionNo());
            version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
            version.setParentVersionId(saveDTO.getParentVersionId());
            version.setDescription(saveDTO.getDescription());
            if (saveDTO.getMaterial() != null) {
                version.setMaterialId(saveDTO.getMaterial().getMaterialId());
                version.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            inspectionSchemeVersionMapper.insert(version);

            // 记录新增版本日志
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.INSPECT_SCHEME.name())
                    .businessId(version.getId())
                    .operationType(OperationType.SAVE.getValue())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build());

            // 构建返回结果
            InspectionSchemeBasicSaveRespDTO respDTO = new InspectionSchemeBasicSaveRespDTO();
            respDTO.setSchemeId(scheme.getId());
            respDTO.setVersionId(version.getId());
            respDTO.setName(scheme.getName());
            respDTO.setVersionNo(version.getVersionNo());
            respDTO.setDescription(version.getDescription());
            return respDTO;
        }
    }

    /**
     * 确保版本可编辑：EDITING 直接通过；COMPLETED 自动回退为 EDITING；其他状态抛异常
     */
    private void revertToEditingIfCompleted(Long versionId) {
        if (versionId == null) return;
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(versionId);
        if (version == null) return;
        if (version.getStatus() == InspectionSchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
            inspectionSchemeVersionMapper.updateById(version);
        } else if (version.getStatus() != InspectionSchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_STATE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initInspectionItemsByPackage(Long schemeId, Long versionId, Long packageId) {
        // 获取实验包的完整配置信息
        com.bmos.lims2.server.inspect.pack.dto.InspectPackageFullConfigDTO fullConfig = 
            inspectPackageService.getFullConfigByPackageId(packageId);

        if (fullConfig == null || CollUtil.isEmpty(fullConfig.getInspectionItems())) {
            return;
        }

        // 转换为检验项目配置保存DTO
        List<InspectionSchemeItemSaveDTO> inspectionItemSaveDTOs = new ArrayList<>();
        int sort = 1;
        
        for (com.bmos.lims2.server.inspect.pack.dto.InspectPackageFullConfigDTO.InspectionItemDTO itemDTO : fullConfig.getInspectionItems()) {
            InspectionSchemeItemSaveDTO itemSaveDTO = new InspectionSchemeItemSaveDTO();
            itemSaveDTO.setInspectItemId(itemDTO.getInspectItemId());
            itemSaveDTO.setIsRequired(itemDTO.getIsRequired() != null ? itemDTO.getIsRequired() : true);
            itemSaveDTO.setSort(sort++);
            itemSaveDTO.setRemark(itemDTO.getRemark());

            // 转换分析项配置
            if (CollUtil.isNotEmpty(itemDTO.getInspectionParameters())) {
                List<InspectionSchemeParameterSaveDTO> parameterSaveDTOs = new ArrayList<>();
                
                for (com.bmos.lims2.server.inspect.pack.dto.InspectPackageFullConfigDTO.AnalysisItemDTO analysisDTO : itemDTO.getInspectionParameters()) {
                    InspectionSchemeParameterSaveDTO parameterSaveDTO = new InspectionSchemeParameterSaveDTO();
                    parameterSaveDTO.setParameterId(analysisDTO.getParameterId());
                    parameterSaveDTO.setStandardRule(analysisDTO.getStandard());
                    parameterSaveDTO.setIsReportable(analysisDTO.getIsReportable() != null ? analysisDTO.getIsReportable() : true);
                    parameterSaveDTO.setIsExecutable(analysisDTO.getIsExecutable() != null ? analysisDTO.getIsExecutable() : true);
                    parameterSaveDTO.setExecuteMethod(ExecuteMethodEnum.LIMS);
                    parameterSaveDTO.setInspectItemId(itemSaveDTO.getInspectItemId());

                    // 转换数据点配置
                    if (CollUtil.isNotEmpty(analysisDTO.getDataPoints())) {
                        List<InspectionSchemeDataPointSaveDTO> dataPointSaveDTOs = new ArrayList<>();
                        
                        for (com.bmos.lims2.server.inspect.pack.dto.InspectPackageFullConfigDTO.DataPointDTO dataPointDTO : analysisDTO.getDataPoints()) {
                            InspectionSchemeDataPointSaveDTO pointSaveDTO = new InspectionSchemeDataPointSaveDTO();
                            pointSaveDTO.setDataPointId(dataPointDTO.getDataPointId());
                            pointSaveDTO.setName(dataPointDTO.getName());
                            pointSaveDTO.setPointType(dataPointDTO.getPointType());
                            pointSaveDTO.setTimeFormat(dataPointDTO.getTimeFormat());
                            pointSaveDTO.setDateStyle(dataPointDTO.getDateStyle());
                            pointSaveDTO.setReportDisplay(dataPointDTO.getReportDisplay() != null ? dataPointDTO.getReportDisplay() : false);
                            pointSaveDTO.setInspectParameterId(parameterSaveDTO.getParameterId());
                            pointSaveDTO.setInspectItemId(itemSaveDTO.getInspectItemId());
                            
                            // 处理选项配置
                            if (CollUtil.isNotEmpty(dataPointDTO.getOptions())) {
                                pointSaveDTO.setOptions(JSON.toJSONString(dataPointDTO.getOptions()));
                            }

                            // 处理趋势线配置
                            if (CollUtil.isNotEmpty(dataPointDTO.getTrends())) {
                                // 这里可以根据需要处理趋势线配置
                                // 暂时简化处理
                                pointSaveDTO.setTrendLineConfig(JSON.toJSONString(dataPointDTO.getTrends()));
                            }

                            dataPointSaveDTOs.add(pointSaveDTO);
                        }
                        parameterSaveDTO.setDataPoints(dataPointSaveDTOs);
                    }

                    parameterSaveDTOs.add(parameterSaveDTO);
                }
                itemSaveDTO.setParameters(parameterSaveDTOs);
            }

            inspectionItemSaveDTOs.add(itemSaveDTO);
        }

        // 保存检验项目配置
        inspectionSchemeItemService.saveInspectionSchemeItems(schemeId, versionId, packageId, inspectionItemSaveDTOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInspectionSchemeItems(List<InspectionSchemeItemUpdateDTO> updateDTO) {
        // 委托给子服务处理
        inspectionSchemeItemService.updateInspectionSchemeItems(updateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInspectionSchemeSamplings(List<InspectionSchemeSamplingUpdateDTO> updateDTO) {
        // 委托给子服务处理
        inspectionSchemeSamplingService.updateInspectionSchemeSamplings(updateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InspectionSchemeBasicSaveRespDTO saveInspectionSchemeFusion(InspectionSchemeSaveFusionDTO fusionDTO) {
        if (fusionDTO == null || fusionDTO.getBasic() == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "基础信息不能为空");
        }

        // 1. 先保存基础信息（新增或编辑），得到方案ID与版本ID
        InspectionSchemeBasicSaveRespDTO resp = saveInspectionSchemeBasic(fusionDTO.getBasic());

        // 2. 更新检验项目配置（可选）
        if (fusionDTO.getItemUpdates() != null && !fusionDTO.getItemUpdates().isEmpty()) {
            Long schemeId = resp.getSchemeId();
            Long versionId = resp.getVersionId();

            for (InspectionSchemeItemUpdateDTO item : fusionDTO.getItemUpdates()) {
                if (item.getSchemeId() == null) {
                    item.setSchemeId(schemeId);
                }
                if (item.getVersionId() == null) {
                    item.setVersionId(versionId);
                }
            }
            inspectionSchemeItemService.updateInspectionSchemeItems(fusionDTO.getItemUpdates());
        }

        // 3. 更新取样配置（可选）
        if (fusionDTO.getSamplingUpdates() != null && !fusionDTO.getSamplingUpdates().isEmpty()) {
            Long schemeId = resp.getSchemeId();
            Long versionId = resp.getVersionId();
            for (InspectionSchemeSamplingUpdateDTO sam : fusionDTO.getSamplingUpdates()) {
                if (sam.getSchemeId() == null) {
                    sam.setSchemeId(schemeId);
                }
                if (sam.getVersionId() == null) {
                    sam.setVersionId(versionId);
                }
            }
            inspectionSchemeSamplingService.updateInspectionSchemeSamplings(fusionDTO.getSamplingUpdates());
        }

        return resp;
    }

    @Override
    public CommonPage<InspectionSchemeDTO> pageInspectionScheme(InspectionSchemeQueryDTO queryDTO) {
        Boolean onlyActive = queryDTO.getOnlyActive() != null && queryDTO.getOnlyActive();
        // 原有逻辑（包含所有方案）
        LambdaQueryWrapper<InspectionScheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(queryDTO.getName() != null, InspectionScheme::getName, queryDTO.getName())
                .in(CollUtil.isNotEmpty(queryDTO.getMaterialIds()), InspectionScheme::getMaterialId, queryDTO.getMaterialIds())
                .isNotNull(onlyActive, InspectionScheme::getActiveVersionNo)
                .orderByDesc(InspectionScheme::getCreateTime);
        // 使用 PageHelper 进行分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<InspectionScheme> schemeList = inspectionSchemeMapper.selectList(wrapper);

        List<InspectionSchemeDTO> records = schemeList.stream().map(scheme -> {
            InspectionSchemeDTO dto = BeanUtil.copyProperties(scheme, InspectionSchemeDTO.class);
            dto.setActiveVersionNo(scheme.getActiveVersionNo());
            if (scheme.getMaterialId() != null) {
                Material material = materialMapper.selectById(scheme.getMaterialId());
                if (material != null) {
                    dto.setMaterialName(material.getName());
                    dto.setMaterialUnitId(material.getUnitId());
                    dto.setMaterialUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
                }
            }
            if (scheme.getPackageId() != null) {
                InspectPackage inspectPackage = inspectPackageMapper.selectById(scheme.getPackageId());
                if (inspectPackage != null) {
                    dto.setPackageName(inspectPackage.getName());
                }
            }
            return dto;
        }).collect(Collectors.toList());
        // 批量补齐生效版本ID，避免N+1查询
        List<Long> schemeIds = schemeList.stream().map(InspectionScheme::getId).collect(Collectors.toList());
        if (!schemeIds.isEmpty()) {
            LambdaQueryWrapper<InspectionSchemeVersion> avWrapper = new LambdaQueryWrapper<>();
            avWrapper.in(InspectionSchemeVersion::getSchemeId, schemeIds)
                    .eq(InspectionSchemeVersion::getStatus, InspectionSchemeVersionStatusEnum.ACTIVE.getValue());
            List<InspectionSchemeVersion> avList = inspectionSchemeVersionMapper.selectList(avWrapper);
            java.util.Map<Long, InspectionSchemeVersion> schemeIdToActiveVersion = avList.stream()
                    .collect(Collectors.toMap(InspectionSchemeVersion::getSchemeId, v -> v, (a, b) -> a));
            for (InspectionSchemeDTO dto : records) {
                InspectionSchemeVersion active = schemeIdToActiveVersion.get(dto.getId());
                if (active != null) {
                    dto.setActiveVersionId(active.getId());
                    if (dto.getActiveVersionNo() == null) {
                        dto.setActiveVersionNo(active.getVersionNo());
                        dto.setActiveVersionId(active.getId());
                    }
                }
            }
        }
        // 先使用原始列表生成分页元信息，再替换DTO列表，避免分页信息丢失
        CommonPage<InspectionScheme> page = CommonPage.convertPage(schemeList);
        return CommonPage.CommonPage(records, Long.valueOf(page.getTotal()), queryDTO);
    }

    @Override
    public InspectionSchemeDTO getInspectionScheme(Long id) {
        // 查询检验方案
        InspectionScheme scheme = inspectionSchemeMapper.selectById(id);
        if (scheme == null) {
            throw new RuntimeException("检验方案不存在");
        }

        // 转换为DTO
        InspectionSchemeDTO dto = BeanUtil.copyProperties(scheme, InspectionSchemeDTO.class);

        // 查询当前生效版本
        InspectionSchemeVersion activeVersion = inspectionSchemeVersionMapper.getActiveVersion(id);
        if (activeVersion != null) {
            dto.setActiveVersionNo(activeVersion.getVersionNo());
        }

        // 查询物料名称
        if (scheme.getMaterialId() != null) {
            Material material = materialMapper.selectById(scheme.getMaterialId());
            if (material != null) {
                dto.setMaterialName(material.getName());
            }
        }
        
        // 查询实验包名称
        if (scheme.getPackageId() != null) {
            InspectPackage inspectPackage = inspectPackageMapper.selectById(scheme.getPackageId());
            if (inspectPackage != null) {
                dto.setPackageName(inspectPackage.getName());
            }
        }

        return dto;
    }

    /**
     * 校验判定条件配置是否引用了已删除的数据点配置
     *
     * @param versionId 方案版本ID
     */
    @Override
    public void validateJudgmentConfigConsistency(Long versionId) {
        List<InspectionSchemeItemDTO> items = inspectionSchemeItemService.listInspectionSchemeItems(versionId);
        if (CollUtil.isEmpty(items)) {
            return;
        }

        for (InspectionSchemeItemDTO item : items) {
            if (CollUtil.isEmpty(item.getParameters())) {
                continue;
            }
            for (InspectionSchemeParameterDTO parameter : item.getParameters()) {
                Set<Long> aliveDpConfigIds = new HashSet<>();
                Map<Long, InspectionSchemeDataPointDTO> configIdToDp = new HashMap<>();
                if (CollUtil.isNotEmpty(parameter.getDataPoints())) {
                    parameter.getDataPoints().forEach(dp -> {
                        if (dp.getId() != null) {
                            aliveDpConfigIds.add(dp.getId());
                            configIdToDp.put(dp.getId(), dp);
                        }
                    });
                }

                if (CollUtil.isNotEmpty(parameter.getJudgments())) {
                    for (InspectionSchemeJudgmentDTO judgment : parameter.getJudgments()) {
                        InspectionSchemeDataPointDTO referencedDp = null;
                        if (judgment.getDataPointConfigId() != null && !aliveDpConfigIds.contains(judgment.getDataPointConfigId())) {
                            throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_DELETED);
                        }
                        if (judgment.getDataPointConfigId() != null) {
                            referencedDp = configIdToDp.get(judgment.getDataPointConfigId());
                        }
                        if (referencedDp != null) {
                            if (judgment.getPointType() != null && referencedDp.getPointType() != null
                                    && !judgment.getPointType().equals(referencedDp.getPointType())) {
                                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_TYPE_CHANGED, referencedDp.getName());
                            }
                            if (ExecuteMethodEnum.ELN.equals(parameter.getExecuteMethod())
                                    && (referencedDp.getComponentId() == null || referencedDp.getFieldId() == null)) {
                                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_BINDING_MISSING, referencedDp.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionScheme(Long id) {
        // 查询检验方案
        InspectionScheme scheme = inspectionSchemeMapper.selectById(id);
        if (scheme == null) {
            throw new RuntimeException("检验方案不存在");
        }

        // 查询是否存在生效版本
        InspectionSchemeVersion activeVersion = inspectionSchemeVersionMapper.getActiveVersion(id);
        if (activeVersion != null) {
            throw new RuntimeException("检验方案存在生效版本，不能删除");
        }

        // 删除检验方案
        inspectionSchemeMapper.deleteById(id);

        // 删除所有版本
        LambdaQueryWrapper<InspectionSchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeVersion::getSchemeId, id);
        inspectionSchemeVersionMapper.delete(wrapper);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVersionItems(InspectionSchemeVersionSaveItemsDTO dto) {
        // 校验版本存在且处于编辑状态
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(dto.getVersionId());
        if (version == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }
        if (version.getStatus() == InspectionSchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
            inspectionSchemeVersionMapper.updateById(version);
        } else if (version.getStatus() != InspectionSchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_STATE_ERROR);
        }

        inspectionSchemeItemService.mergeVersionItems(dto.getSchemeId(), dto.getVersionId(), dto.getParameters());
    }

    /**
     * 检查名称是否重复
     *
     * @param name 名称
     * @param excludeId 排除的ID
     */
    private void checkNameDuplicate(String name, Long excludeId) {
        Integer count = inspectionSchemeMapper.checkNameDuplicate(name, excludeId);
        if (count > 0) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_NAME_EXISTS);
        }
    }

    /**
     * 保存方案的配置信息（检验项目+取样）- 不再保存明细记录
     *
     * @param scheme
     * @param versionId 版本ID
     * @param saveDTO 保存参数
     */
    private void saveSchemeDetails(InspectionScheme scheme, Long versionId, InspectionSchemeSaveDTO saveDTO) {
        // 1. 保存检验项目配置（直接关联版本）
        if (saveDTO.getInspectionItems() != null) {
            List<InspectionSchemeItemSaveDTO> inspectionItemSaveDTOs = new ArrayList<>();
            for (InspectionSchemeSaveDTO.InspectionItemConfigDTO itemConfig : saveDTO.getInspectionItems()) {
                InspectionSchemeItemSaveDTO itemSaveDTO = new InspectionSchemeItemSaveDTO();
                itemSaveDTO.setInspectItemId(itemConfig.getInspectItemId());
                itemSaveDTO.setIsRequired(itemConfig.getIsRequired());
                itemSaveDTO.setSort(itemConfig.getSort());
                itemSaveDTO.setDuration(itemConfig.getDuration());
                itemSaveDTO.setTimeUnit(itemConfig.getTimeUnit());
                itemSaveDTO.setTeams(itemConfig.getTeams());
                // 转换分析项配置
                if (itemConfig.getInspectionParameters() != null) {
                    List<InspectionSchemeParameterSaveDTO> parameterSaveDTOs = new ArrayList<>();
                    for (InspectionSchemeSaveDTO.InspectParameterConfigDTO parameterConfigDTO : itemConfig.getInspectionParameters()) {
                        InspectionSchemeParameterSaveDTO parameterSaveDTO = new InspectionSchemeParameterSaveDTO();
                        parameterSaveDTO.setParameterId(parameterConfigDTO.getParameterId());
                        parameterSaveDTO.setStandardRule(parameterConfigDTO.getStandardRule());
                        parameterSaveDTO.setIsReportable(parameterConfigDTO.getIsReportable());
                        parameterSaveDTO.setRecordId(parameterConfigDTO.getRecordId());
                        parameterSaveDTO.setIsExecutable(parameterConfigDTO.getIsExecutable());
                        parameterSaveDTO.setFinalExpression(parameterConfigDTO.getFinalExpression());
                        parameterSaveDTO.setExecuteMethod(parameterConfigDTO.getExecuteMethod());
                        parameterSaveDTO.setInspectItemId(itemSaveDTO.getInspectItemId());
                        // 转换数据点配置
                        if (parameterConfigDTO.getDataPoints() != null) {
                            List<InspectionSchemeDataPointSaveDTO> dataPointSaveDTOs = new ArrayList<>();
                            for (InspectionSchemeSaveDTO.DataPointConfigDTO pointConfig : parameterConfigDTO.getDataPoints()) {
                                InspectionSchemeDataPointSaveDTO pointSaveDTO = new InspectionSchemeDataPointSaveDTO();
                                pointSaveDTO.setDataPointId(pointConfig.getDataPointId());
                                pointSaveDTO.setName(pointConfig.getName());
                                pointSaveDTO.setPointType(pointConfig.getPointType());
                                pointSaveDTO.setTrendLineConfig(pointConfig.getTrendLineConfig());
                                pointSaveDTO.setOptions(pointConfig.getOptions());
                                pointSaveDTO.setTimeFormat(pointConfig.getTimeFormat());
                                pointSaveDTO.setDateStyle(pointConfig.getDateStyle());
                                pointSaveDTO.setReportDisplay(pointConfig.getReportDisplay());
                                pointSaveDTO.setInspectParameterId(parameterSaveDTO.getParameterId());
                                pointSaveDTO.setInspectItemId(itemSaveDTO.getInspectItemId());
                                dataPointSaveDTOs.add(pointSaveDTO);
                            }
                            parameterSaveDTO.setDataPoints(dataPointSaveDTOs);
                        }
                        if (parameterConfigDTO.getJudgments() != null){
                            List<InspectionSchemeJudgmentSaveDTO> judgmentSaveDTOs = new ArrayList<>();
                            for (InspectionSchemeSaveDTO.JudgmentConfigDTO judgmentConfig : parameterConfigDTO.getJudgments()) {
                                InspectionSchemeJudgmentSaveDTO judgmentSaveDTO = new InspectionSchemeJudgmentSaveDTO();
                                judgmentSaveDTO.setJudgmentType(judgmentConfig.getJudgmentType());
                                judgmentSaveDTO.setDefaultResult(judgmentConfig.getDefaultResult());
                                judgmentSaveDTO.setMinValue(judgmentConfig.getMinValue());
                                judgmentSaveDTO.setMaxValue(judgmentConfig.getMaxValue());
                                judgmentSaveDTO.setStandardValue(judgmentConfig.getStandardValue());
                                judgmentSaveDTO.setExpression(judgmentConfig.getExpression());
                                judgmentSaveDTO.setInspectParameterId(parameterSaveDTO.getParameterId());
                                judgmentSaveDTO.setInspectItemId(itemSaveDTO.getInspectItemId());
                                judgmentSaveDTO.setDataPointId(judgmentConfig.getDataPointId());
                                judgmentSaveDTOs.add(judgmentSaveDTO);
                            }
                            parameterSaveDTO.setJudgments(judgmentSaveDTOs);
                        }
                        parameterSaveDTOs.add(parameterSaveDTO);
                    }
                    itemSaveDTO.setParameters(parameterSaveDTOs);
                }
                inspectionItemSaveDTOs.add(itemSaveDTO);
            }
            // 直接关联版本保存检验项目配置
            // 直接关联方案和版本保存检验项目配置
            inspectionSchemeItemService.saveInspectionSchemeItems(scheme.getId(), versionId,saveDTO.getPackageInfo().getPackageId(), inspectionItemSaveDTOs);
        }
        
        // 2. 保存取样配置（直接关联版本）
        if (saveDTO.getSamplingConfigs() != null && !saveDTO.getSamplingConfigs().isEmpty()) {
            List<InspectionSchemeSamplingSaveDTO> samplingSaveDTOs = new ArrayList<>();
            for (InspectionSchemeSaveDTO.SamplingConfigDTO samplingConfig : saveDTO.getSamplingConfigs()) {
                InspectionSchemeSamplingSaveDTO samplingSaveDTO = new InspectionSchemeSamplingSaveDTO();
                samplingSaveDTO.setSchemeId(scheme.getId()); // 设置方案ID
                samplingSaveDTO.setVersionId(versionId); // 设置版本ID
                samplingSaveDTO.setInspectItemId(samplingConfig.getInspectItemId());
                samplingSaveDTO.setSamplingAmount(samplingConfig.getSamplingAmount());
                samplingSaveDTO.setSamplingUnit(samplingConfig.getSamplingUnit());
                samplingSaveDTO.setSamplingCount(samplingConfig.getSamplingCount());
                samplingSaveDTOs.add(samplingSaveDTO);
            }
            // 保存取样配置（直接关联方案和版本）
            inspectionSchemeSamplingService.saveInspectionSchemeSamplings(scheme.getId(), versionId, samplingSaveDTOs);
        }
    }

    @Override
    public List<InspectionSchemeDropdownDTO> getInspectionSchemeDropdownByMaterialId(Long materialId) {
        // 1. 根据检品ID查询检验方案
        LambdaQueryWrapper<InspectionScheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionScheme::getMaterialId, materialId)
               .eq(InspectionScheme::getDeleted, false)
               .orderByDesc(InspectionScheme::getCreateTime);
        
        List<InspectionScheme> schemes = inspectionSchemeMapper.selectList(wrapper);
        
        if (CollUtil.isEmpty(schemes)) {
            return new ArrayList<>();
        }
        
        // 2. 转换为下拉数据DTO，只返回有生效版本的方案
        List<InspectionSchemeDropdownDTO> dropdownList = new ArrayList<>();
        for (InspectionScheme scheme : schemes) {
            // 查询当前生效版本
            InspectionSchemeVersion activeVersion = inspectionSchemeVersionMapper.getActiveVersion(scheme.getId());
            
            // 只处理有生效版本的方案
            if (activeVersion == null) {
                continue;
            }
            
            InspectionSchemeDropdownDTO dto = new InspectionSchemeDropdownDTO();
            dto.setId(scheme.getId());
            dto.setName(scheme.getName());
            dto.setMaterialId(scheme.getMaterialId());
            dto.setMaterialCode(scheme.getMaterialCode());
            dto.setPackageId(scheme.getPackageId());
            dto.setPackageCode(scheme.getPackageCode());
            
            // 设置生效版本信息
            dto.setActiveVersionNo(activeVersion.getVersionNo());
            dto.setActiveVersionId(activeVersion.getId());
            dto.setDisplayName(scheme.getName() + " - " + activeVersion.getVersionNo());
            
            // 查询物料名称
            if (scheme.getMaterialId() != null) {
                Material material = materialMapper.selectById(scheme.getMaterialId());
                if (material != null) {
                    dto.setMaterialName(material.getName());
                }
            }
            
            // 查询实验包名称
            if (scheme.getPackageId() != null) {
                InspectPackage inspectPackage = inspectPackageMapper.selectById(scheme.getPackageId());
                if (inspectPackage != null) {
                    dto.setPackageName(inspectPackage.getName());
                }
            }
            
            dropdownList.add(dto);
        }
        
        return dropdownList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InspectionSchemeBasicSaveRespDTO copySchemeFromVersion(InspectionSchemeCopyFromVersionDTO dto) {
        // 1. 校验源版本
        InspectionSchemeVersion sourceVersion = inspectionSchemeVersionMapper.selectById(dto.getSourceVersionId());
        if (sourceVersion == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_NOT_EXIST);
        }

        // 2. 校验新方案名称唯一
        checkNameDuplicate(dto.getNewSchemeName(), null);

        InspectionSchemeCopyFromVersionDTO.MaterialInfoDTO material = dto.getMaterial();

        // 3. 先创建新方案
        InspectionScheme scheme = new InspectionScheme();
        scheme.setName(dto.getNewSchemeName());
        scheme.setMaterialId(material.getMaterialId());
        scheme.setMaterialCode(material.getMaterialCode());
        scheme.setPackageId(sourceVersion.getPackageId());
        scheme.setPackageCode(sourceVersion.getPackageCode());
        inspectionSchemeMapper.insert(scheme);

        // 4. 校验新版本号在新方案中唯一
        Integer dup = inspectionSchemeVersionMapper.checkVersionNoDuplicate(scheme.getId(), dto.getNewVersionNo(), null);
        if (dup != null && dup > 0) {
            throw new RuntimeException("版本号在该方案中已存在");
        }

        // 5. 创建新版本，父版本指向源版本
        InspectionSchemeVersion newVersion = new InspectionSchemeVersion();
        newVersion.setSchemeId(scheme.getId());
        newVersion.setVersionNo(dto.getNewVersionNo());
        newVersion.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
        newVersion.setParentVersionId(sourceVersion.getId());
        newVersion.setDescription(dto.getDescription());
        newVersion.setMaterialId(material.getMaterialId());
        newVersion.setMaterialCode(material.getMaterialCode());
        newVersion.setPackageId(sourceVersion.getPackageId());
        newVersion.setPackageCode(sourceVersion.getPackageCode());
        inspectionSchemeVersionMapper.insert(newVersion);

        // 6. 复制项目配置
        List<InspectionSchemeItemDTO> inspectionItems = inspectionSchemeItemService.listInspectionSchemeItems(sourceVersion.getId());
        if (inspectionItems != null && !inspectionItems.isEmpty()) {
            List<InspectionSchemeItemSaveDTO> itemSaves = new ArrayList<>();
            for (InspectionSchemeItemDTO item : inspectionItems) {
                InspectionSchemeItemSaveDTO itemSave = new InspectionSchemeItemSaveDTO();
                itemSave.setInspectItemId(item.getInspectItemId());
                itemSave.setIsRequired(item.getIsRequired());
                itemSave.setSort(item.getSort());
                itemSave.setDuration(item.getDuration());
                itemSave.setTimeUnit(item.getTimeUnit());
                itemSave.setRemark(item.getRemark());
                itemSave.setTeams(item.getTeams());

                if (item.getParameters() != null && !item.getParameters().isEmpty()) {
                    List<InspectionSchemeParameterSaveDTO> params = new ArrayList<>();
                    for (InspectionSchemeParameterDTO p : item.getParameters()) {
                        InspectionSchemeParameterSaveDTO ps = new InspectionSchemeParameterSaveDTO();
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
                        // 关联检验项目ID，防止lm_inspection_scheme_parameter.inspect_item_id为空
                        ps.setInspectItemId(item.getInspectItemId());

                        HashMap<Long, Long> dataPointConfigIdMap = new HashMap<>();

                        if (p.getDataPoints() != null && !p.getDataPoints().isEmpty()) {
                            List<InspectionSchemeDataPointSaveDTO> dps = new ArrayList<>();
                            for (InspectionSchemeDataPointDTO dp : p.getDataPoints()) {
                                InspectionSchemeDataPointSaveDTO d = new InspectionSchemeDataPointSaveDTO();
                                d.setDataPointId(dp.getDataPointId());
                                d.setName(dp.getName());
                                d.setPointType(dp.getPointType());
                                d.setTrendLineConfig(dp.getTrendLineConfig());
                                d.setOptions(dp.getOptions());
                                d.setTimeFormat(dp.getTimeFormat());
                                d.setDateStyle(dp.getDateStyle());
                                d.setReportDisplay(dp.getReportDisplay());
                                d.setDateStyle(dp.getDateStyle());
                                d.setTimeFormat(dp.getTimeFormat());
                                // 补齐新增字段复制
                                d.setRoundingUp(dp.getRoundingUp());
                                // 透传 ELN 绑定字段
                                d.setRecordId(dp.getRecordId());
                                d.setRecordVersionId(dp.getRecordVersionId());
                                d.setComponentId(dp.getComponentId());
                                d.setRecordItemId(dp.getRecordItemId());
                                d.setFieldId(dp.getFieldId());
                                // ensure relational fields are set
                                d.setInspectParameterId(p.getParameterId());
                                d.setInspectItemId(item.getInspectItemId());
                                d.setId(IdUtils.getSnowflake());
                                dps.add(d);
                                dataPointConfigIdMap.put(dp.getId(), d.getId());
                            }
                            ps.setDataPoints(dps);
                        }

                        if (p.getJudgments() != null && !p.getJudgments().isEmpty()) {
                            List<InspectionSchemeJudgmentSaveDTO> jds = new ArrayList<>();
                            for (InspectionSchemeJudgmentDTO j : p.getJudgments()) {
                                InspectionSchemeJudgmentSaveDTO jd = new InspectionSchemeJudgmentSaveDTO();
                                jd.setJudgmentType(j.getJudgmentType());
                                jd.setDefaultResult(j.getDefaultResult());
                                jd.setMinValue(j.getMinValue());
                                jd.setMaxValue(j.getMaxValue());
                                jd.setMinOperator(j.getMinOperator());
                                jd.setMaxOperator(j.getMaxOperator());
                                jd.setStandardValue(j.getStandardValue());
                                jd.setExpression(j.getExpression());
                                jd.setDataPointId(j.getDataPointId());
                                jd.setPointType(j.getPointType());
                                // 补齐时间比较字段复制
                                jd.setMinTime(j.getMinTime());
                                jd.setMaxTime(j.getMaxTime());
                                // 数据点是新增的，datapointConfigId需要记录新增的数据点
                                jd.setDataPointConfigId(dataPointConfigIdMap.get(j.getDataPointConfigId()));
                                jd.setJudgementConfigName(j.getJudgementConfigName());
                                // ensure relational fields are set
                                jd.setInspectParameterId(p.getParameterId());
                                jd.setInspectItemId(item.getInspectItemId());
                                jds.add(jd);
                            }
                            ps.setJudgments(jds);
                        }
                        params.add(ps);
                    }
                    itemSave.setParameters(params);
                }
                itemSaves.add(itemSave);
            }
            inspectionSchemeItemService.saveInspectionSchemeItems(scheme.getId(), newVersion.getId(), sourceVersion.getPackageId(), itemSaves);

            // 复制组件配置
            List<InspectionSchemeParameter> newParams = inspectionSchemeParameterMapper.selectByVersionId(newVersion.getId());
            Map<String, Long> newParamIdMap = newParams.stream().collect(
                    Collectors.toMap(
                            np -> np.getInspectItemId() + "_" + np.getParameterId(),
                            InspectionSchemeParameter::getId,
                            (a, b) -> a));
            for (InspectionSchemeItemDTO item : inspectionItems) {
                if (item.getParameters() == null) continue;
                for (InspectionSchemeParameterDTO p : item.getParameters()) {
                    Long newParamConfigId = newParamIdMap.get(item.getInspectItemId() + "_" + p.getParameterId());
                    if (newParamConfigId == null) continue;
                    List<SchemeParameterComponentConfig> srcConfigs = schemeParameterComponentConfigMapper.selectByParameterConfigId(p.getId());
                    if (srcConfigs.isEmpty()) continue;
                    List<SchemeParameterComponentConfig> newConfigs = new ArrayList<>();
                    for (SchemeParameterComponentConfig sc : srcConfigs) {
                        SchemeParameterComponentConfig nc = new SchemeParameterComponentConfig();
                        nc.setParameterId(sc.getParameterId());
                        nc.setParameterConfigId(newParamConfigId);
                        nc.setSchemeId(scheme.getId());
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

        // 复制取样配置
        List<InspectionSchemeSamplingDTO> samplings = inspectionSchemeSamplingService.listInspectionSchemeSamplings(sourceVersion.getSchemeId(), sourceVersion.getId());
        if (samplings != null && !samplings.isEmpty()) {
            List<InspectionSchemeSamplingSaveDTO> saves = new ArrayList<>();
            for (InspectionSchemeSamplingDTO s : samplings) {
                InspectionSchemeSamplingSaveDTO d = new InspectionSchemeSamplingSaveDTO();
                d.setSchemeId(scheme.getId());
                d.setVersionId(newVersion.getId());
                d.setInspectItemId(s.getInspectItemId());
                d.setSamplingAmount(s.getSamplingAmount());
                d.setSamplingUnit(s.getSamplingUnit());
                d.setSamplingCount(s.getSamplingCount());
                saves.add(d);
            }
            inspectionSchemeSamplingService.saveInspectionSchemeSamplings(scheme.getId(), newVersion.getId(), saves);
        }

        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.INSPECT_SCHEME.name())
                .businessId(newVersion.getId())
                .operationType(OperationType.SAVE.getValue())
                .createBy(SysUserHolder.getUser().getUserId())
                .build());

        InspectionSchemeBasicSaveRespDTO resp = new InspectionSchemeBasicSaveRespDTO();
        resp.setSchemeId(scheme.getId());
        resp.setVersionId(newVersion.getId());
        resp.setName(scheme.getName());
        resp.setVersionNo(newVersion.getVersionNo());
        resp.setDescription(newVersion.getDescription());
        return resp;
    }

} 