package com.bmos.lims2.server.stability.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilityCopyItemsResultDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeItemSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemePlanSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionAuditQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionAuditDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionFullConfigDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilityScheme;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeVersionService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeItemService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemePlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 稳定性方案版本Service实现类
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Service
@Slf4j
public class StabilitySchemeVersionServiceImpl implements StabilitySchemeVersionService {

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    private StabilitySchemeMapper stabilitySchemeMapper;

    @Autowired
    private StabilitySchemeItemService stabilitySchemeItemService;

    @Autowired
    private StabilitySchemePlanService stabilitySchemePlanService;

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Autowired
    private FlowAuditService flowAuditService;

    @Autowired
    private StabilitySchemeService stabilitySchemeService;

    @Override
    public CommonPage<StabilitySchemeVersionDTO> pageStabilitySchemeVersion(StabilitySchemeVersionQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<StabilitySchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getSchemeId() != null, StabilitySchemeVersion::getSchemeId, queryDTO.getSchemeId())
                .like(queryDTO.getVersionNo() != null, StabilitySchemeVersion::getVersionNo, queryDTO.getVersionNo())
                .eq(queryDTO.getStatus() != null, StabilitySchemeVersion::getStatus, queryDTO.getStatus())
                .eq(StabilitySchemeVersion::getDeleted, false)
                .orderByDesc(StabilitySchemeVersion::getCreateTime);

        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<StabilitySchemeVersion> versionList = stabilitySchemeVersionMapper.selectList(wrapper);
        CommonPage<StabilitySchemeVersion> entityPage = CommonPage.convertPage(versionList);
        List<StabilitySchemeVersionDTO> dtoList = BeanUtil.copyToList(versionList, StabilitySchemeVersionDTO.class);

        // 填充方案名称和父版本号
        for (StabilitySchemeVersionDTO dto : dtoList) {
            // 查询方案名称
            StabilityScheme scheme = stabilitySchemeMapper.selectById(dto.getSchemeId());
            if (scheme != null) {
                dto.setSchemeName(scheme.getName());
            }

            // 查询父版本号
            if (dto.getParentVersionId() != null) {
                StabilitySchemeVersion parentVersion = stabilitySchemeVersionMapper.selectById(dto.getParentVersionId());
                if (parentVersion != null) {
                    dto.setParentVersionNo(parentVersion.getVersionNo());
                }
            }

            // 设置状态名称
            if (dto.getStatus() != null) {
                dto.setStatusName(dto.getStatus().getName());
            }
        }

        CommonPage<StabilitySchemeVersionDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(entityPage.getPageNum());
        resultPage.setPageSize(entityPage.getPageSize());
        resultPage.setTotal(entityPage.getTotal());
        resultPage.setList(dtoList);
        return resultPage;
    }

    @Override
    public StabilitySchemeVersionDTO getStabilitySchemeVersion(Long id) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(id);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        StabilitySchemeVersionDTO dto = BeanUtil.copyProperties(version, StabilitySchemeVersionDTO.class);

        // 查询方案名称
        StabilityScheme scheme = stabilitySchemeMapper.selectById(version.getSchemeId());
        if (scheme != null) {
            dto.setSchemeName(scheme.getName());
            dto.setSchemeCode(scheme.getCode());
        }

        // 查询父版本号
        if (version.getParentVersionId() != null) {
            StabilitySchemeVersion parentVersion = stabilitySchemeVersionMapper.selectById(version.getParentVersionId());
            if (parentVersion != null) {
                dto.setParentVersionNo(parentVersion.getVersionNo());
            }
        }

        // 设置状态名称
        if (dto.getStatus() != null) {
            dto.setStatusName(dto.getStatus().getName());
        }

        // 查询数据权限
        dto.setDeptIds(stabilitySchemeService.getPermissions(version.getSchemeId()));

        return dto;
    }

    @Override
    public List<StabilitySchemeVersionDTO> listBySchemeId(Long schemeId) {
        LambdaQueryWrapper<StabilitySchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeVersion::getSchemeId, schemeId)
                .eq(StabilitySchemeVersion::getDeleted, false)
                .orderByDesc(StabilitySchemeVersion::getCreateTime);

        List<StabilitySchemeVersion> versionList = stabilitySchemeVersionMapper.selectList(wrapper);
        List<StabilitySchemeVersionDTO> dtoList = BeanUtil.copyToList(versionList, StabilitySchemeVersionDTO.class);

        // 填充方案名称和状态名称
        for (StabilitySchemeVersionDTO dto : dtoList) {
            StabilityScheme scheme = stabilitySchemeMapper.selectById(dto.getSchemeId());
            if (scheme != null) {
                dto.setSchemeName(scheme.getName());
            }
            if (dto.getStatus() != null) {
                dto.setStatusName(dto.getStatus().getName());
            }
        }

        return dtoList;
    }

    @Override
    public List<StabilitySchemeVersionDTO> listActiveVersionsByMaterialId(Long materialId) {
        LambdaQueryWrapper<StabilitySchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeVersion::getMaterialId, materialId)
                .eq(StabilitySchemeVersion::getStatus, StabilitySchemeVersionStatusEnum.ACTIVE)
                .eq(StabilitySchemeVersion::getDeleted, false)
                .orderByDesc(StabilitySchemeVersion::getCreateTime);

        List<StabilitySchemeVersion> versionList = stabilitySchemeVersionMapper.selectList(wrapper);
        List<StabilitySchemeVersionDTO> dtoList = BeanUtil.copyToList(versionList, StabilitySchemeVersionDTO.class);

        for (StabilitySchemeVersionDTO dto : dtoList) {
            StabilityScheme scheme = stabilitySchemeMapper.selectById(dto.getSchemeId());
            if (scheme != null) {
                dto.setSchemeName(scheme.getName());
                dto.setSchemeCode(scheme.getCode());
            }
            dto.setStatusName(StabilitySchemeVersionStatusEnum.ACTIVE.getName());
        }

        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateVersion(Long versionId) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        StabilityScheme scheme = stabilitySchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NOT_EXIST);
        }

        // 只有已完成或已失效的版本才能提交审批
        if (version.getStatus() != StabilitySchemeVersionStatusEnum.COMPLETED
                && version.getStatus() != StabilitySchemeVersionStatusEnum.INACTIVE) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }

        // 发起审批流程
        FlowStartDTO startDTO = new FlowStartDTO();
        startDTO.setBusinessKey(String.valueOf(versionId));
        startDTO.setCode(AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode());
        startDTO.setCategoryCode(AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode());
        startDTO.setName(scheme.getName());
        startDTO.setExtField(version.getVersionNo());

        String processInstanceId = flowAuditService.flowAuditStart(startDTO);

        // 更新版本状态和审批流程ID
        version.setStatus(StabilitySchemeVersionStatusEnum.APPROVING);
        version.setProcessInstanceId(processInstanceId);
        version.setUpdateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeVersionMapper.updateById(version);

        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), versionId, OperationType.SUBMIT_AUDIT, null);

        log.info("稳定性方案版本审批流程已发起，版本ID: {}, 流程实例ID: {}", versionId, processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivateVersion(Long versionId) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        // 检查当前状态
        if (version.getStatus() != StabilitySchemeVersionStatusEnum.ACTIVE) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }

        // 停用版本
        version.setStatus(StabilitySchemeVersionStatusEnum.INACTIVE);
        version.setUpdateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeVersionMapper.updateById(version);

        // 清空方案的生效版本号
        StabilityScheme scheme = stabilitySchemeMapper.selectById(version.getSchemeId());
        if (scheme != null && version.getVersionNo().equals(scheme.getActiveVersionNo())) {
            scheme.setActiveVersionNo(null);
            scheme.setUpdateBy(SysUserHolder.getUser().getUserId());
            stabilitySchemeMapper.updateById(scheme);
        }

        log.info("停用稳定性方案版本成功：versionId={}", versionId);
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), versionId, OperationType.INVALID, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voidVersion(Long versionId) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        // 检查当前状态
        if (version.getStatus() == StabilitySchemeVersionStatusEnum.ACTIVE) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_ACTIVE_CANNOT_VOID);
        }

        // 作废版本
        version.setStatus(StabilitySchemeVersionStatusEnum.VOIDED);
        version.setUpdateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeVersionMapper.updateById(version);

        log.info("作废稳定性方案版本成功：versionId={}", versionId);
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), versionId, OperationType.NULLIFY, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyVersion(Long versionId, String newVersionNo, String description) {
        // 查询原版本
        StabilitySchemeVersion sourceVersion = stabilitySchemeVersionMapper.selectById(versionId);
        if (sourceVersion == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        // 检查新版本号是否已存在
        StabilitySchemeVersion existVersion = stabilitySchemeVersionMapper.selectBySchemeIdAndVersionNo(
                sourceVersion.getSchemeId(), newVersionNo);
        if (existVersion != null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_EXISTS);
        }

        // 创建新版本
        StabilitySchemeVersion newVersion = new StabilitySchemeVersion();
        newVersion.setSchemeId(sourceVersion.getSchemeId());
        newVersion.setVersionNo(newVersionNo);
        newVersion.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
        newVersion.setDescription(description);
        newVersion.setParentVersionId(versionId);
        newVersion.setMaterialId(sourceVersion.getMaterialId());
        newVersion.setMaterialName(sourceVersion.getMaterialName());
        newVersion.setMaterialCode(sourceVersion.getMaterialCode());
        newVersion.setCreateBy(SysUserHolder.getUser().getUserId());
        stabilitySchemeVersionMapper.insert(newVersion);

        // 复制检验项目配置，获取ID映射后再复制检验计划（修正计划中的配置ID引用）
        StabilityCopyItemsResultDTO copyItemsResult =
                stabilitySchemeItemService.copyItems(versionId, newVersion.getId(), sourceVersion.getSchemeId());
        stabilitySchemePlanService.copyPlans(versionId, newVersion.getId(), sourceVersion.getSchemeId(), copyItemsResult);

        log.info("复制稳定性方案版本成功：sourceVersionId={}, newVersionId={}", versionId, newVersion.getId());
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), newVersion.getId(), OperationType.SAVE, null);

        return newVersion.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeVersion(Long versionId, StabilitySchemeSaveDTO basicDTO,
                                StabilitySchemeItemSaveDTO itemSaveDTO, StabilitySchemePlanSaveDTO planSaveDTO) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }

        if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING
                && version.getStatus() != StabilitySchemeVersionStatusEnum.COMPLETED) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }

        String userId = SysUserHolder.getUser().getUserId();

        // 更新方案基础信息
        StabilityScheme scheme = stabilitySchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NOT_EXIST);
        }
        scheme.setName(basicDTO.getName());
        if (basicDTO.getMaterial() != null) {
            scheme.setMaterialId(basicDTO.getMaterial().getMaterialId());
            scheme.setMaterialName(basicDTO.getMaterial().getMaterialName());
            scheme.setMaterialCode(basicDTO.getMaterial().getMaterialCode());
        }
        scheme.setUpdateBy(userId);
        stabilitySchemeMapper.updateById(scheme);

        // 更新版本基础信息
        version.setVersionNo(basicDTO.getVersionNo());
        version.setDescription(basicDTO.getDescription());
        if (basicDTO.getMaterial() != null) {
            version.setMaterialId(basicDTO.getMaterial().getMaterialId());
            version.setMaterialName(basicDTO.getMaterial().getMaterialName());
            version.setMaterialCode(basicDTO.getMaterial().getMaterialCode());
        }
        version.setUpdateBy(userId);
        stabilitySchemeVersionMapper.updateById(version);

        // 保存检验项目配置（增量：有ID则更新，无ID则新增，不在列表中的项目保持不变）
        if (itemSaveDTO != null) {
            itemSaveDTO.setVersionId(versionId);
            stabilitySchemeItemService.updateItems(itemSaveDTO);
        }

        // 保存检验计划（全量）
        if (planSaveDTO != null) {
            planSaveDTO.setVersionId(versionId);
            stabilitySchemePlanService.savePlans(planSaveDTO);
        }

        // 校验至少有一个检验项目
        List<com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO> items =
                stabilitySchemeItemService.listItems(versionId);
        if (items == null || items.isEmpty()) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NO_ITEM);
        }

        // 校验每个检验项目下至少有一个分析项
        for (com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO item : items) {
            if (item.getInspectionParameters() == null || item.getInspectionParameters().isEmpty()) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_ITEM_NO_PARAMETER);
            }
        }

        // 校验ELN执行方法的数据点必须绑定组件
        for (com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO item : items) {
            if (CollUtil.isEmpty(item.getInspectionParameters())) continue;
            for (com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO parameter : item.getInspectionParameters()) {
                if (parameter.getExecuteMethod() != com.bmos.lims2.common.enums.ExecuteMethodEnum.ELN) continue;
                if (CollUtil.isEmpty(parameter.getDataPoints())) continue;
                com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDataPointDTO invalid = null;
                for (com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDataPointDTO dp : parameter.getDataPoints()) {
                    if (dp.getComponentId() == null) { invalid = dp; break; }
                }
                if (invalid != null) {
                    String itemInfo = cn.hutool.core.util.StrUtil.emptyToDefault(item.getInspectItemName(), "检验项目");
                    String paramInfo = cn.hutool.core.util.StrUtil.emptyToDefault(parameter.getParameterName(), "分析项");
                    String dpInfo = cn.hutool.core.util.StrUtil.emptyToDefault(invalid.getName(), "数据点");
                    throw new BmosException(LimsResponseCode.DATA_POINT_NOT_BIND_RECORD_COMPONENT, itemInfo, paramInfo, dpInfo);
                }
            }
        }

        // 校验判定条件引用的数据点配置是否仍然有效
        stabilitySchemeService.validateJudgmentConfigConsistency(versionId);

        // 校验检验计划时间点取样量合计不超过总取样量
        stabilitySchemePlanService.validatePlanTotalAmounts(versionId);

        // 将版本状态置为已完成
        version.setStatus(StabilitySchemeVersionStatusEnum.COMPLETED);
        version.setUpdateBy(userId);
        stabilitySchemeVersionMapper.updateById(version);

        log.info("完成稳定性方案版本编辑：versionId={}", versionId);
        saveHistoryLog(null, null, userId, versionId, OperationType.COMPLETE, null);
    }

    @Override
    public CommonPage<StabilitySchemeVersionAuditDTO> pageAuditVersions(StabilitySchemeVersionAuditQueryDTO queryDTO) {
        // 先根据业务筛选条件查询版本数据
        LambdaQueryWrapper<StabilitySchemeVersion> versionWrapper = new LambdaQueryWrapper<>();
        versionWrapper.eq(StabilitySchemeVersion::getStatus, StabilitySchemeVersionStatusEnum.APPROVING)
                .eq(StabilitySchemeVersion::getDeleted, false);

        // 先筛选方案ID（方案名、方案编码）
        LambdaQueryWrapper<StabilityScheme> schemeWrapper = new LambdaQueryWrapper<>();
        schemeWrapper.eq(StabilityScheme::getDeleted, false)
                .like(StrUtil.isNotBlank(queryDTO.getSchemeName()), StabilityScheme::getName, queryDTO.getSchemeName())
                .like(StrUtil.isNotBlank(queryDTO.getSchemeCode()), StabilityScheme::getCode, queryDTO.getSchemeCode());
        List<StabilityScheme> schemes = stabilitySchemeMapper.selectList(schemeWrapper);
        if (CollUtil.isEmpty(schemes)) {
            CommonPage<StabilitySchemeVersionAuditDTO> empty = new CommonPage<>();
            empty.setPageNum(queryDTO.getPageNum());
            empty.setPageSize(queryDTO.getPageSize());
            empty.setTotal(0);
            empty.setList(new java.util.ArrayList<>());
            return empty;
        }
        List<Long> schemeIds = schemes.stream()
                .map(StabilityScheme::getId).collect(java.util.stream.Collectors.toList());
        versionWrapper.in(StabilitySchemeVersion::getSchemeId, schemeIds);

        if (CollUtil.isNotEmpty(queryDTO.getMaterialIds())) {
            versionWrapper.in(StabilitySchemeVersion::getMaterialId, queryDTO.getMaterialIds());
        }

        List<StabilitySchemeVersion> versions = stabilitySchemeVersionMapper.selectList(versionWrapper);
        if (CollUtil.isEmpty(versions)) {
            CommonPage<StabilitySchemeVersionAuditDTO> empty = new CommonPage<>();
            empty.setPageNum(queryDTO.getPageNum());
            empty.setPageSize(queryDTO.getPageSize());
            empty.setTotal(0);
            empty.setList(new java.util.ArrayList<>());
            return empty;
        }

        java.util.Map<Long, StabilitySchemeVersionAuditDTO> dataMap = new java.util.LinkedHashMap<>();
        for (StabilitySchemeVersion v : versions) {
            StabilitySchemeVersionAuditDTO dto = new StabilitySchemeVersionAuditDTO();
            dto.setId(v.getId());
            dto.setVersionNo(v.getVersionNo());
            dto.setDescription(v.getDescription());
            dto.setMaterialName(v.getMaterialName());
            dto.setMaterialCode(v.getMaterialCode());
            StabilityScheme scheme = stabilitySchemeMapper.selectById(v.getSchemeId());
            if (scheme != null) {
                dto.setSchemeName(scheme.getName());
                dto.setSchemeCode(scheme.getCode());
            }
            dataMap.put(v.getId(), dto);
        }

        // 查询待办任务
        FlowAuditTaskDTO flowAuditTaskDTO = new FlowAuditTaskDTO();
        flowAuditTaskDTO.setCategory(AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode());
        flowAuditTaskDTO.setCurrent(queryDTO.getPageNum());
        flowAuditTaskDTO.setSize(queryDTO.getPageSize());
        flowAuditTaskDTO.setOrderBy(queryDTO.getOrderBy());
        flowAuditTaskDTO.setDir(queryDTO.getDir());
        flowAuditTaskDTO.setBusinessKeyList(dataMap.keySet().stream()
                .map(String::valueOf).collect(java.util.stream.Collectors.toList()));

        PageQueryResp<List<TaskListResp>> pageResult = flowAuditService.queryToDoListByCategory(flowAuditTaskDTO);
        if (pageResult.getTotal() == 0) {
            CommonPage<StabilitySchemeVersionAuditDTO> empty = new CommonPage<>();
            empty.setPageNum(queryDTO.getPageNum());
            empty.setPageSize(queryDTO.getPageSize());
            empty.setTotal(0);
            empty.setList(new java.util.ArrayList<>());
            return empty;
        }

        List<StabilitySchemeVersionAuditDTO> resultList = new java.util.ArrayList<>();
        for (TaskListResp task : pageResult.getData()) {
            Long businessId = Long.valueOf(task.getBusinessKey());
            StabilitySchemeVersionAuditDTO auditDTO = dataMap.get(businessId);
            if (auditDTO != null) {
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

        CommonPage<StabilitySchemeVersionAuditDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(queryDTO.getPageNum());
        resultPage.setPageSize(queryDTO.getPageSize());
        resultPage.setTotal(pageResult.getTotal().intValue());
        resultPage.setList(resultList);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessSuccessCallBack(String processInstanceId, String comment, String userId) {
        log.info("稳定性方案版本审批成功回调，流程实例ID: {}, 审批人: {}", processInstanceId, userId);

        LambdaQueryWrapper<StabilitySchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeVersion::getProcessInstanceId, processInstanceId);
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectOne(wrapper);

        if (version == null) {
            log.error("根据流程实例ID未找到稳定性方案版本，流程实例ID: {}", processInstanceId);
            return;
        }

        if (version.getStatus() != StabilitySchemeVersionStatusEnum.APPROVING) {
            log.warn("稳定性方案版本状态不是审批中，版本ID: {}, 当前状态: {}", version.getId(), version.getStatus());
            return;
        }

        // 更新当前版本状态为生效（允许同一方案存在多个生效版本，不再将旧版本置为失效）
        version.setStatus(StabilitySchemeVersionStatusEnum.ACTIVE);
        version.setEffectiveDate(java.time.LocalDate.now());
        version.setUpdateBy(userId);
        stabilitySchemeVersionMapper.updateById(version);

        log.info("稳定性方案版本已生效，版本ID: {}, 流程实例ID: {}", version.getId(), processInstanceId);
        saveHistoryLog(null, null, userId, version.getId(), OperationType.APPROVE_AUDIT, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName) {
        log.info("稳定性方案版本审批拒绝回调，流程实例ID: {}, 审批人: {}, 节点: {}", processInstanceId, userId, nodeName);

        LambdaQueryWrapper<StabilitySchemeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeVersion::getProcessInstanceId, processInstanceId);
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectOne(wrapper);

        if (version == null) {
            log.error("根据流程实例ID未找到稳定性方案版本，流程实例ID: {}", processInstanceId);
            return;
        }

        if (version.getStatus() != StabilitySchemeVersionStatusEnum.APPROVING) {
            log.warn("稳定性方案版本状态不是审批中，版本ID: {}, 当前状态: {}", version.getId(), version.getStatus());
            return;
        }

        version.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
        version.setProcessInstanceId(null);
        version.setUpdateBy(userId);
        stabilitySchemeVersionMapper.updateById(version);

        log.info("稳定性方案版本审批拒绝处理完成，版本ID: {}, 流程实例ID: {}", version.getId(), processInstanceId);
        saveHistoryLog(comment, remark, userId, version.getId(), OperationType.REJECT_AUDIT, nodeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditExecutionSuccessCallBack(String businessKey, String comment, String remark, String userId, String nodeName) {
        log.info("稳定性方案版本审批执行成功回调，业务key: {}, 审批人: {}, 节点: {}", businessKey, userId, nodeName);
        saveHistoryLog(comment, remark, userId, Long.valueOf(businessKey), OperationType.APPROVE_AUDIT, nodeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditExecutionRejectCallBack(String businessKey, String comment, String userId) {
        log.info("稳定性方案版本审批执行拒绝回调，业务key: {}, 审批人: {}", businessKey, userId);
        saveHistoryLog(comment, null, userId, Long.valueOf(businessKey), OperationType.BACK_AUDIT, null);
    }

    private void saveHistoryLog(String comment, String remark, String userId, Long versionId, OperationType operationType, String nodeName) {
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.STABILITY_SCHEME.name())
                .businessId(versionId)
                .operationType(operationType.getValue())
                .comment(comment)
                .remark(remark)
                .nodeName(nodeName)
                .createBy(userId)
                .build());
    }

    @Override
    public StabilitySchemeVersionFullConfigDTO getVersionFullConfig(Long versionId) {
        StabilitySchemeVersionFullConfigDTO dto = buildBasicConfig(versionId);
        dto.setInspectionItems(stabilitySchemeItemService.listItems(versionId));
        dto.setPlans(stabilitySchemePlanService.listPlans(versionId));
        return dto;
    }

    @Override
    public StabilitySchemeVersionFullConfigDTO getVersionBasicConfig(Long versionId) {
        return buildBasicConfig(versionId);
    }

    private StabilitySchemeVersionFullConfigDTO buildBasicConfig(Long versionId) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }
        StabilitySchemeVersionFullConfigDTO dto = new StabilitySchemeVersionFullConfigDTO();
        dto.setId(version.getId());
        dto.setSchemeId(version.getSchemeId());
        dto.setVersionNo(version.getVersionNo());
        dto.setStatus(version.getStatus());
        dto.setDescription(version.getDescription());
        dto.setEffectiveDate(version.getEffectiveDate());
        dto.setParentVersionId(version.getParentVersionId());
        dto.setCreateTime(version.getCreateTime());

        StabilityScheme scheme = stabilitySchemeMapper.selectById(version.getSchemeId());
        if (scheme != null) {
            dto.setSchemeName(scheme.getName());
            dto.setSchemeCode(scheme.getCode());
        }
        if (version.getParentVersionId() != null) {
            StabilitySchemeVersion parent = stabilitySchemeVersionMapper.selectById(version.getParentVersionId());
            if (parent != null) {
                dto.setParentVersionNo(parent.getVersionNo());
            }
        }
        StabilitySchemeVersionFullConfigDTO.MaterialInfoDTO material = new StabilitySchemeVersionFullConfigDTO.MaterialInfoDTO();
        material.setMaterialId(version.getMaterialId());
        material.setMaterialName(version.getMaterialName());
        material.setMaterialCode(version.getMaterialCode());
        dto.setMaterial(material);
        return dto;
    }
}
