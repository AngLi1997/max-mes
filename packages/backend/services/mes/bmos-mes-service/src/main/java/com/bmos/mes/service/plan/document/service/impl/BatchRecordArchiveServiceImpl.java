package com.bmos.mes.service.plan.document.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.constant.ThreadPoolConstants;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.dataset.GenerateSourceEnum;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveOperateTypeEnum;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.model.FlowAuditProcess;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.dataset.handle.PlanBatchDocumentHandler;
import com.bmos.mes.service.dataset.handle.data.PlanBatchDocumentData;
import com.bmos.mes.service.dataset.handle.data.PlanLoadingData;
import com.bmos.mes.service.dataset.handle.data.RenderTemplateData;
import com.bmos.mes.service.plan.document.controller.vo.ArchiveAuditPageVO;
import com.bmos.mes.service.plan.document.controller.vo.BatchRecordArchiveVO;
import com.bmos.mes.service.plan.document.controller.vo.BatchRecordVersionVO;
import com.bmos.mes.service.plan.document.controller.vo.RecordArchiveTemplateVersionVO;
import com.bmos.mes.service.plan.document.convert.BatchRecordArchiveConverter;
import com.bmos.mes.service.plan.document.mapper.BatchRecordArchiveGenerateMapper;
import com.bmos.mes.service.plan.document.mapper.BatchRecordArchiveMapper;
import com.bmos.mes.service.plan.document.mapper.param.ArchiveParam;
import com.bmos.mes.service.plan.document.model.*;
import com.bmos.mes.service.plan.document.service.ArchiveAuditCallBackDTO;
import com.bmos.mes.service.plan.document.service.BatchRecordArchiveLogService;
import com.bmos.mes.service.plan.document.service.BatchRecordArchiveService;
import com.bmos.mes.service.plan.document.service.BatchTemplateService;
import com.bmos.mes.service.plan.document.service.dto.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.info.vo.PlanEasyInfoVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.dict.enums.DictCodeConstants;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.constant.RecordConstant.ERROR_PATH;
import static com.bmos.mes.service.platform.PlatformCodeConstants.BATCH_RECORD_ARCHIVE_SERIAL;

/**
 * 批记录档案服务
 */
@Service
@Slf4j
public class BatchRecordArchiveServiceImpl implements BatchRecordArchiveService {

    @Autowired
    private BatchRecordArchiveMapper batchRecordArchiveMapper;

    @Autowired
    private PlanService planService;

    @Autowired
    private BatchTemplateService batchTemplateService;

    @Autowired
    private BatchRecordArchiveLogService batchRecordArchiveLogService;

    @Autowired
    private MinioFileClient minioFileClient;

    @Autowired
    private FlowAuditService auditService;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    PlatformCodeFeign platformCodeFeign;

    @Autowired
    @Lazy
    PlanBatchDocumentHandler planBatchDocumentHandler;

    @Autowired
    ProductPlanRelationService productPlanRelationService;

    @Autowired
    private BatchRecordArchiveGenerateMapper batchRecordArchiveGenerateMapper;

    @Value("${minio.url}")
    private String minioUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCallBack(GenerateBatchRecordDTO dto) {
        // 生成批记录回调业务
        doCallback(dto);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getBatchArchiveProductTreeByTemplateId(Integer categoryType, Long templateId) {
        // 查询绑定有模板的工艺
        List<Long> processIdList = batchTemplateService.selectProcessIdListByTemplateId(templateId);
        return productMaterialService.queryTreeNodeByCategoryTypeAndProcessId(categoryType, processIdList);
    }


    @Override
    public CommonPage<RecordArchiveTemplateVersionVO> archivePage(RecordArchiveTemplateVersionDTO dto) {
        BatchRecordArchiveQueryDTO archiveQueryDTO = getQueryDTO(dto);
        // 拥有权限的模板列表
        if (CollUtil.isEmpty(archiveQueryDTO.getTemplateIdList())) {
            return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<RecordArchiveTemplateVersionVO> list = batchRecordArchiveMapper.queryBatchArchivePage(archiveQueryDTO);
        CommonPage<RecordArchiveTemplateVersionVO> result = CommonPage.convertPage(list);
        result.getList().forEach(e->e.setPlanId(dto.getPlanId()));
        return result;
    }

    private BatchRecordArchiveQueryDTO getQueryDTO(RecordArchiveTemplateVersionDTO dto) {
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 需要具备数据权限
        BatchRecordArchiveQueryDTO archiveQueryDTO = dto.convertArchiveQueryDTO();
        archiveQueryDTO.setProcessId(plan.getProcessId());
        archiveQueryDTO.setPlanId(dto.getPlanId());
        archiveQueryDTO.setDeptIds(platformApiAdaptor.deptIds());
        archiveQueryDTO.setStatusValue(BatchRecordArchiveStatusEnum.EFFECTIVE.getValue());
        // 获取生产计划绑定的工艺下绑定的所有模板id
        List<BatchTemplateInfoProcess> batchTemplateInfoProcessList = batchTemplateService.selectTemplateProcessByProcessId(plan.getProcessId());
        if (CollUtil.isNotEmpty(batchTemplateInfoProcessList)) {
            List<BatchTemplateInfo> batchTemplateInfos = batchTemplateService.selectAuthByIdList(CollectionUtils.convertList(batchTemplateInfoProcessList, BatchTemplateInfoProcess::getBatchTemplateInfoId), platformApiAdaptor.deptIds());
            archiveQueryDTO.setTemplateIdList(CollectionUtils.convertList(batchTemplateInfos, BatchTemplateInfo::getId));
        }
        return archiveQueryDTO;
    }

    @Override
    public BatchRecordVersionVO planInfo(RecordPlanInfoDTO dto) {
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        BatchTemplateInfo templateInfo = batchTemplateService.selectById(dto.getTemplateInfoId());
        return BatchRecordArchiveConverter.INSTANCE.convert2BatchRecordVersionVO(plan, templateInfo);
    }

    @Override
    public CommonPage<BatchRecordArchiveVO> planArchiveRecordPage(RecordArchivePageDTO dto) {
        // 根据生产计划id以及版本id获取批记录
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 获取模板下的版本
        List<Long> templateVersionIdList = batchTemplateService.selectTemplateVersionByInfoId(dto.getTemplateInfoId());
        if (CollUtil.isEmpty(templateVersionIdList)){
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<BatchRecordArchive> batchRecordArchives = batchRecordArchiveMapper.selectPlanArchiveRecord(dto, templateVersionIdList);
        return CommonPage.convertPage(batchRecordArchives, BatchRecordArchiveConverter.INSTANCE::convert2BatchRecordArchiveVOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long reGenerate(BatchRecordArchiveOperateDTO dto) {
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(dto.getArchiveId());
        if (Objects.isNull(batchRecordArchive)){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_NOT_EXISTS);
        }
        if (!BatchRecordArchiveStatusEnum.EDIT.getValue().equals(batchRecordArchive.getStatus())){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_EDIT_ALREADY_OPERATE, batchRecordArchive.getArchiveNo(),
                    BatchRecordArchiveOperateTypeEnum.RE_GENERATE.getName());
        }
        Plan plan = planService.getById(batchRecordArchive.getPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 查询模板是否存在
        BatchTemplateVersion templateVersion = batchTemplateService.selectByVersionId(batchRecordArchive.getBatchTemplateVersionId());
        if (Objects.isNull(templateVersion)){
            throw new BmosException(MesResponseCode.BATCH_TEMPLATE_VERSION_NOT_EXISTS);
        }
        BatchRecordArchiveGenerate batchRecordArchiveGenerate = BatchRecordArchiveConverter.INSTANCE.convert2ArchiveGenerateDO(templateVersion.getId(), plan);
        batchRecordArchiveGenerate.setComplete(Boolean.FALSE);
        batchRecordArchiveGenerate.setDeleteFileFlag(Boolean.FALSE);
        batchRecordArchiveGenerate.setUserId(SysUserHolder.getUser().getUserId());
        batchRecordArchiveGenerate.setBatchRecordArchiveId(batchRecordArchive.getId());
        batchRecordArchiveGenerate.setOperateType(BatchRecordArchiveOperateTypeEnum.RE_GENERATE.getValue());
        batchRecordArchiveGenerateMapper.insert(batchRecordArchiveGenerate);
        // 查询需要生成的批次关联的所有批次id
        List<PlanEasyInfoVO> relationPlanList = planService.relationPlan(plan.getId());
        Map<Long, PlanEasyInfoVO> planMap = relationPlanList.stream().collect(Collectors.toMap(PlanEasyInfoVO::getId, Function.identity()));
        List<PlanEasyInfoVO> sortRelationPlanList = new ArrayList<>();
        for (Long planId : dto.getSortPlanIdList()) {
            sortRelationPlanList.add(planMap.get(planId));
        }
        List<PlanLoadingData> planLoadingDataList = new ArrayList<>();
        if (CollUtil.isNotEmpty(sortRelationPlanList)){
            planLoadingDataList = sortRelationPlanList.stream().map(planEasyInfoVO -> new PlanLoadingData().setPlanId(planEasyInfoVO.getId())).collect(Collectors.toList());
        }
        doGenerateArchive(plan, Lists.newArrayList(templateVersion), Lists.newArrayList(batchRecordArchiveGenerate.getId()), planLoadingDataList);
        return batchRecordArchiveGenerate.getId();
    }

    @Override
    public void download(BatchRecordArchiveOperateDTO dto, HttpServletResponse response) {
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(dto.getArchiveId());
        if (Objects.isNull(batchRecordArchive)){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_NOT_EXISTS);
        }
        downloadPath(batchRecordArchive.getPath(), response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "#dto.archiveId")
    public void auditArchive(BatchRecordArchiveOperateDTO dto) {
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(dto.getArchiveId());
        if (Objects.isNull(batchRecordArchive)){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_NOT_EXISTS);
        }
        if (!BatchRecordArchiveStatusEnum.EDIT.getValue().equals(batchRecordArchive.getStatus())){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_EDIT_ALREADY_OPERATE, batchRecordArchive.getArchiveNo(),
                    BatchRecordArchiveOperateTypeEnum.AUDIT.getName());
        }
        Plan plan = planService.getById(batchRecordArchive.getPlanId());
        // 发起审核流
        FlowStartDTO flowStartDTO = new FlowStartDTO();
        flowStartDTO.setBusinessKey(String.valueOf(batchRecordArchive.getId()));
        FlowAuditProcess flowAuditProcess = auditService.selectBindProcessFlowAudit(AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE.getCode(), plan.getProcessId());
        if (Objects.isNull(flowAuditProcess)){
            flowStartDTO.setCode(AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE.getCode());
            flowStartDTO.setCategoryCode(AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE.getCode());
        } else {
            flowStartDTO.setCode(flowAuditProcess.getCode());
            flowStartDTO.setCategoryCode(flowAuditProcess.getCategoryCode());
        }
        flowStartDTO.setName(batchRecordArchive.getArchiveNo());
        flowStartDTO.setExtField(batchRecordArchive.getArchiveNo());
        String instanceId = auditService.flowAuditStart(flowStartDTO);
        // 更新状态
        SysUser user = SysUserHolder.getUser();
        batchRecordArchive.setAuditorId(user.getUserId());
        batchRecordArchive.setAuditorName(user.getUserName());
        batchRecordArchive.setAuditorLoginName(user.getLoginName());
        batchRecordArchive.setInstanceId(instanceId);
        batchRecordArchive.setStatus(BatchRecordArchiveStatusEnum.AUDIT.getValue());
        batchRecordArchiveMapper.updateById(batchRecordArchive);
        // 记录操作日志
        batchRecordArchiveLogService.saveLog(Lists.newArrayList(ArchiveSaveLogDTO.builder().instanceId(instanceId).batchRecordArchiveId(dto.getArchiveId())
                .path(batchRecordArchive.getPath()).archiveTime(batchRecordArchive.getArchiveTime())
                .effectiveTime(batchRecordArchive.getEffectiveTime()).operateType(BatchRecordArchiveOperateTypeEnum.AUDIT.getValue())
                .remark(batchRecordArchive.getRemark()).build()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrapArchive(BatchRecordArchiveOperateDTO dto) {
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(dto.getArchiveId());
        if (Objects.isNull(batchRecordArchive)){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_NOT_EXISTS);
        }
        if (BatchRecordArchiveStatusEnum.AUDIT.getValue().equals(batchRecordArchive.getStatus())){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_AUDIT_NOT_OPERATE, batchRecordArchive.getArchiveNo(),
                    BatchRecordArchiveOperateTypeEnum.SCRAP.getName());
        }
        if (BatchRecordArchiveStatusEnum.SCRAP.getValue().equals(batchRecordArchive.getStatus())){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_SCRAP_NOT_OPERATE, batchRecordArchive.getArchiveNo(),
                    BatchRecordArchiveOperateTypeEnum.SCRAP.getName());
        }
        batchRecordArchive.setStatus(BatchRecordArchiveStatusEnum.SCRAP.getValue());
        batchRecordArchiveMapper.updateById(batchRecordArchive);
        // 记录操作日志
        batchRecordArchiveLogService.saveLog(Lists.newArrayList(ArchiveSaveLogDTO.builder().batchRecordArchiveId(dto.getArchiveId())
                .path(batchRecordArchive.getPath()).operateType(BatchRecordArchiveOperateTypeEnum.SCRAP.getValue())
                .archiveTime(batchRecordArchive.getArchiveTime()).effectiveTime(batchRecordArchive.getEffectiveTime())
                .remark(batchRecordArchive.getRemark()).build()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateArchive(ArchiveGenerateDTO dto) {
        // 查询生产计划是否存在
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 查询模板是否存在
        BatchTemplateVersion templateVersion = batchTemplateService.selectByVersionId(dto.getTemplateVersionId());
        if (Objects.isNull(templateVersion)){
            throw new BmosException(MesResponseCode.BATCH_TEMPLATE_VERSION_NOT_EXISTS);
        }
        // 生成操作记录
        BatchRecordArchiveGenerate batchRecordArchiveGenerate = BatchRecordArchiveConverter.INSTANCE.convert2ArchiveGenerateDO(templateVersion.getId(), plan);
        batchRecordArchiveGenerate.setComplete(Boolean.FALSE);
        batchRecordArchiveGenerate.setDeleteFileFlag(Boolean.FALSE);
        batchRecordArchiveGenerate.setOperateType(BatchRecordArchiveOperateTypeEnum.GENERATE.getValue());
        batchRecordArchiveGenerate.setUserId(SysUserHolder.getUser().getUserId());
        batchRecordArchiveGenerateMapper.insert(batchRecordArchiveGenerate);
        // 生成批记录
        List<PlanEasyInfoVO> relationPlanList = planService.relationPlan(plan.getId());
        Map<Long, PlanEasyInfoVO> planMap = relationPlanList.stream().collect(Collectors.toMap(PlanEasyInfoVO::getId, Function.identity()));
        List<PlanEasyInfoVO> sortRelationPlanList = new ArrayList<>();
        for (Long planId : dto.getSortPlanIdList()) {
            sortRelationPlanList.add(planMap.get(planId));
        }
        List<PlanLoadingData> planLoadingDataList = new ArrayList<>();
        if (CollUtil.isNotEmpty(sortRelationPlanList)){
            planLoadingDataList = sortRelationPlanList.stream().map(planEasyInfoVO -> new PlanLoadingData().setPlanId(planEasyInfoVO.getId())).collect(Collectors.toList());
        }
        doGenerateArchive(plan, Lists.newArrayList(templateVersion), Lists.newArrayList(batchRecordArchiveGenerate.getId()), planLoadingDataList);
        return batchRecordArchiveGenerate.getId();
    }

    @Override
    public void autoGenerateArchive(ArchiveAutoGenerateDTO dto) {
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 查询当前生产计划关联的工艺下所有批记录模板
        List<BatchTemplateVersion> templateVersionList = batchTemplateService.selectByNormalProcessId(plan.getProcessId());
        if (CollUtil.isEmpty(templateVersionList)){
            return ;
        }
        // 生成操作记录
        List<BatchRecordArchiveGenerate> batchRecordArchiveGenerateList = Lists.newArrayList();
        for (BatchTemplateVersion batchTemplateVersion : templateVersionList) {
            BatchRecordArchiveGenerate batchRecordArchiveGenerate = BatchRecordArchiveConverter.INSTANCE.convert2ArchiveGenerateDO(batchTemplateVersion.getId(), plan);
            batchRecordArchiveGenerate.setComplete(Boolean.FALSE);
            batchRecordArchiveGenerate.setDeleteFileFlag(Boolean.FALSE);
            batchRecordArchiveGenerate.setUserId(SysUserHolder.getUser().getUserId());
            batchRecordArchiveGenerate.setOperateType(BatchRecordArchiveOperateTypeEnum.AUTO_GENERATE.getValue());
            batchRecordArchiveGenerateList.add(batchRecordArchiveGenerate);
        }
        batchRecordArchiveGenerateMapper.insertBatch(batchRecordArchiveGenerateList);
        List<Long> generateIdList = batchRecordArchiveGenerateList.stream().map(BatchRecordArchiveGenerate::getId).collect(Collectors.toList());
        // 生成批记录
        List<PlanEasyInfoVO> sortRelationPlanList = planService.relationPlan(plan.getId());
        List<PlanLoadingData> planLoadingDataList = new ArrayList<>();
        if (CollUtil.isNotEmpty(sortRelationPlanList)){
            planLoadingDataList = sortRelationPlanList.stream().map(planEasyInfoVO -> new PlanLoadingData().setPlanId(planEasyInfoVO.getId())).collect(Collectors.toList());
        }
        doGenerateArchive(plan, templateVersionList, generateIdList, planLoadingDataList);
    }

    @Override
    public void downloadPath(String path, HttpServletResponse response) {
        try {
            minioFileClient.download(MinioBucket.ARCHIVE_BUCKET, path, response);
        } catch (Exception e) {
            log.error("下载批记录模板失败", e);
        }
    }

    @Override
    public CommonPage<ArchiveAuditPageVO> archiveFlowPage(ArchiveAuditPageDTO dto) {
        FlowAuditTaskDTO flowAuditTaskDTO = dto.convertAuditTaskDTO();
        if (dto.isExistsCondition()){
            // 查询模板版本
            List<BatchRecordArchive> batchRecordArchiveList = batchRecordArchiveMapper.selectByParam(ArchiveParam.builder()
                    .batchNo(dto.getBatchNo()).templateName(dto.getTemplateName()).productName(dto.getProductName()).build());
            if (CollUtil.isEmpty(batchRecordArchiveList)){
                return CommonPage.CommonPage(Collections.emptyList(), 0L, dto.convertBasePage());
            }
            List<String> instanceIdList = batchRecordArchiveList.stream().map(archive -> String.valueOf(archive.getId())).collect(Collectors.toList());
            flowAuditTaskDTO.setBusinessKeyList(instanceIdList);
        }
        // 查询代办列表
        PageQueryResp<List<TaskListResp>> taskListResps = auditService.queryToDoListByCategory(flowAuditTaskDTO);
        if (CollUtil.isEmpty(taskListResps.getData())) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto.convertBasePage());
        }
        List<String> processInstanceIds = taskListResps.getData()
                .stream()
                .map(TaskListResp::getProcessInstanceId)
                .collect(Collectors.toList());
        List<BatchRecordArchive> batchRecordArchiveList = batchRecordArchiveMapper.selectByInstanceIdList(processInstanceIds);
        List<Long> curPlanIdList = new ArrayList<>(batchRecordArchiveList.stream().map(BatchRecordArchive::getPlanId).collect(Collectors.toSet()));
        Map<String, BatchRecordArchive> batchRecordArchiveMap = CollectionUtils.convertMap(batchRecordArchiveList, BatchRecordArchive::getInstanceId);
        List<Plan> planList = planService.getByIds(curPlanIdList);
        Map<Long, Plan> planMap = CollectionUtils.convertMap(planList, Plan::getId);
        return CommonPage.CommonPage(BatchRecordArchiveConverter.INSTANCE.convert2AuditPageVOList(taskListResps.getData(), batchRecordArchiveMap, planMap),
                taskListResps.getTotal(), dto.convertBasePage());
    }

    @Override
    public void auditCallBack(ArchiveAuditCallBackDTO dto) {
        // 流程审核结束节点回调
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(dto.getArchiveId());
        if (Objects.isNull(batchRecordArchive)){
            return ;
        }
        if (!BatchRecordArchiveStatusEnum.AUDIT.getValue().equals(batchRecordArchive.getStatus())){
            return ;
        }
        SysUser user = SysUserHolder.getUser();
        LocalDateTime now = LocalDateTime.now();
        // 查询出版本以及生产计划id下生效的批记录
        batchRecordArchive.setAuditorId(user.getUserId());
        batchRecordArchive.setAuditorLoginName(user.getLoginName());
        batchRecordArchive.setAuditorName(user.getUserName());
        ArchiveSaveLogDTO saveLogDTO = ArchiveSaveLogDTO.builder().batchRecordArchiveId(batchRecordArchive.getId()).path(batchRecordArchive.getPath())
                .operateType(BatchRecordArchiveOperateTypeEnum.AUDIT_COMPLETE.getValue()).archiveTime(batchRecordArchive.getArchiveTime())
                .effectiveTime(batchRecordArchive.getEffectiveTime()).remark(batchRecordArchive.getRemark()).instanceId(batchRecordArchive.getInstanceId())
                .auditOpinion(dto.getAuditOpinion()).elementName(dto.getElementName()).auditResult(Boolean.TRUE).operatorId(user.getUserId())
                .operatorName(user.getUserName()).operatorLoginName(user.getLoginName()).operateTime(now).build();
        if (Objects.isNull(dto.getAuditResult())){
            // 节点完成只需要记录日志 回到编辑状态
            batchRecordArchiveLogService.saveLog(Lists.newArrayList(saveLogDTO));
            batchRecordArchiveMapper.updateById(batchRecordArchive);
            return ;
        }
        // 审核完成
        saveLogDTO.setAuditResult(dto.getAuditResult());
        // 审核不通过需要回滚到确定状态
        if (!dto.getAuditResult()){
            batchRecordArchive.setStatus(BatchRecordArchiveStatusEnum.EDIT.getValue());
            batchRecordArchiveMapper.updateById(batchRecordArchive);
            batchRecordArchiveLogService.saveLog(Lists.newArrayList(saveLogDTO));
            return ;
        }
        // 作废之前已经生效的批记录 整个流程结束则无需进行日志记录
        this.scrapPreEffectiveArchive(batchRecordArchive.getPlanId(), batchRecordArchive.getBatchTemplateInfoId(), now);
        // 审核通过 直接进行生效
        batchRecordArchive.setStatus(BatchRecordArchiveStatusEnum.EFFECTIVE.getValue());
        batchRecordArchive.setEffectiveTime(now);
        batchRecordArchiveMapper.updateById(batchRecordArchive);
    }

    @Override
    public String judgeGenerate(Long generateId) {
        BatchRecordArchiveGenerate batchRecordArchiveGenerate = batchRecordArchiveGenerateMapper.selectById(generateId);
        if (Objects.isNull(batchRecordArchiveGenerate)){
            return null;
        }
        if (StrUtil.equals(batchRecordArchiveGenerate.getPath(), ERROR_PATH)){
            throw new BmosException(MesResponseCode.BATCH_GENERATE_FAIL);
        }
        return batchRecordArchiveGenerate.getPath();
    }

    @Override
    public Long verifyTemplateVersion(TemplateVerifyDTO dto) {
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        BatchTemplateVersion batchTemplateVersion = batchTemplateService.selectByVersionId(dto.getTemplateVersionId());
        if (Objects.isNull(batchTemplateVersion)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST);
        }
        BatchRecordArchiveGenerate batchRecordArchiveGenerate = BatchRecordArchiveConverter.INSTANCE.convert2ArchiveGenerateDO(batchTemplateVersion.getId(), plan);
        batchRecordArchiveGenerate.setComplete(Boolean.FALSE);
        batchRecordArchiveGenerate.setDeleteFileFlag(Boolean.FALSE);
        batchRecordArchiveGenerate.setUserId(SysUserHolder.getUser().getUserId());
        batchRecordArchiveGenerate.setOperateType(BatchRecordArchiveOperateTypeEnum.VERIFIER.getValue());
        batchRecordArchiveGenerateMapper.insert(batchRecordArchiveGenerate);
        List<PlanEasyInfoVO> relationPlanList = planService.relationPlan(plan.getId());
        Map<Long, PlanEasyInfoVO> planMap = relationPlanList.stream().collect(Collectors.toMap(PlanEasyInfoVO::getId, Function.identity()));
        List<PlanEasyInfoVO> sortRelationPlanList = new ArrayList<>();
        for (Long planId : dto.getSortPlanIdList()) {
            sortRelationPlanList.add(planMap.get(planId));
        }
        List<PlanLoadingData> planLoadingDataList = new ArrayList<>();
        if (CollUtil.isNotEmpty(sortRelationPlanList)){
            planLoadingDataList = sortRelationPlanList.stream().map(planEasyInfoVO -> new PlanLoadingData().setPlanId(planEasyInfoVO.getId())).collect(Collectors.toList());
        }
        doGenerateArchive(plan, Lists.newArrayList(batchTemplateVersion), Lists.newArrayList(batchRecordArchiveGenerate.getId()), planLoadingDataList);
        return batchRecordArchiveGenerate.getId();
    }

    @Override
    public List<String> selectBusinessKeys(List<Long> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)){
            return new ArrayList<>();
        }
        // 查询当前部门所在权限的template
        Page<Object> localPage = PageHelper.getLocalPage();
        String orderBy = null;
        if (localPage != null) {
         orderBy = localPage.getOrderBy();
        }
        PageHelper.clearPage();
        List<BatchTemplateInfo> batchTemplateInfos = batchTemplateService.selectAuthByIdList(null, deptIdList);
        if (CollUtil.isEmpty(batchTemplateInfos)){
            return new ArrayList<>();
        }
        // 查询这些templateInfo下有哪些正在审批的批记录
        if (orderBy != null) {
            PageHelper.orderBy(orderBy);
        }
        List<BatchRecordArchive> batchRecordArchives = batchRecordArchiveMapper.selectByTemplateInfoIdList(batchTemplateInfos.stream().map(BatchTemplateInfo::getId).collect(Collectors.toList()));
        if (CollUtil.isEmpty(batchRecordArchives)){
            return new ArrayList<>();
        }
        return batchRecordArchives.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
    }

    @Override
    public void removeVerifyArchive() {
        List<BatchRecordArchiveGenerate> batchRecordArchiveGenerates = batchRecordArchiveGenerateMapper.selectVerifyArchive();
        if (CollUtil.isEmpty(batchRecordArchiveGenerates)){
            return ;
        }
        // 查询所有不为验证且path不为空的文件
        List<BatchRecordArchiveGenerate> notVerifyBatchRecordArchiveGenerates = batchRecordArchiveGenerateMapper.selectNotVerifyArchive();
        Set<String> notDeleteFileName = notVerifyBatchRecordArchiveGenerates.stream().map(BatchRecordArchiveGenerate::getPath).filter(StrUtil::isNotBlank).collect(Collectors.toSet());

        List<BatchRecordArchiveGenerate> couldDeleteRecordArchiveGenerates = batchRecordArchiveGenerates.stream().filter(e -> !notDeleteFileName.contains(e.getPath())).collect(Collectors.toList());
        List<String> fileNames = couldDeleteRecordArchiveGenerates.stream().map(BatchRecordArchiveGenerate::getPath).collect(Collectors.toList());
        Set<String> notDelete = minioFileClient.removeFiles(MinioBucket.ARCHIVE_BUCKET, fileNames);
        List<BatchRecordArchiveGenerate> alreadyDelete = couldDeleteRecordArchiveGenerates;
        if (CollUtil.isEmpty(notDelete)){
            alreadyDelete = couldDeleteRecordArchiveGenerates.stream().filter(e -> !notDelete.contains(e.getPath())).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(alreadyDelete)){
            for (BatchRecordArchiveGenerate batchRecordArchiveGenerate : alreadyDelete) {
                batchRecordArchiveGenerate.setDeleteFileFlag(Boolean.TRUE);
            }
            batchRecordArchiveGenerateMapper.updateBatch(alreadyDelete);
        }
        log.info("已经删除的验证的批记录文件：{}", JSON.toJSONString(alreadyDelete.stream().map(BatchRecordArchiveGenerate::getPath).collect(Collectors.toList())));

    }

    @Override
    public void effectiveArchive(BatchRecordArchiveOperateDTO dto) {
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(dto.getArchiveId());
        if (Objects.isNull(batchRecordArchive)){
            throw new BmosException(MesResponseCode.BATCH_RECORD_ARCHIVE_NOT_EXISTS);
        }
        if (!BatchRecordArchiveStatusEnum.EDIT.getValue().equals(batchRecordArchive.getStatus())){
            throw new BmosException(MesResponseCode.BATCH_ARCHIVE_NOT_EFFECTIVE, batchRecordArchive.getArchiveNo(),
                    BatchRecordArchiveOperateTypeEnum.EFFECTIVE.getName());
        }
        LocalDateTime now = LocalDateTime.now();
        // 作废之前的批记录
        this.scrapPreEffectiveArchive(batchRecordArchive.getPlanId(), batchRecordArchive.getBatchTemplateInfoId(), now);
        // 当前版本的批记录生效
        batchRecordArchive.setStatus(BatchRecordArchiveStatusEnum.EFFECTIVE.getValue());
        batchRecordArchive.setEffectiveTime(now);

        batchRecordArchiveMapper.updateById(batchRecordArchive);
        // 记录操作日志
        batchRecordArchiveLogService.saveLog(Lists.newArrayList(ArchiveSaveLogDTO.builder().batchRecordArchiveId(dto.getArchiveId())
                .path(batchRecordArchive.getPath()).operateType(BatchRecordArchiveOperateTypeEnum.EFFECTIVE.getValue())
                .archiveTime(batchRecordArchive.getArchiveTime()).effectiveTime(batchRecordArchive.getEffectiveTime())
                .remark(batchRecordArchive.getRemark()).build()));
    }

    @Override
    public BatchRecordArchive selectById(Long businessId) {
        BatchRecordArchive batchRecordArchive = batchRecordArchiveMapper.selectById(businessId);
        return batchRecordArchive;
    }


    /**
     * 生成完成批记录后回调DTO转换为实体集合
     * @param dto
     * @return
     */
    private void doCallback(GenerateBatchRecordDTO dto){
        Map<Long, BatchRecordArchiveSaveDTO>  extInfoSaveDTOMap = new HashMap<>();
        for (BatchRecordArchiveSaveDTO batchRecordArchiveSaveDTO : dto.getBatchRecordArchiveSaveDTOList()) {
            BatchRecordArchiveExtInfo extInfo = JSON.parseObject(batchRecordArchiveSaveDTO.getExtInfo(), BatchRecordArchiveExtInfo.class);
            extInfoSaveDTOMap.put(extInfo.getGenerateId(), batchRecordArchiveSaveDTO);
        }
        List<BatchRecordArchiveGenerate> batchRecordArchiveGenerates = batchRecordArchiveGenerateMapper.selectBatchIds(extInfoSaveDTOMap.keySet());
        // 回调值
        try{
            fillArchiveGenerate(batchRecordArchiveGenerates, dto, extInfoSaveDTOMap);
        } catch (Exception e){
            log.error("批记录生成回调异常 dto={}", JSON.toJSONString(dto), e);
            // 生成错误直接结束
            for (BatchRecordArchiveGenerate batchRecordArchiveGenerate : batchRecordArchiveGenerates) {
                batchRecordArchiveGenerate.setPath(RecordConstant.ERROR_PATH);
            }
            batchRecordArchiveGenerateMapper.updateBatch(batchRecordArchiveGenerates);
        }

    }

    private void fillArchiveGenerate(List<BatchRecordArchiveGenerate> batchRecordArchiveGenerates, GenerateBatchRecordDTO dto, Map<Long, BatchRecordArchiveSaveDTO>  extInfoSaveDTOMap) {
        List<String> userIdList = Lists.newArrayList();
        List<Long> planIdList = new ArrayList<>();
        List<Long> templateVersionIdList = new ArrayList<>();
        Set<Long> reGenerateArchiveIdList = new HashSet<>();
        List<BatchRecordArchiveGenerate> notRegen = new ArrayList<>();
        List<BatchRecordArchiveGenerate> regen = new ArrayList<>();
        List<String> confirmNoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (BatchRecordArchiveGenerate batchRecordArchiveGenerate : batchRecordArchiveGenerates) {
            planIdList.add(batchRecordArchiveGenerate.getPlanId());
            BatchRecordArchiveSaveDTO batchRecordArchiveSaveDTO = extInfoSaveDTOMap.get(batchRecordArchiveGenerate.getId());
            batchRecordArchiveGenerate.setPath(batchRecordArchiveSaveDTO.getPath());
            batchRecordArchiveGenerate.setComplete(Boolean.TRUE);
            if (BatchRecordArchiveOperateTypeEnum.VERIFIER.getValue().equals(batchRecordArchiveGenerate.getOperateType())){
                continue;
            }
            templateVersionIdList.add(batchRecordArchiveGenerate.getBatchTemplateVersionId());
            userIdList.add(batchRecordArchiveGenerate.getUserId());
            if (BatchRecordArchiveOperateTypeEnum.RE_GENERATE.getValue().equals(batchRecordArchiveGenerate.getOperateType())){
                regen.add(batchRecordArchiveGenerate);
                reGenerateArchiveIdList.add(batchRecordArchiveGenerate.getBatchRecordArchiveId());
                continue;
            }
            notRegen.add(batchRecordArchiveGenerate);
        }
        // 获取人员信息
        ResponseInfo<Map<String, FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> userFeign.getByUserIds(data), userIdList);
        Map<String, FeignUserVO> userVOMap = responseInfo.getData();
        // 生产计划信息
        List<Plan> planList = planService.getByIds(planIdList);
        Map<Long, Plan> planMap = planList.stream().collect(Collectors.toMap(Plan::getId, p -> p));
        List<ArchiveSaveLogDTO> saveLogDTOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(reGenerateArchiveIdList)){
            solveGen(reGenerateArchiveIdList, regen, userVOMap, now, saveLogDTOS, extInfoSaveDTOMap);
        }
        if (CollUtil.isNotEmpty(notRegen)){
            solveNotGen(templateVersionIdList, notRegen, planList.get(0).getBatchNo(), dto, batchRecordArchiveGenerates, planMap, userVOMap, confirmNoList, saveLogDTOS);
        }
        if (CollUtil.isNotEmpty(batchRecordArchiveGenerates)){
            batchRecordArchiveGenerateMapper.updateBatch(batchRecordArchiveGenerates);
        }
        // 保存日志
        if (CollUtil.isNotEmpty(saveLogDTOS)){
            batchRecordArchiveLogService.saveLog(saveLogDTOS);
        }
        // 确认编号
        if (CollUtil.isEmpty(confirmNoList)){
            return ;
        }
        platformCodeFeign.batchConfirmNo(BatchConfirmNextUseCodeDTO.builder().code(BATCH_RECORD_ARCHIVE_SERIAL)
                .fields(new HashMap<String, String>(){{put(DictCodeConstants.PRODUCT_BATCH_NO_PARAMETER, planList.get(0).getBatchNo());}}).fullNos(confirmNoList).build());
    }

    private void solveGen(Collection<Long> reGenerateArchiveIdList, List<BatchRecordArchiveGenerate> regen, Map<String, FeignUserVO> userVOMap, LocalDateTime now, List<ArchiveSaveLogDTO> saveLogDTOS, Map<Long, BatchRecordArchiveSaveDTO>  extInfoSaveDTOMap) {
        List<BatchRecordArchive> updateBatchRecordArchiveList = new ArrayList<>();
        List<BatchRecordArchive> archives = batchRecordArchiveMapper.selectBatchIds(reGenerateArchiveIdList);
        Map<Long, BatchRecordArchive> archiveMap = archives.stream().collect(Collectors.toMap(BatchRecordArchive::getId, b -> b));
        for (BatchRecordArchiveGenerate batchRecordArchiveGenerate : regen) {
            BatchRecordArchive batchRecordArchive = archiveMap.get(batchRecordArchiveGenerate.getBatchRecordArchiveId());
            // 日志记录
            FeignUserVO feignUserVO = userVOMap.get(batchRecordArchiveGenerate.getUserId());
            ArchiveSaveLogDTO saveLogDTO = ArchiveSaveLogDTO.builder()
                    .batchRecordArchiveId(batchRecordArchive.getId())
                    .path(extInfoSaveDTOMap.get(batchRecordArchiveGenerate.getId()).getPath())
                    .archiveTime(now)
                    .operateTime(now)
                    .operatorId(feignUserVO.getUserId())
                    .operatorLoginName(feignUserVO.getLoginName())
                    .operatorName(feignUserVO.getUserName())
                    .operateType(batchRecordArchiveGenerate.getOperateType())
                    .build();
            saveLogDTOS.add(saveLogDTO);
            if (BatchRecordArchiveStatusEnum.EDIT.getValue().equals(batchRecordArchive.getStatus())){
                continue;
            }
            batchRecordArchive.setPath(batchRecordArchiveGenerate.getPath());
            batchRecordArchive.setArchiveTime(now);
            updateBatchRecordArchiveList.add(batchRecordArchive);
        }
        if (CollUtil.isNotEmpty(updateBatchRecordArchiveList)){
            batchRecordArchiveMapper.updateBatch(updateBatchRecordArchiveList);
        }
    }

    private void solveNotGen(List<Long> templateVersionIdList, List<BatchRecordArchiveGenerate> notRegen, String batchNo, GenerateBatchRecordDTO dto,
                             List<BatchRecordArchiveGenerate> batchRecordArchiveGenerates, Map<Long, Plan> planMap, Map<String, FeignUserVO> userVOMap,
                             List<String> confirmNoList, List<ArchiveSaveLogDTO> saveLogDTOS) {
        List<BatchTemplateVersion> batchTemplateVersionList = batchTemplateService.selectByVersionIdList(templateVersionIdList);
        Map<Long, BatchTemplateVersion> batchTemplateVersionMap = batchTemplateVersionList.stream().collect(Collectors.toMap(BatchTemplateVersion::getId, b -> b));
        // 查询模板名称
        Set<Long> templateInfoIdSet = batchTemplateVersionList.stream().map(BatchTemplateVersion::getBatchTemplateInfoId).collect(Collectors.toSet());
        List<BatchTemplateInfo> templateInfoList = batchTemplateService.selectByIdList(templateInfoIdSet);
        Map<Long, BatchTemplateInfo> batchTemplateInfoMap = templateInfoList.stream().collect(Collectors.toMap(BatchTemplateInfo::getId, b -> b));
        List<BatchRecordArchive> batchRecordArchives = new ArrayList<>();
        LocalDateTime archiveTime = LocalDateTime.now();
        ResponseInfo<BatchNextCodeVO> batchNextUseNoResponse = platformCodeFeign.getBatchNextUseNo(BatchNextUseCodeDTO.builder().code(BATCH_RECORD_ARCHIVE_SERIAL)
                .fields(new HashMap<String, String>(){{put(DictCodeConstants.PRODUCT_BATCH_NO_PARAMETER, batchNo);}}).num(dto.getBatchRecordArchiveSaveDTOList().size()).build());
        List<String> codeNoList = batchNextUseNoResponse.getData().getNos().stream().map(BatchNextCodeVO.NextCodeVO::getNo).collect(Collectors.toList());
        for (int i = 0; i < notRegen.size(); i++) {
            BatchRecordArchiveGenerate batchRecordArchiveGenerate = batchRecordArchiveGenerates.get(i);
            BatchRecordArchive batchRecordArchive = new BatchRecordArchive();
            batchRecordArchive.setArchiveNo(codeNoList.remove(0));
            batchRecordArchive.setPath(batchRecordArchiveGenerate.getPath());

            BatchTemplateVersion batchTemplateVersion = batchTemplateVersionMap.get(batchRecordArchiveGenerate.getBatchTemplateVersionId());
            batchRecordArchive.setBatchTemplateVersionId(batchTemplateVersion.getId());
            BatchTemplateInfo batchTemplateInfo = batchTemplateInfoMap.get(batchTemplateVersion.getBatchTemplateInfoId());
            batchRecordArchive.setBatchTemplateInfoId(batchTemplateInfo.getId());
            batchRecordArchive.setTemplateName(batchTemplateInfo.getName());
            batchRecordArchive.setTemplateVersion(batchTemplateVersion.getVersion());
            batchRecordArchive.setBatchTemplateVersionId(batchTemplateVersion.getId());
            Plan plan = planMap.get(batchRecordArchiveGenerate.getPlanId());

            batchRecordArchive.setBatchNo(plan.getBatchNo());
            batchRecordArchive.setPlanId(plan.getId());
            batchRecordArchive.setProductName(plan.getProductName());
            batchRecordArchive.setStatus(BatchRecordArchiveStatusEnum.EDIT.getValue());
            batchRecordArchive.setArchiveTime(archiveTime);
            batchRecordArchives.add(batchRecordArchive);
            FeignUserVO feignUserVO = userVOMap.get(batchRecordArchiveGenerate.getUserId());
            if (Objects.nonNull(feignUserVO)){
                batchRecordArchive.setOperatorId(feignUserVO.getUserId());
                batchRecordArchive.setOperatorLoginName(feignUserVO.getLoginName());
                batchRecordArchive.setOperatorName(feignUserVO.getUserName());
            }
            confirmNoList.add(batchRecordArchive.getArchiveNo());
        }

        // 生成批记录
        batchRecordArchiveMapper.insertBatch(batchRecordArchives);
        // 更新批记录生成表
        for (int i = 0; i < notRegen.size(); i++) {
            BatchRecordArchiveGenerate batchRecordArchiveGenerate = notRegen.get(i);
            BatchRecordArchive batchRecordArchive = batchRecordArchives.get(i);
            batchRecordArchiveGenerate.setBatchRecordArchiveId(batchRecordArchive.getId());
            // 日志DTO构建
            ArchiveSaveLogDTO saveLogDTO = ArchiveSaveLogDTO.builder()
                    .batchRecordArchiveId(batchRecordArchive.getId())
                    .path(batchRecordArchive.getPath())
                    .archiveTime(batchRecordArchive.getArchiveTime())
                    .operateTime(batchRecordArchive.getArchiveTime())
                    .operatorId(batchRecordArchive.getOperatorId())
                    .operatorLoginName(batchRecordArchive.getOperatorLoginName())
                    .operatorName(batchRecordArchive.getOperatorName())
                    .operateType(batchRecordArchiveGenerate.getOperateType())
                    .remark(batchRecordArchive.getRemark()).build();
            saveLogDTOS.add(saveLogDTO);
        }
    }

    /**
     * 生成批记录
     * @param plan
     * @param templateVersionList
     */
    protected void doGenerateArchive(Plan plan, List<BatchTemplateVersion> templateVersionList, List<Long> generateIdList, List<PlanLoadingData> planLoadingDataList) {
        List<Long> sortPlanIdList = Lists.newArrayList(plan.getId());
        if (CollUtil.isNotEmpty(planLoadingDataList)){
            sortPlanIdList.addAll(planLoadingDataList.stream().map(PlanLoadingData::getPlanId).collect(Collectors.toList()));
        }
        List<RenderTemplateData> renderTemplateDataList = new ArrayList<>();
        for (int i = 0; i < templateVersionList.size(); i++) {
            BatchTemplateVersion templateVersion = templateVersionList.get(i);
            RenderTemplateData renderTemplateData = new RenderTemplateData();
            renderTemplateData.setRenderTemplateUrl(templateVersion.getPath());
            BatchRecordArchiveExtInfo build = BatchRecordArchiveExtInfo.builder()
                    .generateId(generateIdList.get(i)).build();
            renderTemplateData.setExtInfo(JSON.toJSONString(build));
            renderTemplateDataList.add(renderTemplateData);
        }
        PlanBatchDocumentData documentData = new PlanBatchDocumentData()
                .setRenderTemplateDataList(renderTemplateDataList)
                .setPlanId(plan.getId())
                .setSortPlanIdList(sortPlanIdList)
                .setRenderTemplateDataList(renderTemplateDataList)
                .setPlanLoadingData(planLoadingDataList)
                .setSourceEnum(GenerateSourceEnum.BATCH_RECORD)
                .setSortPlanIdList(sortPlanIdList);
        SysUser user = SysUserHolder.getUser();
        ThreadPoolConstants.BATCH_THREAD_POOL.execute(
                ()-> {
                    SysUserHolder.setUser(user);
                    planBatchDocumentHandler.handle(documentData);
                    SysUserHolder.remove();
                }
        );
    }

    /**
     * 作废之前已经生效的记录
     * @param planId
     * @param batchTemplateInfoId
     */
    private void scrapPreEffectiveArchive(Long planId, Long batchTemplateInfoId, LocalDateTime now) {
        List<BatchRecordArchive> batchRecordArchiveList = batchRecordArchiveMapper.selectByPlanIdAndTemplateId(planId, batchTemplateInfoId, BatchRecordArchiveStatusEnum.EFFECTIVE.getValue());
        if (CollUtil.isEmpty(batchRecordArchiveList)){
            return ;
        } else {
            batchRecordArchiveList.forEach(archive -> {
                archive.setStatus(BatchRecordArchiveStatusEnum.SCRAP.getValue());
            });
        }
        // 作废记录日志
        List<ArchiveSaveLogDTO> saveDTOList = batchRecordArchiveList.stream().map(archive -> ArchiveSaveLogDTO.builder().batchRecordArchiveId(archive.getId())
                        .path(archive.getPath()).operateType(BatchRecordArchiveOperateTypeEnum.SCRAP.getValue())
                        .archiveTime(archive.getArchiveTime()).effectiveTime(archive.getEffectiveTime())
                        .remark(archive.getRemark()).instanceId(archive.getInstanceId()).operateTime(now).build())
                .collect(Collectors.toList());
        batchRecordArchiveMapper.updateBatch(batchRecordArchiveList);
        // 日志记录
        batchRecordArchiveLogService.saveLog(saveDTOList);
    }
}
