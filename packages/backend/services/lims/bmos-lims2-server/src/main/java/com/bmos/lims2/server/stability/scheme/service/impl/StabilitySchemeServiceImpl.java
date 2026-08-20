package com.bmos.lims2.server.stability.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.enums.PermissionModuleEnum;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.lims2.server.permission.mapper.ResourcePermissionMapper;
import com.bmos.lims2.server.permission.model.ResourcePermission;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDataPointDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilityCopyItemsResultDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeCopyFromVersionDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeItemSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveFusionDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeBasicSaveRespDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilityScheme;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeItemService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemePlanService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 稳定性方案Service实现类
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Service
@Slf4j
public class StabilitySchemeServiceImpl implements StabilitySchemeService {

    @Autowired
    private StabilitySchemeMapper stabilitySchemeMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private ResourcePermissionMapper resourcePermissionMapper;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private StabilitySchemeItemService stabilitySchemeItemService;

    @Autowired
    private StabilitySchemePlanService stabilitySchemePlanService;

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Override
    public CommonPage<StabilitySchemeDTO> pageStabilityScheme(StabilitySchemeQueryDTO queryDTO) {
        // 数据权限：取当前登录人部门及父部门集合 -> 查出有权限的方案ID集合
        List<Long> myDeptIds = platformApiAdaptor.deptIds();
        List<Long> permittedSchemeIds;
        if (!myDeptIds.isEmpty()) {
            List<ResourcePermission> rp = resourcePermissionMapper.selectByDeptIdsAndModule(
                    myDeptIds, PermissionModuleEnum.STABILITY_SCHEME.getValue());
            if (rp != null && !rp.isEmpty()) {
                permittedSchemeIds = rp.stream().map(ResourcePermission::getResourceId).distinct().collect(Collectors.toList());
            } else {
                return CommonPage.CommonPage(new ArrayList<>(), 0L, queryDTO);
            }
        } else {
            return CommonPage.CommonPage(new ArrayList<>(), 0L, queryDTO);
        }

        // 构建查询条件
        LambdaQueryWrapper<StabilityScheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getName()), StabilityScheme::getName, queryDTO.getName())
                .like(StrUtil.isNotBlank(queryDTO.getCode()), StabilityScheme::getCode, queryDTO.getCode())
                .eq(queryDTO.getMaterialId() != null, StabilityScheme::getMaterialId, queryDTO.getMaterialId())
                .in(CollUtil.isNotEmpty(queryDTO.getMaterialIds()), StabilityScheme::getMaterialId, CollUtil.emptyIfNull(queryDTO.getMaterialIds()))
                .in(StabilityScheme::getId, permittedSchemeIds)
                .orderByDesc(StabilityScheme::getCreateTime);

        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<StabilityScheme> schemeList = stabilitySchemeMapper.selectList(wrapper);
        CommonPage<StabilityScheme> entityPage = CommonPage.convertPage(schemeList);
        List<StabilitySchemeDTO> dtoList = BeanUtil.copyToList(schemeList, StabilitySchemeDTO.class);
        dtoList.forEach(dto -> dto.setDeptIds(getPermissions(dto.getId())));

        CommonPage<StabilitySchemeDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(entityPage.getPageNum());
        resultPage.setPageSize(entityPage.getPageSize());
        resultPage.setTotal(entityPage.getTotal());
        resultPage.setList(dtoList);
        return resultPage;
    }

    @Override
    public StabilitySchemeDTO getStabilityScheme(Long id) {
        StabilityScheme scheme = stabilitySchemeMapper.selectById(id);
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NOT_EXIST);
        }
        StabilitySchemeDTO dto = BeanUtil.copyProperties(scheme, StabilitySchemeDTO.class);
        dto.setDeptIds(getPermissions(id));
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StabilitySchemeBasicSaveRespDTO saveStabilityScheme(StabilitySchemeSaveDTO saveDTO) {
        if (saveDTO.getId() != null && saveDTO.getVersionId() != null) {
            // 编辑逻辑：加载并校验版本与方案
            StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(saveDTO.getVersionId());
            if (version == null) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
            }
            if (!version.getSchemeId().equals(saveDTO.getId())) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
            }

            StabilityScheme scheme = stabilitySchemeMapper.selectById(saveDTO.getId());
            if (scheme == null) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NOT_EXIST);
            }

            revertToEditingIfCompleted(saveDTO.getVersionId());
            // 仅允许编辑"编辑中"的版本
            if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
            }

            // 更新方案基础信息（仅更新非空字段，支持暂存场景）
            if (saveDTO.getName() != null) {
                scheme.setName(saveDTO.getName());
            }
            if (saveDTO.getCode() != null) {
                scheme.setCode(saveDTO.getCode());
            }
            if (saveDTO.getMaterial() != null) {
                scheme.setMaterialId(saveDTO.getMaterial().getMaterialId());
                scheme.setMaterialName(saveDTO.getMaterial().getMaterialName());
                scheme.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            scheme.setUpdateBy(SysUserHolder.getUser().getUserId());
            stabilitySchemeMapper.updateById(scheme);

            // 更新版本基础信息（仅更新非空字段）
            if (saveDTO.getVersionNo() != null) {
                version.setVersionNo(saveDTO.getVersionNo());
            }
            if (saveDTO.getDescription() != null) {
                version.setDescription(saveDTO.getDescription());
            }
            if (saveDTO.getMaterial() != null) {
                version.setMaterialId(saveDTO.getMaterial().getMaterialId());
                version.setMaterialName(saveDTO.getMaterial().getMaterialName());
                version.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            version.setUpdateBy(SysUserHolder.getUser().getUserId());
            stabilitySchemeVersionMapper.updateById(version);

            // 保存数据权限
            savePermissions(scheme.getId(), saveDTO.getDeptIds());

            log.info("更新稳定性方案成功：schemeId={}, versionId={}", scheme.getId(), version.getId());
            saveHistoryLog(SysUserHolder.getUser().getUserId(), version.getId(), OperationType.REDACT);

            StabilitySchemeBasicSaveRespDTO respDTO = new StabilitySchemeBasicSaveRespDTO();
            respDTO.setSchemeId(scheme.getId());
            respDTO.setVersionId(version.getId());
            respDTO.setSchemeName(scheme.getName());
            respDTO.setSchemeCode(scheme.getCode());
            respDTO.setVersionNo(version.getVersionNo());
            respDTO.setIsNew(false);
            return respDTO;
        } else {
            // 新增逻辑
            // 检查方案编码是否重复
            if (saveDTO.getCode() != null) {
                StabilityScheme existScheme = stabilitySchemeMapper.selectByCode(saveDTO.getCode());
                if (existScheme != null) {
                    throw new BmosException(LimsResponseCode.STABILITY_SCHEME_CODE_EXIST);
                }
            }

            // 创建方案
            StabilityScheme scheme = new StabilityScheme();
            scheme.setName(saveDTO.getName());
            scheme.setCode(saveDTO.getCode());
            if (saveDTO.getMaterial() != null) {
                scheme.setMaterialId(saveDTO.getMaterial().getMaterialId());
                scheme.setMaterialName(saveDTO.getMaterial().getMaterialName());
                scheme.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            scheme.setCreateBy(SysUserHolder.getUser().getUserId());
            stabilitySchemeMapper.insert(scheme);

            // 创建初始版本
            StabilitySchemeVersion version = new StabilitySchemeVersion();
            version.setSchemeId(scheme.getId());
            version.setVersionNo(saveDTO.getVersionNo());
            version.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
            version.setDescription(saveDTO.getDescription());
            if (saveDTO.getMaterial() != null) {
                version.setMaterialId(saveDTO.getMaterial().getMaterialId());
                version.setMaterialName(saveDTO.getMaterial().getMaterialName());
                version.setMaterialCode(saveDTO.getMaterial().getMaterialCode());
            }
            version.setParentVersionId(saveDTO.getParentVersionId());
            version.setCreateBy(SysUserHolder.getUser().getUserId());
            stabilitySchemeVersionMapper.insert(version);

            // 保存数据权限
            savePermissions(scheme.getId(), saveDTO.getDeptIds());

            log.info("新增稳定性方案成功：schemeId={}, versionId={}", scheme.getId(), version.getId());
            saveHistoryLog(SysUserHolder.getUser().getUserId(), version.getId(), OperationType.SAVE);

            StabilitySchemeBasicSaveRespDTO respDTO = new StabilitySchemeBasicSaveRespDTO();
            respDTO.setSchemeId(scheme.getId());
            respDTO.setVersionId(version.getId());
            respDTO.setSchemeName(scheme.getName());
            respDTO.setSchemeCode(scheme.getCode());
            respDTO.setVersionNo(version.getVersionNo());
            respDTO.setIsNew(true);
            return respDTO;
        }
    }

    /**
     * 确保版本可编辑：EDITING 直接通过；COMPLETED 自动回退为 EDITING；其他状态抛异常
     */
    private void revertToEditingIfCompleted(Long versionId) {
        if (versionId == null) return;
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(versionId);
        if (version == null) return;
        if (version.getStatus() == StabilitySchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
            stabilitySchemeVersionMapper.updateById(version);
        } else if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStabilityScheme(Long id) {
        StabilityScheme scheme = stabilitySchemeMapper.selectById(id);
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NOT_EXIST);
        }

        // 检查是否有生效中的版本
        int activeCount = stabilitySchemeVersionMapper.countBySchemeIdAndStatus(
                id, StabilitySchemeVersionStatusEnum.ACTIVE);
        if (activeCount > 0) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_HAS_ACTIVE_VERSION);
        }

        // 逻辑删除方案
        scheme.setDeleted(true);
        scheme.setUpdateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeMapper.updateById(scheme);

        // 逻辑删除所有版本
        List<StabilitySchemeVersion> versions = stabilitySchemeVersionMapper.selectBySchemeId(id);
        for (StabilitySchemeVersion version : versions) {
            version.setDeleted(true);
            version.setUpdateBy(SysUserHolder.getUser().getUserId());
            stabilitySchemeVersionMapper.updateById(version);
        }

        // 删除数据权限
        resourcePermissionService.deleteByResourceId(id);

        log.info("删除稳定性方案成功：schemeId={}", id);
    }

    @Override
    public StabilitySchemeDTO getByMaterialId(Long materialId) {
        StabilityScheme scheme = stabilitySchemeMapper.selectByMaterialId(materialId);
        if (scheme == null) {
            return null;
        }
        return BeanUtil.copyProperties(scheme, StabilitySchemeDTO.class);
    }

    @Override
    public List<StabilitySchemeDTO> listAll() {
        LambdaQueryWrapper<StabilityScheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilityScheme::getDeleted, false)
                .orderByDesc(StabilityScheme::getCreateTime);
        List<StabilityScheme> schemeList = stabilitySchemeMapper.selectList(wrapper);
        return BeanUtil.copyToList(schemeList, StabilitySchemeDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StabilitySchemeBasicSaveRespDTO saveStabilitySchemeFusion(StabilitySchemeSaveFusionDTO fusionDTO) {
        // 1. 保存基础信息
        StabilitySchemeBasicSaveRespDTO resp = saveStabilityScheme(fusionDTO.getBasic());

        // 2. 保存分析项（可选）
        if (fusionDTO.getItemUpdates() != null && !fusionDTO.getItemUpdates().isEmpty()) {
            StabilitySchemeItemSaveDTO itemSaveDTO = new StabilitySchemeItemSaveDTO();
            itemSaveDTO.setVersionId(resp.getVersionId());
            itemSaveDTO.setItems(fusionDTO.getItemUpdates());
            stabilitySchemeItemService.saveItems(itemSaveDTO);
        }

        // 3. 保存检验计划（可选）
        if (fusionDTO.getPlanSave() != null && fusionDTO.getPlanSave().getPlans() != null) {
            fusionDTO.getPlanSave().setVersionId(resp.getVersionId());
            stabilitySchemePlanService.savePlans(fusionDTO.getPlanSave());
        }

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePermissions(Long schemeId, List<Long> deptIds) {
        ResourcePermissionSaveDTO saveDTO = ResourcePermissionSaveDTO.builder()
                .resourceId(schemeId)
                .deptIds(CollUtil.emptyIfNull(deptIds))
                .module(PermissionModuleEnum.STABILITY_SCHEME.getValue())
                .build();
        resourcePermissionService.save(saveDTO);
    }

    @Override
    public List<Long> getPermissions(Long schemeId) {
        return resourcePermissionService.getDeptListByResourceId(schemeId);
    }

    private void saveHistoryLog(String userId, Long versionId, OperationType operationType) {
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.STABILITY_SCHEME.name())
                .businessId(versionId)
                .operationType(operationType.getValue())
                .createBy(userId)
                .build());
    }

    /**
     * 校验判定条件配置是否引用了已删除的数据点配置
     *
     * @param versionId 方案版本ID
     */
    @Override
    public void validateJudgmentConfigConsistency(Long versionId) {
        List<StabilitySchemeItemDTO> items = stabilitySchemeItemService.listItems(versionId);
        if (CollUtil.isEmpty(items)) {
            return;
        }

        for (StabilitySchemeItemDTO item : items) {
            if (CollUtil.isEmpty(item.getInspectionParameters())) {
                continue;
            }
            for (StabilitySchemeParameterDTO parameter : item.getInspectionParameters()) {
                Set<Long> aliveDpConfigIds = new HashSet<>();
                Map<Long, StabilitySchemeDataPointDTO> configIdToDp = new HashMap<>();
                if (CollUtil.isNotEmpty(parameter.getDataPoints())) {
                    parameter.getDataPoints().forEach(dp -> {
                        if (dp.getId() != null) {
                            aliveDpConfigIds.add(dp.getId());
                            configIdToDp.put(dp.getId(), dp);
                        }
                    });
                }

                if (CollUtil.isNotEmpty(parameter.getJudgments())) {
                    for (StabilitySchemeJudgmentDTO judgment : parameter.getJudgments()) {
                        if (judgment.getDataPointConfigId() != null
                                && !aliveDpConfigIds.contains(judgment.getDataPointConfigId())) {
                            throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_DELETED);
                        }
                        StabilitySchemeDataPointDTO referencedDp = null;
                        if (judgment.getDataPointConfigId() != null) {
                            referencedDp = configIdToDp.get(judgment.getDataPointConfigId());
                        }
                        if (referencedDp != null) {
                            if (judgment.getPointType() != null && referencedDp.getPointType() != null
                                    && !judgment.getPointType().equals(referencedDp.getPointType())) {
                                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_TYPE_CHANGED,
                                        referencedDp.getName());
                            }
                            if (ExecuteMethodEnum.ELN.equals(parameter.getExecuteMethod())
                                    && (referencedDp.getComponentId() == null || referencedDp.getFieldId() == null)) {
                                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_BINDING_MISSING,
                                        referencedDp.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StabilitySchemeBasicSaveRespDTO copySchemeFromVersion(StabilitySchemeCopyFromVersionDTO dto) {
        // 1. 校验源版本存在
        StabilitySchemeVersion sourceVersion = stabilitySchemeVersionMapper.selectById(dto.getSourceVersionId());
        if (sourceVersion == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        // 2. 校验方案编码唯一，创建新方案
        StabilityScheme existScheme = stabilitySchemeMapper.selectByCode(dto.getNewSchemeCode());
        if (existScheme != null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_CODE_EXIST);
        }
        // 校验方案名称唯一
        Long nameCount = stabilitySchemeMapper.selectCount(new LambdaQueryWrapper<StabilityScheme>()
                .eq(StabilityScheme::getName, dto.getNewSchemeName())
                .eq(StabilityScheme::getDeleted, false));
        if (nameCount > 0) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NAME_EXISTS);
        }
        StabilityScheme newScheme = new StabilityScheme();
        newScheme.setName(dto.getNewSchemeName());
        newScheme.setCode(dto.getNewSchemeCode());
        if (dto.getMaterial() != null) {
            newScheme.setMaterialId(dto.getMaterial().getMaterialId());
            newScheme.setMaterialName(dto.getMaterial().getMaterialName());
            newScheme.setMaterialCode(dto.getMaterial().getMaterialCode());
        }
        newScheme.setCreateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeMapper.insert(newScheme);
        savePermissions(newScheme.getId(), dto.getDeptIds());

        // 3. 创建新版本
        StabilitySchemeVersion newVersion = new StabilitySchemeVersion();
        newVersion.setSchemeId(newScheme.getId());
        newVersion.setVersionNo(dto.getNewVersionNo());
        newVersion.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
        newVersion.setDescription(dto.getDescription());
        newVersion.setParentVersionId(dto.getSourceVersionId());
        if (dto.getMaterial() != null) {
            newVersion.setMaterialId(dto.getMaterial().getMaterialId());
            newVersion.setMaterialName(dto.getMaterial().getMaterialName());
            newVersion.setMaterialCode(dto.getMaterial().getMaterialCode());
        }
        newVersion.setCreateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeVersionMapper.insert(newVersion);

        // 4. 复制检验项目配置，获取ID映射后再复制检验计划（修正计划中的配置ID引用）
        StabilityCopyItemsResultDTO copyItemsResult =
                stabilitySchemeItemService.copyItems(dto.getSourceVersionId(), newVersion.getId(), newScheme.getId());
        stabilitySchemePlanService.copyPlans(dto.getSourceVersionId(), newVersion.getId(), newScheme.getId(), copyItemsResult);

        log.info("从方案版本复制新建稳定性方案成功：sourceVersionId={}, newSchemeId={}, newVersionId={}",
                dto.getSourceVersionId(), newScheme.getId(), newVersion.getId());
        saveHistoryLog(SysUserHolder.getUser().getUserId(), newVersion.getId(), OperationType.SAVE);

        StabilitySchemeBasicSaveRespDTO respDTO = new StabilitySchemeBasicSaveRespDTO();
        respDTO.setSchemeId(newScheme.getId());
        respDTO.setVersionId(newVersion.getId());
        respDTO.setSchemeName(newScheme.getName());
        respDTO.setSchemeCode(newScheme.getCode());
        respDTO.setVersionNo(newVersion.getVersionNo());
        respDTO.setIsNew(true);
        return respDTO;
    }
}
