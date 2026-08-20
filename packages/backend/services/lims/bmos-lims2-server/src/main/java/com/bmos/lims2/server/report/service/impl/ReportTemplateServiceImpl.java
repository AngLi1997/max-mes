package com.bmos.lims2.server.report.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.constants.DictCodeConstant;
import com.bmos.lims2.common.enums.*;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.lims2.server.permission.mapper.ResourcePermissionMapper;
import com.bmos.lims2.server.permission.model.ResourcePermission;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.config.minio.MinioProperties;
import com.bmos.lims2.server.report.service.ReportGenerateAsyncService;
import com.bmos.lims2.server.report.service.ReportDocTemplateProcessor;
import com.bmos.lims2.server.report.service.ReportRenderContext;
import com.bmos.lims2.server.report.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.bmos.lims2.server.platform.system.code.PlatformCodeFeignClient;
import com.bmos.lims2.server.report.mapper.ReportGenerateTaskMapper;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.lims2.server.report.dto.*;
import com.bmos.lims2.server.report.entity.*;
import com.bmos.lims2.server.report.mapper.*;
import com.bmos.lims2.server.report.service.ReportTemplateService;
import com.bmos.lims2.server.util.PageUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.base.user.SysUser;
import com.bmos.lims2.server.util.PdfWatermark;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.facade.dict.vo.DictDataFeignVO;
import com.bmos.platform.facade.dict.enums.DictCodeConstants;
import java.time.format.DateTimeFormatter;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportTemplateServiceImpl implements ReportTemplateService {

    @Autowired
    private ReportTemplateMapper templateMapper;
    @Autowired
    private ReportTemplateVersionMapper versionMapper;
    @Autowired
    private ReportTemplateSchemeBindMapper bindMapper;
    @Autowired
    private ReportTemplateOperationHistoryMapper historyMapper;
    @Autowired

    private ReportValidationTaskMapper validationTaskMapper;
    @Autowired

    private ResourcePermissionService resourcePermissionService;
    @Autowired

    private InspectionOrderMapper inspectionOrderMapper;
    @Autowired

    private ResourcePermissionMapper resourcePermissionMapper;
    @Autowired

    private MaterialMapper materialMapper;
    @Autowired

    private ReportGenerateTaskMapper reportGenerateTaskMapper;
    @Autowired

    private PlatformCodeFeignClient platformCodeFeignClient;
    @Autowired

    private StorageService storageService;
    @Autowired

    private MinioProperties minioProperties;
    @Autowired

    private PlatformApiAdaptor platformApiAdaptor;
    @Autowired
    private ReportGenerateAsyncService reportGenerateAsyncService;
    @Autowired
    private com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper inspectionSchemeVersionMapper;
    @Autowired
    private com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper inspectionSchemeMapper;
    @Autowired
    private AuditOperationLogService auditOperationLogService;
    @Autowired
    private BusinessParameterFeign businessParameterFeign;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private ReportDocTemplateProcessor reportDocTemplateProcessor;

    @Autowired
    private com.bmos.lims2.server.report.service.ReportApprovalService reportApprovalService;

    // FlowAuditService not used in this class

    @Override
    public CommonPage<ReportTemplateDTO> page(ReportTemplatePageQueryDTO dto) {
        // 数据权限：取当前登录人部门及父部门集合 -> 查出有权限的模板ID集合
        List<Long> myDeptIds = platformApiAdaptor.deptIds();
        List<Long> permittedTemplateIds;
        if (!myDeptIds.isEmpty()) {
            List<ResourcePermission> rp = resourcePermissionMapper.selectByDeptIdsAndModule(myDeptIds, PermissionModuleEnum.REPORT_TEMPLATE.getValue());
            if (rp != null && !rp.isEmpty()) {
                permittedTemplateIds = rp.stream().map(ResourcePermission::getResourceId).distinct().collect(Collectors.toList());
            } else {
                // 无权限直接返回空
                return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
            }
        } else {
            return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
        }

        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), PageUtils.getOrderByOrDefaultByCreateTimeDesc(dto));
        List<ReportTemplate> list = templateMapper.selectList(new LambdaQueryWrapperX<ReportTemplate>()
                .likeIfPresent(ReportTemplate::getName, dto.getName())
                .inIfPresent(ReportTemplate::getMaterialId, dto.getMaterialIds())
                .in(ReportTemplate::getId, permittedTemplateIds)
                .orderByDesc(ReportTemplate::getCreateTime)
        );
        CommonPage<ReportTemplate> page = CommonPage.convertPage(list);
        List<ReportTemplateDTO> dtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getList())) {
            List<Long> templateIds = page.getList().stream().map(ReportTemplate::getId).collect(Collectors.toList());
            List<Long> materialIds = page.getList().stream().map(ReportTemplate::getMaterialId).distinct().collect(Collectors.toList());
            List<Material> materials = materialIds.isEmpty() ? Collections.emptyList() : materialMapper.selectBatchIds(materialIds);
            java.util.Map<Long, Material> materialMap = materials == null ? java.util.Collections.emptyMap() : materials.stream().collect(java.util.stream.Collectors.toMap(Material::getId, m -> m));

            // 绑定关系：模板 -> 方案版本IDs，再映射为方案IDs
            java.util.List<ReportTemplateSchemeBind> binds = bindMapper.selectList(new LambdaQueryWrapperX<ReportTemplateSchemeBind>()
                    .in(ReportTemplateSchemeBind::getTemplateId, templateIds));
            java.util.Map<Long, java.util.List<Long>> templateIdToVersionIds = new java.util.HashMap<>();
            if (CollUtil.isNotEmpty(binds)) {
                for (ReportTemplateSchemeBind b : binds) {
                    templateIdToVersionIds
                            .computeIfAbsent(b.getTemplateId(), k -> new java.util.ArrayList<>())
                            .add(b.getSchemeId());
                }
            }

            for (ReportTemplate t : page.getList()) {
                ReportTemplateDTO item = BeanUtil.copyProperties(t, ReportTemplateDTO.class);
                Material m = materialMap.get(t.getMaterialId());
                if (m != null) {
                    item.setMaterialName(m.getName());
                    item.setMaterialCode(m.getCode());
                    item.setMaterialSpec(m.getSpecification());
                }
                java.util.List<Long> boundVersionIds = templateIdToVersionIds.getOrDefault(t.getId(), java.util.Collections.emptyList());
                if (!boundVersionIds.isEmpty()) {
                    item.setSchemeIdList(boundVersionIds);
                } else {
                    item.setSchemeIdList(java.util.Collections.emptyList());
                }
                dtoList.add(item);
            }
        }
        return CommonPage.CommonPage(dtoList, Long.valueOf(page.getTotal()), dto);
    }

    @Override
    public CommonPage<ReportTemplateVersionDTO> pageVersions(ReportTemplateVersionPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize(), PageUtils.getOrderByOrDefaultByCreateTimeDesc(queryDTO));
        List<ReportTemplateVersion> list = versionMapper.selectList(new LambdaQueryWrapperX<ReportTemplateVersion>()
                .eq(ReportTemplateVersion::getTemplateId, queryDTO.getTemplateId())
                .orderByDesc(ReportTemplateVersion::getCreateTime, ReportTemplateVersion::getId)
        );
        CommonPage<ReportTemplateVersion> page = CommonPage.convertPage(list);
        List<ReportTemplateVersionDTO> dtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getList())) {
            for (ReportTemplateVersion v : page.getList()) {
                dtoList.add(BeanUtil.copyProperties(v, ReportTemplateVersionDTO.class));
            }
        }
        return CommonPage.CommonPage(dtoList, Long.valueOf(page.getTotal()), queryDTO);
    }

    @Override
    public java.util.List<com.bmos.lims2.server.report.dto.ReportTemplateVersionOptionDTO> listVersionOptions(Long templateId) {
        if (templateId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "模板ID不能为空");
        }
        // 仅查询生效的模板版本（已确认）
        java.util.List<ReportTemplateVersion> versions = versionMapper.selectList(new LambdaQueryWrapperX<ReportTemplateVersion>()
                .eq(ReportTemplateVersion::getTemplateId, templateId)
                .eq(ReportTemplateVersion::getStatus, ReportTemplateVersionStatusEnum.CONFIRMED)
                .orderByDesc(ReportTemplateVersion::getIsDefault)
                .orderByDesc(ReportTemplateVersion::getUpdateTime, ReportTemplateVersion::getId)
        );
        if (CollUtil.isEmpty(versions)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<com.bmos.lims2.server.report.dto.ReportTemplateVersionOptionDTO> result = new java.util.ArrayList<>();
        for (ReportTemplateVersion v : versions) {
            com.bmos.lims2.server.report.dto.ReportTemplateVersionOptionDTO option = new com.bmos.lims2.server.report.dto.ReportTemplateVersionOptionDTO();
            option.setId(v.getId());
            option.setTemplateId(v.getTemplateId());
            option.setVersionNo(v.getVersionNo());
            option.setStatus(v.getStatus());
            option.setIsDefault(v.getIsDefault());
            option.setLabel(v.getVersionNo());
            result.add(option);
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTemplate(ReportTemplateSaveDTO dto) {
        // 名称全局唯一校验
        Long exists = templateMapper.selectCount(new LambdaQueryWrapperX<ReportTemplate>()
                .eq(ReportTemplate::getName, dto.getName()));
        if (exists != null && exists > 0) {
            throw new BmosException(LimsResponseCode.REPORT_TEMPLATE_NAME_EXISTS);
        }
        // 保存模板
        ReportTemplate entity = BeanUtil.copyProperties(dto, ReportTemplate.class);
        templateMapper.insert(entity);

        // 创建初始版本
        ReportTemplateVersion version = new ReportTemplateVersion();
        version.setTemplateId(entity.getId());
        version.setVersionNo(dto.getVersionNo());
        version.setStatus(ReportTemplateVersionStatusEnum.EDITING);
        version.setIsDefault(Boolean.FALSE);
        version.setPath(dto.getPath());
        version.setRemark(dto.getRemark());
        versionMapper.insert(version);

        // 回写默认版本ID
        ReportTemplate updateDefault = new ReportTemplate();
        updateDefault.setId(entity.getId());
        updateDefault.setDefaultVersionId(version.getId());
        templateMapper.updateById(updateDefault);

        // 保存数据权限
        savePermissions(entity.getId(), dto.getDeptIds());

        // 历史：创建
        saveHistory(version.getId(), ReportOperationTypeEnum.CREATE, null, dto.getRemark());
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createVersion(ReportTemplateVersionSaveDTO dto) {
        ReportTemplate template = templateMapper.selectById(dto.getTemplateId());
        if (template == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        Long exists = versionMapper.selectCount(new LambdaQueryWrapperX<ReportTemplateVersion>()
                .eq(ReportTemplateVersion::getTemplateId, dto.getTemplateId())
                .eq(ReportTemplateVersion::getVersionNo, dto.getVersionNo()));
        if (exists != null && exists > 0) {
            throw new BmosException(LimsResponseCode.REPORT_TEMPLATE_VERSION_EXISTED);
        }

        ReportTemplateVersion version = new ReportTemplateVersion();
        version.setTemplateId(dto.getTemplateId());
        version.setVersionNo(dto.getVersionNo());
        version.setStatus(ReportTemplateVersionStatusEnum.EDITING);
        version.setIsDefault(Boolean.FALSE);
        version.setPath(dto.getPath());
        version.setRemark(dto.getRemark());
        versionMapper.insert(version);

        saveHistory(version.getId(), ReportOperationTypeEnum.CREATE, null, dto.getRemark());
        return version.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePermissions(Long templateId, List<Long> deptIds) {
        // 先删除，后保存
        bindMapper.delete(new LambdaQueryWrapperX<ReportTemplateSchemeBind>().eq(ReportTemplateSchemeBind::getTemplateId, templateId));
        ResourcePermissionSaveDTO saveDTO = ResourcePermissionSaveDTO.builder()
                .resourceId(templateId)
                .deptIds(CollUtil.emptyIfNull(deptIds))
                .module(PermissionModuleEnum.REPORT_TEMPLATE.getValue())
                .build();
        resourcePermissionService.save(saveDTO);
    }

    @Override
    public List<Long> getPermissions(Long templateId) {
        return resourcePermissionService.getDeptListByResourceId(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindSchemes(Long templateId,  List<Long> schemeIds) {
        // 先删除已绑定
        bindMapper.delete(new LambdaQueryWrapperX<ReportTemplateSchemeBind>()
                .eq(ReportTemplateSchemeBind::getTemplateId, templateId));
        if (CollUtil.isEmpty(schemeIds)) {
            return;
        }
        List<ReportTemplateSchemeBind> binds = new ArrayList<>();
        for (Long id : schemeIds) {
            ReportTemplateSchemeBind b = new ReportTemplateSchemeBind();
            b.setTemplateId(templateId);
            // 直接绑定传入的ID（作为方案版本ID使用）
            b.setSchemeId(id);
            binds.add(b);
        }
        if (CollUtil.isNotEmpty(binds)) {
            bindMapper.insertBatch(binds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadVersionFileMultipart(MultipartFile file) {
        String bucket = minioProperties != null && minioProperties.getBuckets() != null ? minioProperties.getBuckets().getReportTemplate() : null;
        if (bucket == null || bucket.isEmpty()) {
            bucket = "report-version";
        }
        String suffix = FileNameUtil.getSuffix(file.getOriginalFilename());
        String objectKey = IdUtils.getSnowflakeStr() + "." + suffix;
        storageService.upload(bucket, objectKey, file);

        return objectKey;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmVersion(Long versionId) {
        ReportTemplateVersion v = versionMapper.selectById(versionId);
        if (v == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        if (v.getStatus() == ReportTemplateVersionStatusEnum.VOIDED) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }
        ReportTemplateVersion update = new ReportTemplateVersion();
        update.setId(versionId);
        update.setStatus(ReportTemplateVersionStatusEnum.CONFIRMED);
        versionMapper.updateById(update);
        // 写入生效版本冗余
        ReportTemplate tpl = new ReportTemplate();
        tpl.setId(v.getTemplateId());
        tpl.setEffectiveVersionId(versionId);
        tpl.setEffectiveVersionNo(v.getVersionNo());
        templateMapper.updateById(tpl);
        saveHistory(versionId, ReportOperationTypeEnum.CONFIRM, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voidVersion(Long versionId) {
        ReportTemplateVersion v = versionMapper.selectById(versionId);
        if (v == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        ReportTemplateVersion update = new ReportTemplateVersion();
        update.setId(versionId);
        update.setStatus(ReportTemplateVersionStatusEnum.VOIDED);
        versionMapper.updateById(update);
        // 如果作废的是生效版本，清空冗余
        ReportTemplate current = templateMapper.selectById(v.getTemplateId());
        if (current != null && versionId.equals(current.getEffectiveVersionId())) {
            ReportTemplate clear = new ReportTemplate();
            clear.setId(current.getId());
            clear.setEffectiveVersionId(null);
            clear.setEffectiveVersionNo(null);
            templateMapper.updateById(clear);
        }
        saveHistory(versionId, ReportOperationTypeEnum.VOID, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultVersion(Long versionId) {
        ReportTemplateVersion v = versionMapper.selectById(versionId);
        if (v == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        // 清除模板下默认
        ReportTemplateVersion clear = new ReportTemplateVersion();
        clear.setIsDefault(Boolean.FALSE);
        versionMapper.update(clear, new LambdaQueryWrapperX<ReportTemplateVersion>()
                .eq(ReportTemplateVersion::getTemplateId, v.getTemplateId()));
        // 设置默认
        ReportTemplateVersion set = new ReportTemplateVersion();
        set.setId(versionId);
        set.setIsDefault(Boolean.TRUE);
        versionMapper.updateById(set);
        // 回写模板默认版本
        ReportTemplate updateTpl = new ReportTemplate();
        updateTpl.setId(v.getTemplateId());
        updateTpl.setDefaultVersionId(versionId);
        templateMapper.updateById(updateTpl);
        // 若该版本已确认，则同时冗余为生效版本
        if (v.getStatus() == ReportTemplateVersionStatusEnum.CONFIRMED) {
            ReportTemplate eff = new ReportTemplate();
            eff.setId(v.getTemplateId());
            eff.setEffectiveVersionId(versionId);
            eff.setEffectiveVersionNo(v.getVersionNo());
            templateMapper.updateById(eff);
        }
        saveHistory(versionId, ReportOperationTypeEnum.SET_DEFAULT, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long startValidation(ReportValidationStartDTO dto) {
        // 严校验：检验单存在、属于指定检品与方案版本、且样品审核已通过
        com.bmos.lims2.server.inspect.order.entity.InspectionOrder order = inspectionOrderMapper.selectById(dto.getInspectionOrderId());
        if (order == null) {
            throw new BmosException(LimsResponseCode.CHECK_ORDER_NOT_FOUND);
        }
        if (!dto.getMaterialId().equals(order.getMaterialId())) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM);
        }
        if (InspectionOrderStatusEnum.COMPLETED != order.getOrderStatus()) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }
        ReportValidationTask task = new ReportValidationTask();
        task.setTemplateVersionId(dto.getTemplateVersionId());
        task.setMaterialId(dto.getMaterialId());
        task.setSchemeVersionId(order.getSchemeVersionId());
        task.setInspectionOrderId(dto.getInspectionOrderId());
        task.setStatus(ReportGenerateStatusEnum.PENDING);
        task.setStartTime(LocalDateTime.now());
        if (dto.getSelectedOperateVersionIds() != null && !dto.getSelectedOperateVersionIds().isEmpty()) {
            String ids = dto.getSelectedOperateVersionIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(";"));
            task.setOperateVersionIds(ids);
        }
        validationTaskMapper.insert(task);
//        saveHistory(dto.getTemplateVersionId(), ReportOperationTypeEnum.VALIDATE, null, "");
        // 异步执行（验证）在事务提交后再触发，避免未提交导致查询不到
        final Long validationTaskId = task.getId();
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    reportGenerateAsyncService.executeValidation(validationTaskId);
                }
            });
        } else {
            reportGenerateAsyncService.executeValidation(validationTaskId);
        }
        return task.getId();
    }

    @Override
    public ReportValidationTaskDTO getValidationStatus(Long taskId) {
        ReportValidationTask task = validationTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        ReportValidationTaskDTO dto = BeanUtil.copyProperties(task, ReportValidationTaskDTO.class);
        if (dto != null) {
            dto.setPath(task.getPath());
        }
        return dto;
    }

    @Override
    public List<ReportTemplateOperationHistoryDTO> listOperationHistory(Long templateVersionId) {
        List<ReportTemplateOperationHistory> list = historyMapper.selectList(new LambdaQueryWrapperX<ReportTemplateOperationHistory>()
                .eq(ReportTemplateOperationHistory::getTemplateVersionId, templateVersionId)
                .orderByDesc(ReportTemplateOperationHistory::getOperateTime, ReportTemplateOperationHistory::getId)
        );
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(i -> BeanUtil.copyProperties(i, ReportTemplateOperationHistoryDTO.class)).collect(Collectors.toList());
    }

    private void saveHistory(Long templateVersionId, ReportOperationTypeEnum type, String path, String remark) {
        ReportTemplateOperationHistory h = new ReportTemplateOperationHistory();
        h.setTemplateVersionId(templateVersionId);
        h.setOperationType(type);
        SysUser user = SysUserHolder.getUser();
        if (user != null) {
            h.setOperatorId(user.getUserId());
            h.setOperatorName(StrUtil.emptyToDefault(user.getUserName(), user.getLoginName()));
        }
        h.setOperateTime(LocalDateTime.now());
        h.setPath(path);
        h.setRemark(remark);
        historyMapper.insert(h);
    }

    @Override
    public ReportTemplateVersionDTO getVersionFile(Long versionId) {
        ReportTemplateVersion v = versionMapper.selectById(versionId);
        if (v == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        saveHistory(versionId, ReportOperationTypeEnum.DOWNLOAD, v.getPath(), null);

        return BeanUtil.copyProperties(v, ReportTemplateVersionDTO.class);
    }

    @Override
    public java.util.List<EligibleOrderDTO> listEligibleOrders(Long materialId, Long schemeVersionId) {
        List<InspectionOrder> list = inspectionOrderMapper.selectEligibleForReportValidation(materialId, schemeVersionId);
        if (CollUtil.isEmpty(list)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<EligibleOrderDTO> result = new java.util.ArrayList<>();
        for (InspectionOrder o : list) {
            EligibleOrderDTO dto = new EligibleOrderDTO();
            dto.setId(o.getId());
            dto.setOrderNo(o.getOrderNo());
            dto.setBatchNo(o.getBatchNo());
            result.add(dto);
        }
        return result;
    }

    @Override
    public java.util.List<ReportTemplateDTO> listByScheme(Long schemeVersionId) {
        java.util.List<ReportTemplateSchemeBind> binds = bindMapper.selectList(new LambdaQueryWrapperX<ReportTemplateSchemeBind>()
                .eq(ReportTemplateSchemeBind::getSchemeId, schemeVersionId));
        if (CollUtil.isEmpty(binds)) {
            return java.util.Collections.emptyList();
        }
        java.util.Set<Long> templateIds = binds.stream().map(ReportTemplateSchemeBind::getTemplateId).collect(java.util.stream.Collectors.toSet());
        java.util.List<ReportTemplate> templates = templateMapper.selectBatchIds(new java.util.ArrayList<>(templateIds));
        java.util.List<ReportTemplateVersion> versions = versionMapper.selectList(new LambdaQueryWrapperX<ReportTemplateVersion>()
                .in(ReportTemplateVersion::getTemplateId, templateIds)
        );
        java.util.List<ReportTemplateDTO> result = new java.util.ArrayList<>();
        for (ReportTemplate t : templates) {
            ReportTemplateDTO dto = BeanUtil.copyProperties(t, ReportTemplateDTO.class);
            java.util.List<ReportTemplateVersionDTO> vList = versions.stream()
                    .filter(v -> v.getTemplateId().equals(t.getId()))
                    .map(v -> BeanUtil.copyProperties(v, ReportTemplateVersionDTO.class))
                    .collect(java.util.stream.Collectors.toList());
            dto.setVersions(vList);
            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVersionFile(Long versionId, String path,String remark) {
        ReportTemplateVersion v = versionMapper.selectById(versionId);
        if (v == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        if (v.getStatus() == ReportTemplateVersionStatusEnum.CONFIRMED || v.getStatus() == ReportTemplateVersionStatusEnum.VOIDED) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }

        ReportTemplateVersion upd = new ReportTemplateVersion();
        upd.setPath(path);
        upd.setId(versionId);
        versionMapper.updateById(upd);

        saveHistory(versionId, ReportOperationTypeEnum.UPLOAD, path, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long startGenerate(ReportGenerateStartDTO dto) {
        // 校验：模板版本存在，检验单样品审核已通过
        ReportTemplateVersion v = versionMapper.selectById(dto.getTemplateVersionId());
        if (v == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        InspectionOrder order = inspectionOrderMapper.selectById(dto.getInspectionOrderId());
        if (order == null) {
            throw new BmosException(LimsResponseCode.CHECK_ORDER_NOT_FOUND);
        }
        if (InspectionOrderStatusEnum.COMPLETED != order.getOrderStatus() && InspectionOrderStatusEnum.TERMINATED != order.getOrderStatus()) {
            throw new BmosException(LimsResponseCode.REPORT_START_ORDER_STATUS_ERROR);
        }

        // 申请报告编号（规则码可按平台配置调整）
        String reportNo = platformCodeFeignClient.getNextNo("INSPECTION_REPORT", com.bmos.lims2.common.enums.CodeRuleTypeEnum.INSPECTION_REPORT_NO);

        ReportGenerateTask task = new ReportGenerateTask();
        task.setId(IdUtils.getSnowflake());
        task.setTemplateVersionId(dto.getTemplateVersionId());
        task.setInspectionOrderId(dto.getInspectionOrderId());
        task.setReportNo(reportNo);
        task.setStatus(ReportGenerateStatusEnum.PENDING);
        task.setLifecycleStatus(com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.PENDING_CONFIRM);
        task.setStartTime(LocalDateTime.now());
        task.setMaterialId(order.getMaterialId());
        task.setSchemeVersionId(order.getSchemeVersionId());

        // 保存选择的操作规程版本ID列表，报告渲染时通过ID查询规程信息
        if (dto.getSelectedOperateVersionIds() != null && !dto.getSelectedOperateVersionIds().isEmpty()) {
            String ids = dto.getSelectedOperateVersionIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(";"));
            task.setOperateVersionIds(ids);
        }

        reportGenerateTaskMapper.insert(task);
        final Long generateTaskId = task.getId();
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    reportGenerateAsyncService.executeGenerate(generateTaskId);
                }
            });
        } else {
            reportGenerateAsyncService.executeGenerate(generateTaskId);
        }
        return task.getId();
    }

    @Override
    public ReportValidationTaskDTO getGenerateStatus(Long taskId) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        ReportValidationTaskDTO dto = new ReportValidationTaskDTO();
        dto.setId(task.getId());
        dto.setStatus(task.getStatus());
        dto.setMessage(task.getMessage());
        dto.setPath(task.getPath());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setLifecycleStatus(task.getLifecycleStatus());
        dto.setDocxSnapshotPath(task.getDocxSnapshotPath());
        return dto;
    }

    @Override
    public CommonPage<ReportGeneratedDTO> pageGeneratedReports(ReportGeneratedPageQueryDTO queryDTO) {
        com.github.pagehelper.PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        java.util.List<ReportGeneratedDTO> list = reportGenerateTaskMapper.selectGeneratedPage(queryDTO);
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(list)) {
            // 填充单位名称
            com.bmos.unit.service.UnitCache unitCache = (com.bmos.unit.service.UnitCache) cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.unit.service.UnitCache.class);
            for (ReportGeneratedDTO dto : list) {
                if (dto.getUnitId() != null) {
                    dto.setUnitName(unitCache.getGlobalUnitName(dto.getUnitId()));
                }
            }

            // 批量查询请验单自定义字段
            java.util.List<Long> orderIds = list.stream().map(ReportGeneratedDTO::getInspectionOrderId).filter(java.util.Objects::nonNull).distinct().collect(java.util.stream.Collectors.toList());
            if (!orderIds.isEmpty()) {
                com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper cfMapper = (com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper) cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper.class);
                java.util.Map<Long, java.util.List<com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField>> cfMap = cfMapper.selectByInspectionOrderIds(orderIds);
                if (!cfMap.isEmpty()) {
                    for (ReportGeneratedDTO dto : list) {
                        java.util.List<com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField> cfs = cfMap.get(dto.getInspectionOrderId());
                        if (cfs != null && !cfs.isEmpty()) {
                            java.util.List<com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO> values = new java.util.ArrayList<>();
                            for (com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField entity : cfs) {
                                com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO v = new com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO();
                                v.setFieldCode(entity.getFieldCode());
                                v.setFieldName(entity.getFieldName());
                                v.setFieldValue(entity.getFieldValue());
                                v.setRequired(entity.getRequired());
                                values.add(v);
                            }
                            dto.setCustomFields(values);
                        }
                    }
                }
            }
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public com.bmos.lims2.server.report.dto.ReportGeneratedListDTO listGeneratedReports(Long inspectionOrderId, Long templateId) {
        if (inspectionOrderId == null) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.INVALID_PARAM, "检验单ID不能为空");
        }
        com.bmos.lims2.server.report.dto.ReportOrderInfoDTO orderInfo = reportGenerateTaskMapper.selectOrderInfo(inspectionOrderId);
        com.bmos.lims2.server.report.dto.ReportGeneratedListDTO result = new com.bmos.lims2.server.report.dto.ReportGeneratedListDTO();
        result.setOrderInfo(orderInfo);

        java.util.List<com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO> items = reportGenerateTaskMapper.selectGeneratedList(inspectionOrderId, templateId);

        // 补充单位名称
        if (orderInfo != null && orderInfo.getUnitId() != null) {
            com.bmos.unit.service.UnitCache unitCache = cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.unit.service.UnitCache.class);
            orderInfo.setUnitName(unitCache.getGlobalUnitName(orderInfo.getUnitId()));
        }
        java.util.List<Long> orderIds = java.util.Collections.singletonList(inspectionOrderId);
        com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper cfMapper = cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper.class);
        java.util.Map<Long, java.util.List<com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField>> cfMap = cfMapper.selectByInspectionOrderIds(orderIds);
        java.util.List<com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO> values = new java.util.ArrayList<>();
        java.util.List<com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField> cfs = cfMap.get(inspectionOrderId);
        if (cfs != null && !cfs.isEmpty()) {
            for (com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField entity : cfs) {
                com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO v = new com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO();
                v.setFieldCode(entity.getFieldCode());
                v.setFieldName(entity.getFieldName());
                v.setFieldValue(entity.getFieldValue());
                v.setRequired(entity.getRequired());
                values.add(v);
            }
        }
        if (orderInfo != null) {
            orderInfo.setCustomFields(values);
        }

        result.setReports(items == null ? java.util.Collections.emptyList() : items);
        return result;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = java.lang.Exception.class)
    public void voidReport(Long generateTaskId, String reason) {
        com.bmos.lims2.server.report.entity.ReportGenerateTask task = reportGenerateTaskMapper.selectById(generateTaskId);
        if (task == null) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.DATA_NOT_EXISTS);
        }
        com.bmos.lims2.common.enums.ReportLifecycleStatusEnum lifecycle = task.getLifecycleStatus();
        boolean allowed = lifecycle == com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.PENDING_CONFIRM
                || lifecycle == com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.PENDING_APPROVAL
                || lifecycle == com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.APPROVING
                || lifecycle == com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.EFFECTIVE;
        if (!allowed) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }

        com.bmos.lims2.server.report.entity.ReportGenerateTask update = new com.bmos.lims2.server.report.entity.ReportGenerateTask();
        update.setId(task.getId());
        update.setLifecycleStatus(com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.VOIDED);
        if (java.lang.Boolean.TRUE.equals(task.getReportApproved())) {
            update.setReportApproved(false);
        }
        StringBuilder msg = new StringBuilder(task.getMessage() == null ? "" : task.getMessage());
        if (msg.length() > 0) {
            msg.append("; ");
        }
        msg.append("voided");
        if (reason != null && !reason.trim().isEmpty()) {
            msg.append(": ").append(reason.trim());
        }
        update.setMessage(msg.toString());
        reportGenerateTaskMapper.updateById(update);

        // 记录报告作废操作历史（统一使用bm_log_operation表）
        try {
            String userId = SysUserHolder.getUser() != null ? SysUserHolder.getUser().getUserId() : null;
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .businessId(task.getId())
                    .module(AuditBusinessModule.REPORT_AUDIT.name())
                    .operationType(OperationType.NULLIFY.getValue())
                    .remark(reason == null ? "报告作废" : ("报告作废: " + reason))
                    .createBy(userId)
                    .build());
        } catch (Exception e) {
            log.warn("记录报告作废历史失败", e);
        }
    }

    @Override
    public java.util.List<com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO> listReportHistory(Long inspectionOrderId, Long templateId) {
        if (inspectionOrderId == null || templateId == null) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.INVALID_PARAM);
        }
        java.util.List<com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO> items = reportGenerateTaskMapper.selectGeneratedList(inspectionOrderId, templateId);
        return items == null ? java.util.Collections.emptyList() : items;
    }

    @Override
    public java.util.List<com.bmos.lims2.server.audit.vo.TaskHistoryVO> getReportOperationHistory(Long generateTaskId) {
        if (generateTaskId == null) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.INVALID_PARAM);
        }
        com.bmos.lims2.server.report.entity.ReportGenerateTask task = reportGenerateTaskMapper.selectById(generateTaskId);
        if (task == null || task.getReportApprovalProcessInstanceId() == null || task.getReportApprovalProcessInstanceId().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        com.bmos.lims2.server.audit.FlowAuditService flowAuditServiceBean = (com.bmos.lims2.server.audit.FlowAuditService) cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.lims2.server.audit.FlowAuditService.class);
        return flowAuditServiceBean.listTaskHistory(task.getReportApprovalProcessInstanceId());
    }

    @Override
    public java.util.List<com.bmos.lims2.server.audit.operationlog.vo.ListLogVO> listReportOperationHistory(Long taskId) {
        if (taskId == null) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.INVALID_PARAM);
        }
        // 从统一的操作历史表查询（bm_log_operation），返回格式与方案版本历史查询接口一致
        return auditOperationLogService.listRecordLog(taskId);
    }

    @Override
    public void logReportDownload(Long taskId, String path) {
        if (taskId == null) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.INVALID_PARAM);
        }
        // 记录报告下载操作历史（统一使用bm_log_operation表）
        try {
            String userId = SysUserHolder.getUser() != null ? SysUserHolder.getUser().getUserId() : null;
            // 将path信息存储在detail字段中
            String detail = path != null ? "{\"path\":\"" + path + "\"}" : null;
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .businessId(taskId)
                    .module(AuditBusinessModule.REPORT_AUDIT.name())
                    .operationType(OperationType.view.getValue())
                    .remark("下载报告")
                    .detail(detail)
                    .createBy(userId)
                    .build());
        } catch (Exception e) {
            log.warn("记录报告下载历史失败", e);
        }
    }

    @Override
    public java.io.InputStream previewReportByTask(Long taskId) {
        if (taskId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM);
        }
        ReportValidationTaskDTO dto = getGenerateStatus(taskId);
        String path = dto.getPath();
        if (path == null || path.isEmpty()) {
            throw new RuntimeException("报告文件不存在");
        }
        // 路径以 .docx 结尾时，正式 PDF 尚未生成，从快照渲染预览；否则直接提供 PDF
        return previewReportByPath(path);
    }

    @Override
    public java.io.InputStream previewReportByPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM);
        }

        // 路径以 .docx 结尾时，直接走快照预览（正式 PDF 尚未生成）
        if (path.endsWith(".docx")) {
            return previewFromSnapshot(path);
        }

        // 直接从 MinIO 获取 PDF 文件
        String bucket = minioProperties.getBuckets().getReportVersion();
        java.io.File tempFile = null;
        try {
            tempFile = java.io.File.createTempFile("report-preview-", ".pdf");
            try (java.io.InputStream inputStream = storageService.getObject(bucket, path);
                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return applyWatermarkToFile(tempFile);
        } catch (Exception e) {
            log.error("预览报告文件失败", e);
            throw new RuntimeException("预览报告文件失败: " + e.getMessage(), e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                try {
                    tempFile.delete();
                } catch (Exception e) {
                    log.warn("删除临时文件失败", e);
                }
            }
        }
    }

    /**
     * 从 DOCX 快照实时渲染 PDF 预览（不保存到存储）。
     */
    private java.io.InputStream previewFromSnapshot(String snapshotPath) {
        java.io.File tempFile = null;
        try {
            String bucket = minioProperties.getBuckets().getReportVersion();
            byte[] docxBytes;
            try (java.io.InputStream snapshotStream = storageService.getObject(bucket, snapshotPath);
                 java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = snapshotStream.read(buf)) != -1) baos.write(buf, 0, n);
                docxBytes = baos.toByteArray();
            }
            byte[] pdfBytes = reportDocTemplateProcessor.renderPdfFromDocxSnapshot(
                    docxBytes, new com.bmos.lims2.server.report.service.ReportRenderContext());
            tempFile = java.io.File.createTempFile("report-preview-", ".pdf");
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                fos.write(pdfBytes);
            }
            return applyWatermarkToFile(tempFile);
        } catch (Exception e) {
            log.error("快照预览生成失败，snapshotPath={}", snapshotPath, e);
            throw new RuntimeException("报告预览生成失败: " + e.getMessage(), e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                try { tempFile.delete(); } catch (Exception e) { log.warn("删除临时文件失败", e); }
            }
        }
    }

    /**
     * 对临时 PDF 文件添加水印后返回输入流（水印失败时回退原文件）。
     */
    private java.io.InputStream applyWatermarkToFile(java.io.File pdfFile) throws Exception {
        SysUser user = SysUserHolder.getUser();
        String watermark = String.format("%s-%s %s",
                user != null ? user.getUserName() : "",
                user != null ? user.getLoginName() : "",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        ResponseInfo<BusinessParameterDetailFeignVO> fontPathResponse = FeignUtils.handleRequest(
                data -> businessParameterFeign.detailByCode(data),
                BusinessParameterCodeConstants.PLATFORM_SYS_WATERMARK_FONT_PATH);

        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        if (fontPathResponse != null && fontPathResponse.getData() != null
                && cn.hutool.core.util.StrUtil.isNotBlank(fontPathResponse.getData().getValue())) {
            try {
                PdfWatermark.addRandomWatermarks(pdfFile.getAbsolutePath(), outputStream, watermark,
                        fontPathResponse.getData().getValue(), 10, 8);
            } catch (Exception fontEx) {
                log.error("添加水印失败，返回源文件", fontEx);
                try (java.io.FileInputStream fileInputStream = new java.io.FileInputStream(pdfFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } else {
            log.info("没有设置水印的字体,返回源文件");
            try (java.io.FileInputStream fileInputStream = new java.io.FileInputStream(pdfFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
        return new java.io.ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReport(Long taskId, ReportConfirmDTO dto) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        if (task.getStatus() != com.bmos.lims2.common.enums.ReportGenerateStatusEnum.SUCCESS) {
            throw new BmosException(LimsResponseCode.REPORT_NOT_GENERATED);
        }
        if (task.getLifecycleStatus() != com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.PENDING_CONFIRM) {
            throw new BmosException(LimsResponseCode.REPORT_CONFIRM_STATUS_ERROR);
        }

        SysUser user = SysUserHolder.getUser();
        LocalDateTime now = LocalDateTime.now();
        String userId = user != null ? user.getUserId() : null;

        // 更新确认信息和状态
        ReportGenerateTask update = new ReportGenerateTask();
        update.setId(taskId);
        update.setConfirmBy(userId);
        update.setConfirmTime(now);
        update.setInspectionConclusion(dto.getInspectionConclusion());
        update.setConfirmRemark(dto.getConfirmRemark());
        update.setLifecycleStatus(com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.PENDING_APPROVAL);
        reportGenerateTaskMapper.updateById(update);

        // 记录操作日志
        try {
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .businessId(taskId)
                    .module(AuditBusinessModule.REPORT_AUDIT.name())
                    .operationType(OperationType.CONFIRM.getValue())
                    .remark("报告确认，检验结论：" + (dto.getInspectionConclusion() != null ? dto.getInspectionConclusion() : "")
                            + (dto.getConfirmRemark() != null && !dto.getConfirmRemark().isEmpty() ? "，备注：" + dto.getConfirmRemark() : ""))
                    .createBy(userId)
                    .build());
        } catch (Exception e) {
            log.warn("记录报告确认操作历史失败", e);
        }

        // 确认后立即重渲染报告，将报告人和检验结论写入PDF
        try {
            reportApprovalService.reRenderReport(taskId);
        } catch (Exception e) {
            log.warn("报告确认后重渲染失败，taskId={}", taskId, e);
        }
    }

    @Override
    public java.util.List<ReportBasicIndexDTO> listBasicInfoIndexes() {
        java.util.List<ReportBasicIndexDTO> list = new java.util.ArrayList<>();
        list.add(new ReportBasicIndexDTO("检品名称", "${INFO:MATERIAL_NAME}"));
        list.add(new ReportBasicIndexDTO("检品编号", "${INFO:MATERIAL_CODE}"));
        list.add(new ReportBasicIndexDTO("检品规格", "${INFO:MATERIAL_SPEC}"));
        list.add(new ReportBasicIndexDTO("批号", "${INFO:BATCH_NO}"));
        list.add(new ReportBasicIndexDTO("检验项目", "${INFO:INSPECT_ITEMS}"));
        list.add(new ReportBasicIndexDTO("分析项名称", "${INFO:PARAMETERS}"));
        list.add(new ReportBasicIndexDTO("检验方案名称", "${INFO:SCHEME_NAME}"));
        list.add(new ReportBasicIndexDTO("检验方案编码", "${INFO:SCHEME_CODE}"));
        list.add(new ReportBasicIndexDTO("取样数量", "${INFO:SAMPLING_COUNT}"));
        list.add(new ReportBasicIndexDTO("取样人", "${INFO:SAMPLING_BY}"));
        list.add(new ReportBasicIndexDTO("取样时间", "${INFO:SAMPLING_TIME}"));
        list.add(new ReportBasicIndexDTO("接收时间", "${INFO:RECEIVE_TIME}"));
        list.add(new ReportBasicIndexDTO("报告生成时间", "${INFO:REPORT_GENERATE_TIME}"));
        list.add(new ReportBasicIndexDTO("报告编码", "${INFO:REPORT_NO}"));
        list.add(new ReportBasicIndexDTO("检验结论", "${INFO:INSPECT_CONCLUSION}"));
        list.add(new ReportBasicIndexDTO("报告人", "${INFO:REPORTER}"));
        list.add(new ReportBasicIndexDTO("检验依据", "${INFO:INSPECT_BASIS}"));
        // 审批节点示例（2个节点）
        list.add(new ReportBasicIndexDTO("审批节点1人名", "${Node_1_USER}"));
        list.add(new ReportBasicIndexDTO("审批节点1时间", "${Node_1_TIME}"));
        list.add(new ReportBasicIndexDTO("审批节点2人名", "${Node_2_USER}"));
        list.add(new ReportBasicIndexDTO("审批节点2时间", "${Node_2_TIME}"));
        return list;
    }

    @Override
    public java.util.List<ReportCustomFieldIndexDTO> listCustomFieldIndexes() {
        java.util.List<String> categoryList = new java.util.ArrayList<>();
        categoryList.add(DictCodeConstant.MATERIAL_CUSTOM_FIELDS);
        categoryList.add(DictCodeConstant.INSPECTION_DOCUMENT_CUSTOM_FIELDS);

        java.util.List<ReportCustomFieldIndexDTO> result = new java.util.ArrayList<>();
        try {
            ResponseInfo<java.util.List<DictDetailFeignVO>> resp =
                    FeignUtils.handleRequest(data -> dictFeign.selectDictByCategory(data), categoryList);
            if (resp != null && resp.getData() != null) {
                for (DictDetailFeignVO category : resp.getData()) {
                    if (category.getDictDataList() == null) continue;
                    for (DictDataFeignVO item : category.getDictDataList()) {
                        if (item.getDictValue() == null) continue;
                        ReportCustomFieldIndexDTO dto = new ReportCustomFieldIndexDTO();
                        dto.setCategoryCode(category.getDictCode());
                        dto.setCategoryName(category.getDictName());
                        dto.setFieldCode(item.getDictValue());
                        dto.setFieldName(item.getDictLabel());
                        dto.setIndexPlaceholder("${CUSTOM:" + category.getDictCode() + ":" + item.getDictValue() + "}");
                        result.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("查询平台字典自定义字段失败", e);
        }
        return result;
    }

    @Override
    public CommonPage<ReportTemplateDTO> pageByScheme(ReportTemplateBySchemePageQueryDTO queryDTO) {
        // 数据权限：查询当前用户可见的模板ID
        java.util.List<Long> myDeptIds = platformApiAdaptor.deptIds();
        if (myDeptIds == null || myDeptIds.isEmpty()) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }
        java.util.List<ResourcePermission> rp = resourcePermissionMapper.selectByDeptIdsAndModule(myDeptIds, PermissionModuleEnum.REPORT_TEMPLATE.getValue());
        java.util.List<Long> permittedTemplateIds = (rp == null || rp.isEmpty()) ? java.util.Collections.emptyList() : rp.stream().map(ResourcePermission::getResourceId).distinct().collect(java.util.stream.Collectors.toList());
        if (permittedTemplateIds.isEmpty()) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }

        // 计算需要的方案ID集合（若入参是版本ID，则转换为方案ID）
        java.util.Set<Long> schemeIds = new java.util.HashSet<>();
        if (queryDTO.getSchemeId() != null) {
            schemeIds.add(queryDTO.getSchemeId());
        } else if (queryDTO.getSchemeVersionId() != null) {
            com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(queryDTO.getSchemeVersionId());
            if (version != null && version.getSchemeId() != null) {
                schemeIds.add(version.getSchemeId());
            }
        }
        if (schemeIds.isEmpty()) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }

        // 绑定关系：找到绑定到这些方案的模板ID
        java.util.List<ReportTemplateSchemeBind> binds = bindMapper.selectList(new com.bmos.mybatis.query.LambdaQueryWrapperX<ReportTemplateSchemeBind>()
                .in(ReportTemplateSchemeBind::getSchemeId, schemeIds));
        if (binds == null || binds.isEmpty()) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }
        java.util.Set<Long> boundTemplateIds = binds.stream().map(ReportTemplateSchemeBind::getTemplateId).collect(java.util.stream.Collectors.toSet());
        if (boundTemplateIds.isEmpty()) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }

        // 权限与绑定取交集
        java.util.Set<Long> finalTemplateIds = new java.util.HashSet<>(permittedTemplateIds);
        finalTemplateIds.retainAll(boundTemplateIds);
        if (finalTemplateIds.isEmpty()) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }

        com.github.pagehelper.PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize(), PageUtils.getOrderByOrDefaultByUpdateTimeDesc(queryDTO));
        java.util.List<ReportTemplate> list = templateMapper.selectList(new com.bmos.mybatis.query.LambdaQueryWrapperX<ReportTemplate>()
                .likeIfPresent(ReportTemplate::getName, queryDTO.getName())
                .in(ReportTemplate::getId, finalTemplateIds)
        );
        CommonPage<ReportTemplate> page = CommonPage.convertPage(list);
        java.util.List<ReportTemplateDTO> dtoList = new java.util.ArrayList<>();
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(page.getList())) {
            java.util.List<Long> templateIds = page.getList().stream().map(ReportTemplate::getId).collect(java.util.stream.Collectors.toList());
            java.util.List<Long> materialIds = page.getList().stream().map(ReportTemplate::getMaterialId).distinct().collect(java.util.stream.Collectors.toList());
            java.util.List<Material> materials = materialIds.isEmpty() ? java.util.Collections.emptyList() : materialMapper.selectBatchIds(materialIds);
            java.util.Map<Long, Material> materialMap = (materials == null) ? java.util.Collections.emptyMap() : materials.stream().collect(java.util.stream.Collectors.toMap(Material::getId, m -> m));

            java.util.List<ReportTemplateSchemeBind> binds2 = bindMapper.selectList(new com.bmos.mybatis.query.LambdaQueryWrapperX<ReportTemplateSchemeBind>()
                    .in(ReportTemplateSchemeBind::getTemplateId, templateIds));
            java.util.Map<Long, java.util.List<Long>> templateIdToSchemeIds = new java.util.HashMap<>();
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(binds2)) {
                for (ReportTemplateSchemeBind b : binds2) {
                    templateIdToSchemeIds.computeIfAbsent(b.getTemplateId(), k -> new java.util.ArrayList<>()).add(b.getSchemeId());
                }
            }

            for (ReportTemplate t : page.getList()) {
                ReportTemplateDTO item = cn.hutool.core.bean.BeanUtil.copyProperties(t, ReportTemplateDTO.class);
                Material m = materialMap.get(t.getMaterialId());
                if (m != null) {
                    item.setMaterialName(m.getName());
                    item.setMaterialCode(m.getCode());
                    item.setMaterialSpec(m.getSpecification());
                }
                java.util.List<Long> boundSchemeIdList = templateIdToSchemeIds.getOrDefault(t.getId(), java.util.Collections.emptyList());
                if (!boundSchemeIdList.isEmpty()) {
                    item.setSchemeIdList(boundSchemeIdList.stream().filter(java.util.Objects::nonNull).distinct().collect(java.util.stream.Collectors.toList()));
                } else {
                    item.setSchemeIdList(java.util.Collections.emptyList());
                }
                dtoList.add(item);
            }
        }
        return CommonPage.CommonPage(dtoList, Long.valueOf(page.getTotal()), queryDTO);
    }

    @Override
    public CommonPage<ReportTemplateByOrderItemDTO> pageByOrder(ReportTemplateByOrderPageQueryDTO queryDTO) {
        if (queryDTO.getOrderId() == null) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }
        InspectionOrder order = inspectionOrderMapper.selectById(queryDTO.getOrderId());
        if (order == null || order.getSchemeVersionId() == null) {
            return CommonPage.CommonPage(new java.util.ArrayList<>(), 0L, queryDTO);
        }
        ReportTemplateBySchemePageQueryDTO byScheme = new ReportTemplateBySchemePageQueryDTO();
        byScheme.setPageNum(queryDTO.getPageNum());
        byScheme.setPageSize(queryDTO.getPageSize());
        byScheme.setOrderBy(queryDTO.getOrderBy());
        byScheme.setSchemeVersionId(order.getSchemeVersionId());
        byScheme.setName(queryDTO.getName());
        CommonPage<ReportTemplateDTO> page = pageByScheme(byScheme);

        java.util.List<ReportTemplateByOrderItemDTO> items = new java.util.ArrayList<>();
        if (page != null && page.getList() != null && !page.getList().isEmpty()) {
            java.util.List<ReportGenerateTask> tasks = reportGenerateTaskMapper.selectByOrderId(queryDTO.getOrderId());
            java.util.List<ReportGenerateTask> approved = new java.util.ArrayList<>();
            if (tasks != null) {
                for (ReportGenerateTask t : tasks) {
                    if (t.getStatus() == ReportGenerateStatusEnum.SUCCESS
                            && java.lang.Boolean.TRUE.equals(t.getReportApproved())) {
                        approved.add(t);
                    }
                }
            }
            java.util.Map<Long, Long> versionIdToTemplateId = new java.util.HashMap<>();
            java.util.Map<Long, String> versionIdToVersionNo = new java.util.HashMap<>();
            if (!approved.isEmpty()) {
                java.util.Set<Long> versionIds = approved.stream()
                        .map(ReportGenerateTask::getTemplateVersionId)
                        .filter(java.util.Objects::nonNull)
                        .collect(java.util.stream.Collectors.toSet());
                if (!versionIds.isEmpty()) {
                    java.util.List<ReportTemplateVersion> versions = versionMapper.selectBatchIds(new java.util.ArrayList<>(versionIds));
                    if (versions != null) {
                        for (ReportTemplateVersion v : versions) {
                            versionIdToTemplateId.put(v.getId(), v.getTemplateId());
                            versionIdToVersionNo.put(v.getId(), v.getVersionNo());
                        }
                    }
                }
            }

            for (ReportTemplateDTO t : page.getList()) {
                ReportTemplateByOrderItemDTO item = new ReportTemplateByOrderItemDTO();
                item.setTemplateId(t.getId());
                item.setTemplateName(t.getName());
                item.setOrderId(order.getId());
                item.setOrderNo(order.getOrderNo());

                // find latest approved task for this template
                ReportGenerateTask latest = null;
                if (!approved.isEmpty()) {
                    for (ReportGenerateTask task : approved) {
                        Long tplId = versionIdToTemplateId.get(task.getTemplateVersionId());
                        if (tplId != null && tplId.equals(t.getId())) {
                            if (latest == null || (latest.getReportApprovalTime() != null && task.getReportApprovalTime() != null && task.getReportApprovalTime().isAfter(latest.getReportApprovalTime()))) {
                                latest = task;
                            }
                        }
                    }
                }
                if (latest != null) {
                    item.setHasValidReport(Boolean.TRUE);
                    item.setReportTaskId(latest.getId());
                    item.setReportNo(latest.getReportNo());
                    item.setReportApproved(latest.getReportApproved());
                    item.setReportEndTime(latest.getEndTime());
                    item.setReportApprovalTime(latest.getReportApprovalTime());
                    item.setReportGeneratedBy(latest.getCreateBy());
                    item.setReportPath(latest.getPath());
                    // version info reflects the version used by the valid report
                    item.setReportTemplateVersionId(latest.getTemplateVersionId());
                    item.setReportTemplateVersionNo(versionIdToVersionNo.get(latest.getTemplateVersionId()));
                } else {
                    item.setHasValidReport(Boolean.FALSE);
                    // no valid report -> no version info
                    item.setReportTemplateVersionId(null);
                    item.setReportTemplateVersionNo(null);
                }
                items.add(item);
            }
        }
        return CommonPage.CommonPage(items, page == null ? 0L : page.getTotal(), queryDTO);
    }

    @Override
    public java.util.List<com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO> listSchemesByTemplateId(Long templateId) {
        java.util.List<com.bmos.lims2.server.report.entity.ReportTemplateSchemeBind> binds = bindMapper.selectList(new com.bmos.mybatis.query.LambdaQueryWrapperX<com.bmos.lims2.server.report.entity.ReportTemplateSchemeBind>()
                .eq(com.bmos.lims2.server.report.entity.ReportTemplateSchemeBind::getTemplateId, templateId));
        if (cn.hutool.core.collection.CollUtil.isEmpty(binds)) {
            return java.util.Collections.emptyList();
        }
        java.util.Set<Long> schemeIds = binds.stream()
                .map(com.bmos.lims2.server.report.entity.ReportTemplateSchemeBind::getSchemeId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());
        if (schemeIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme> schemes = inspectionSchemeMapper.selectBatchIds(new java.util.ArrayList<>(schemeIds));
        if (schemes == null || schemes.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        // material info for enrichment
        java.util.Map<Long, com.bmos.lims2.server.material.entity.Material> materialMap = java.util.Collections.emptyMap();
        java.util.List<Long> materialIds = schemes.stream().map(com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme::getMaterialId).filter(java.util.Objects::nonNull).distinct().collect(java.util.stream.Collectors.toList());
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            java.util.List<com.bmos.lims2.server.material.entity.Material> materials = materialMapper.selectBatchIds(materialIds);
            if (materials != null && !materials.isEmpty()) {
                materialMap = materials.stream().collect(java.util.stream.Collectors.toMap(com.bmos.lims2.server.material.entity.Material::getId, m -> m));
            }
        }
        // active version id via mapper (activeVersionNo may exist on scheme)
        java.util.Map<Long, Long> schemeIdToActiveVersionId = new java.util.HashMap<>();
        for (com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme s : schemes) {
            com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion active = inspectionSchemeVersionMapper.getActiveVersion(s.getId());
            if (active != null && active.getId() != null) {
                schemeIdToActiveVersionId.put(s.getId(), active.getId());
            }
        }
        java.util.List<com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO> result = new java.util.ArrayList<>();
        for (com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme s : schemes) {
            com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO dto = new com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO();
            dto.setId(s.getId());
            dto.setName(s.getName());
            // s.getCode() not available in entity; keep null
            dto.setActiveVersionNo(s.getActiveVersionNo());
            dto.setActiveVersionId(schemeIdToActiveVersionId.get(s.getId()));
            dto.setMaterialId(s.getMaterialId());
            dto.setPackageId(s.getPackageId());
            dto.setPackageCode(s.getPackageCode());
            com.bmos.lims2.server.material.entity.Material material = materialMap.get(s.getMaterialId());
            if (material != null) {
                dto.setMaterialCode(material.getCode());
                dto.setMaterialName(material.getName());
            } else {
                dto.setMaterialCode(s.getMaterialCode());
            }
            // displayName: 方案名称 - 版本号
            if (dto.getName() != null && dto.getActiveVersionNo() != null) {
                dto.setDisplayName(dto.getName() + " - " + dto.getActiveVersionNo());
            } else if (dto.getName() != null) {
                dto.setDisplayName(dto.getName());
            }
            result.add(dto);
        }
        return result;
    }

    /**
     * 尝试解析系统内可用的中文字体路径（Windows/Mac/Linux 常见中文字体）
     * @return 字体文件绝对路径，若不可用返回 null
     */
    private String resolveSystemChineseFontPath() {
        try {
            String os = java.lang.System.getProperty("os.name", "").toLowerCase();
            java.util.List<String> candidates = new java.util.ArrayList<>();
            if (os.contains("win")) {
                // Windows 常见中文字体
                candidates.add("C:\\\\Windows\\\\Fonts\\\\msyh.ttc");
                candidates.add("C:\\\\Windows\\\\Fonts\\\\msyh.ttf");
                candidates.add("C:\\\\Windows\\\\Fonts\\\\simsun.ttc");
                candidates.add("C:\\\\Windows\\\\Fonts\\\\simhei.ttf");
                candidates.add("C:\\\\Windows\\\\Fonts\\\\simkai.ttf");
                candidates.add("C:\\\\Windows\\\\Fonts\\\\simfang.ttf");
            } else if (os.contains("mac")) {
                // macOS 常见中文字体
                candidates.add("/System/Library/Fonts/PingFang.ttc");
                candidates.add("/System/Library/Fonts/STHeiti Light.ttc");
                candidates.add("/System/Library/Fonts/STHeiti Medium.ttc");
                candidates.add("/System/Library/Fonts/Supplemental/Songti.ttc");
            } else {
                // Linux 常见中文字体（Noto / 文泉驿 / AR PL 等）
                candidates.add("/usr/share/fonts/opentype/noto/NotoSansCJKsc-Regular.otf");
                candidates.add("/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc");
                candidates.add("/usr/share/fonts/truetype/wqy/wqy-microhei.ttc");
                candidates.add("/usr/share/fonts/truetype/wqy/wqy-microhei.ttf");
                candidates.add("/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc");
                candidates.add("/usr/share/fonts/truetype/wqy/wqy-zenhei.ttf");
                candidates.add("/usr/share/fonts/truetype/arphic/uming.ttc");
            }
            for (String path : candidates) {
                java.io.File f = new java.io.File(path);
                if (f.exists() && f.isFile()) {
                    return f.getAbsolutePath();
                }
            }
        } catch (Exception ignore) {
        }
        return null;
    }
}


