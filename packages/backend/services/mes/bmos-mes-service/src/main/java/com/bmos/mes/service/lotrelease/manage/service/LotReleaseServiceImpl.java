package com.bmos.mes.service.lotrelease.manage.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.multi.Table;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.dataset.GenerateSourceEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.model.FlowAuditProcess;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.dataset.common.DatasetTrans;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.handle.PlanBatchDocumentHandler;
import com.bmos.mes.service.dataset.handle.data.*;
import com.bmos.mes.service.dataset.mapper.IDatasetPointMapper;
import com.bmos.mes.service.dataset.mapper.IDatasetPointTemplateRelationMapper;
import com.bmos.mes.service.dataset.model.DatasetPointTemplateRelation;
import com.bmos.mes.service.dataset.util.DocxRenderUtil;
import com.bmos.mes.service.dataset.util.XlsxRenderUtil;
import com.bmos.mes.service.dataset.util.options.ChangeNumberPhotoData;
import com.bmos.mes.service.dataset.util.options.DocxRenderConstants;
import com.bmos.mes.service.dataset.util.options.DocxTakePhotoLegendReplaceOption;
import com.bmos.mes.service.dataset.util.options.ProcessTakePhotoData;
import com.bmos.mes.service.exception.mapper.ExecuteExceptionMapper;
import com.bmos.mes.service.exception.model.ExecuteException;
import com.bmos.mes.service.lotrelease.manage.convert.LotReleaseConvert;
import com.bmos.mes.service.lotrelease.manage.dto.*;
import com.bmos.mes.service.lotrelease.manage.enums.LotReleaseStatus;
import com.bmos.mes.service.lotrelease.manage.mapper.ILotReleaseHistoryMapper;
import com.bmos.mes.service.lotrelease.manage.mapper.ILotReleaseMapper;
import com.bmos.mes.service.lotrelease.manage.model.LotRelease;
import com.bmos.mes.service.lotrelease.manage.model.LotReleaseHistory;
import com.bmos.mes.service.lotrelease.manage.vo.*;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseOperateType;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateOperateType;
import com.bmos.mes.service.lotrelease.template.mapper.ILotReleaseTemplateHistoryMapper;
import com.bmos.mes.service.lotrelease.template.mapper.ILotReleaseTemplateMapper;
import com.bmos.mes.service.lotrelease.template.mapper.ILotReleaseTemplateProcessRelationMapper;
import com.bmos.mes.service.lotrelease.template.mapper.ILotReleaseTemplateVersionMapper;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplate;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateHistory;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateVersion;
import com.bmos.mes.service.plan.document.service.BatchTemplateService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.PlatformCodeConstants;
import com.bmos.mes.service.platform.code.dto.ConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 16:44
 */
@Service
@Slf4j
public class LotReleaseServiceImpl implements ILotReleaseService {

    @Resource
    private ProductMaterialService productMaterialService;

    @Resource
    private ILotReleaseTemplateProcessRelationMapper lotReleaseTemplateProcessRelationMapper;

    @Resource
    private ILotReleaseTemplateMapper lotReleaseTemplateMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ExecuteExceptionMapper executeExceptionMapper;

    @Resource
    private ILotReleaseMapper lotReleaseMapper;

    private static final String[] TEMPLATE_SUFFIXES = {"xls", "xlsx", "xlsm"};

    private static final String LOT_RELEASE_TEMPLATE_FILE_SUFFIX = "LOT_RELEASE_TEMPLATE_";
    private static final String LOT_RELEASE_FILE_SUFFIX = "LOT_RELEASE_";

    @Resource
    private MinioFileClient minioFileClient;

    @Resource
    private FlowAuditService flowAuditService;

    @Resource
    private ILotReleaseTemplateVersionMapper lotReleaseTemplateVersionMapper;

    @Resource
    @Lazy
    private PlanBatchDocumentHandler documentHandler;

    @Resource
    private IDatasetPointTemplateRelationMapper datasetPointTemplateRelationMapper;

    @Resource
    private IDatasetPointMapper datasetPointMapper;

    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private BusinessParameterFeign businessParameterFeign;

    @Resource
    private ILotReleaseHistoryMapper lotReleaseHistoryMapper;

    @Resource
    private ILotReleaseTemplateHistoryMapper lotReleaseTemplateHistoryMapper;

    @Resource
    private BatchTemplateService batchTemplateService;

    @Override
    public List<ProductCategoryTreeNodeVO> getLotReleaseProductTreeByTemplateId(Integer categoryType, Long templateId) {
        // 查询所有配置了模板的工艺id
        List<Long> processIds = lotReleaseTemplateProcessRelationMapper.selectAllProcessIdsByTemplateId(templateId);
        return productMaterialService.queryTreeNodeByCategoryTypeAndProcessId(categoryType, processIds);
    }

    @Override
    public CommonPage<LotReleasePlanVO> queryPlanPage(LotReleasePlanPageQuery pageQuery) {

        // 产品树
        List<ProductCategoryTreeNodeVO> productList = productMaterialService.getProductListCondition(pageQuery.getProductId(), pageQuery.getProductCategoryId());
        List<Long> productIds;
        if (productList == null){
            productIds = new ArrayList<>();
        }else {
            productIds = productList.stream().map(ProductCategoryTreeNodeVO::getId).collect(Collectors.toList());
        }
        // 部门权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtil.isEmpty(deptIds)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        // 查询产品绑定的，并且有权限的工艺
        List<Process> processes = processMapper.selectByProductIdsAndDeptIds(productIds, platformApiAdaptor.deptIds());
        if (CollectionUtil.isEmpty(processes)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        List<Long> processIds = processes.stream().map(Process::getId).collect(Collectors.toList());
        if (pageQuery.getLotRelease()){
            List<Long>  bindTemplateProcessIds = lotReleaseTemplateProcessRelationMapper.selectAllProcessIdsByTemplateId(null);
            processIds.removeIf(processId -> !bindTemplateProcessIds.contains(processId));
        } else {
            List<Long> bindTemplateProcessIds = batchTemplateService.selectAllProcessIds();
            processIds.removeIf(processId -> !bindTemplateProcessIds.contains(processId));
        }


        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<Plan> plans = planMapper.selectByProcessIdsNotTermination(processIds, pageQuery);
        CommonPage<Plan> planCommonPage = CommonPage.convertPage(plans);
        CommonPage<LotReleasePlanVO> result = LotReleaseConvert.INSTANCE.convertToPlanVO(planCommonPage);

        // 查询异常数量
        if (CollectionUtil.isNotEmpty(plans)) {
            Map<Long, List<ExecuteException>> collect = executeExceptionMapper.queryListByPlanIds(plans.stream().map(Plan::getId).collect(Collectors.toList()))
                    .stream()
                    .collect(Collectors.groupingBy(ExecuteException::getProductPlanId));
            result.getList().forEach(item -> {
                item.setErrorCount(collect.getOrDefault(item.getId(), new ArrayList<>()).size());
            });
        }
        return result;
    }

    @Override
    public CommonPage<LotReleasePageVO> queryPage(LotReleasePageQuery pageQuery) {
        Long planId = pageQuery.getPlanId();
        Plan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        Long processId = plan.getProcessId();
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<LotReleasePageVO> list = lotReleaseMapper.selectTemplateIdsByProcessId(pageQuery, processId, planId);
        CommonPage<LotReleasePageVO> page = CommonPage.convertPage(list);
        page.getList().forEach(item -> item.setPlanId(planId));
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String generate(LotReleaseGenerateDTO dto) {
        LotReleaseTemplateVersion version = lotReleaseTemplateVersionMapper.selectByTemplateIdAndVersion(dto.getLotReleaseTemplateId(), dto.getLotReleaseVersion());
        if (version == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }

        LotReleaseTemplate template = lotReleaseTemplateMapper.selectById(dto.getLotReleaseTemplateId());
        if (template == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_NOT_EXIST);
        }

        Plan plan = planMapper.selectById(dto.getPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        List<RenderTemplateData> renderTemplateDataList = new ArrayList<>();
        renderTemplateDataList.add(new RenderTemplateData(version.getTemplateUrl(), null));

        List<DynamicRenderingData> dynamicRenderingDataList = new ArrayList<>();
        for (LotReleaseGenerateDTO.DynamicData dynamicDatum : dto.getDynamicData()) {
            DynamicRenderingData dynamicRenderingData = new DynamicRenderingData();
            dynamicRenderingData.setSingle(dynamicDatum.getDatasetKey(), dynamicDatum.getDatasetPointKey(), dynamicDatum.getValue());
            dynamicRenderingDataList.add(dynamicRenderingData);
        }
        List<PlanLoadingData> planLoadingData = new ArrayList<>();
        List<LotReleaseGenerateDTO.BatchLink> batchLinksData = dto.getBatchLinksData();
        for (LotReleaseGenerateDTO.BatchLink batchLinksDatum : batchLinksData) {
            Long planId = batchLinksDatum.getPlanId();
            planLoadingData.add(new PlanLoadingData(planId));
        }
        List<Long> planIds = new ArrayList<>();
        planIds.add(plan.getId());
        planIds.addAll(planLoadingData.stream()
                .map(PlanLoadingData::getPlanId)
                .collect(Collectors.toList()));
        planIds.add(dto.getPlanId());
        PlanBatchDocumentData planBatchDocumentData = new PlanBatchDocumentData()
                .setPlanId(dto.getPlanId())
                .setRenderTemplateDataList(renderTemplateDataList)
                .setPlanLoadingData(planLoadingData)
                .setSourceEnum(GenerateSourceEnum.BATCH_ISSUE)
                .setDynamicRenderingData(dynamicRenderingDataList)
                .setSortPlanIdList(planIds);
        List<String> fileUrls = documentHandler.handle(planBatchDocumentData);
        // 批签发默认值生成一个文件
        String fileUrl = null;
        if (CollectionUtil.isNotEmpty(fileUrls)) {
            fileUrl = fileUrls.get(0);
        }

        LotRelease editing = lotReleaseMapper.selectEditByTemplateIdAndVersion(dto.getLotReleaseTemplateId(), dto.getLotReleaseVersion(), dto.getPlanId());
        if (!dto.getIsValid()) {
            // 存在编辑中的批签发记录
            if (editing != null){
                // 重新生成
                editing.setGeneratorName(SysUserHolder.getUser().getUserName());
                editing.setGeneratorId(SysUserHolder.getUser().getUserId());
                editing.setGenerateTime(LocalDateTime.now());
                editing.setStatus(LotReleaseStatus.EDIT);
                editing.setFileUrl(fileUrl);
                lotReleaseMapper.updateById(editing);
                lotReleaseHistoryMapper.insert(LotReleaseHistory.create(editing.getId(), LotReleaseOperateType.RE_GENERATE, null, editing.getFileUrl()));
            }else {
                // 首次生成
                LotRelease lotRelease = new LotRelease();
                lotRelease.setNo(getLotReleaseSerial(plan.getBatchNo()));
                lotRelease.setName(template.getName());
                lotRelease.setTemplateVersion(version.getVersion());
                lotRelease.setTemplateId(template.getId());
                lotRelease.setProcessId(plan.getProcessId());
                lotRelease.setProcessName(plan.getProcessName());
                lotRelease.setProductMergeCode(plan.getProductMergeCode());
                lotRelease.setSpecification(plan.getProductSpecification());
                lotRelease.setPlanId(plan.getId());
                lotRelease.setBatchNo(plan.getBatchNo());
                lotRelease.setProductId(plan.getProductId());
                lotRelease.setProductName(plan.getProductName());
                lotRelease.setGeneratorName(SysUserHolder.getUser().getUserName());
                lotRelease.setGeneratorId(SysUserHolder.getUser().getUserId());
                lotRelease.setGenerateTime(LocalDateTime.now());
                lotRelease.setStatus(LotReleaseStatus.EDIT);
                lotRelease.setFileUrl(fileUrl);
                lotReleaseMapper.insert(lotRelease);
                lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.GENERATE, null, lotRelease.getFileUrl()));
                confirmLotReleaseSerial(lotRelease.getNo(), plan.getBatchNo());
            }
        }else {
            // 保存验证历史
            lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                    version.getId(),
                    LotReleaseTemplateOperateType.VALIDATE,
                    null,
                    fileUrl
            ));
        }
        return fileUrl;
    }

    @Override
    public CommonPage<LotReleaseVersionPageVO> queryVersionPage(LotReleaseVersionPageQuery pageQuery) {
        LotReleaseTemplate lotReleaseTemplate = lotReleaseTemplateMapper.selectById(pageQuery.getLotReleaseTemplateId());
        if (lotReleaseTemplate == null) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<LotReleaseVersionPageVO> lotReleases = lotReleaseMapper.selectListByTemplateId(pageQuery);
        return CommonPage.convertPage(lotReleases);
    }

    @Override
    public List<LotReleaseGeneratePreviewVO> getGeneratePreviewList(Long processId) {
        if (processId == null) {
            return new ArrayList<>();
        }
        return lotReleaseMapper.queryGeneratePreviewListByProcessId(processId);
    }

    @Override
    public String uploadExcel(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        if (!ArrayUtil.contains(TEMPLATE_SUFFIXES, extension)) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_FILE_TYPE_ERROR);
        }
        File tempFile = File.createTempFile(LOT_RELEASE_TEMPLATE_FILE_SUFFIX + UUID.randomUUID() + "_" + System.currentTimeMillis(), "." + extension);
        file.transferTo(tempFile);
        return minioFileClient.uploadFile(MinioBucket.BMOS_LOT_RELEASE, tempFile, "/generate/" + tempFile.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExcelFile(LotReleaseUpdateExcelFileDTO dto) {
        log.info("更新批签发文件");

        LotRelease lotRelease = lotReleaseMapper.selectById(dto.getLotReleaseId());
        if (lotRelease == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_NOT_EXISTS);
        }
        if (lotRelease.getStatus() != LotReleaseStatus.EDIT) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_CANT_EDIT);
        }
        lotRelease.setFileUrl(dto.getFileUrl());
        lotRelease.setRemark(dto.getRemark());
        lotReleaseMapper.updateById(lotRelease);
        lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.UPLOAD, dto.getRemark(), dto.getFileUrl()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrap(Long id) {
        LotRelease lotRelease = lotReleaseMapper.selectById(id);
        if (lotRelease == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_NOT_EXISTS);
        }
        if (lotRelease.getStatus() != LotReleaseStatus.EDIT && lotRelease.getStatus() != LotReleaseStatus.EFFECTIVE) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_CANT_SCRAP);
        }
        lotRelease.setStatus(LotReleaseStatus.SCRAPED);
        lotReleaseMapper.updateById(lotRelease);
        // 清除关联的模板生效的批签发
        lotReleaseTemplateMapper.clearEffectiveByLotReleaseId(lotRelease.getId());
        lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.SCRAP, null, lotRelease.getFileUrl()));
    }

    @Override
    public void submit(Long id) {
        LotRelease lotRelease = lotReleaseMapper.selectById(id);
        if (lotRelease == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_NOT_EXISTS);
        }
        FlowStartDTO dto = new FlowStartDTO();
        dto.setBusinessKey(lotRelease.getId().toString());
        FlowAuditProcess flowAuditProcess = flowAuditService.selectBindProcessFlowAudit(AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode(), lotRelease.getProcessId());
        if (Objects.isNull(flowAuditProcess)){
            dto.setCode(AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode());
            dto.setCategoryCode(AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode());
        } else {
            dto.setCode(flowAuditProcess.getCode());
            dto.setCategoryCode(flowAuditProcess.getCategoryCode());
        }
        dto.setName(lotRelease.getName());
        dto.setExtField(lotRelease.getNo());
        String instanceId = flowAuditService.flowAuditStart(dto);
        lotRelease.setAuditProcessInstance(instanceId);
        lotRelease.setSubmitterId(SysUserHolder.getUser().getUserId());
        lotRelease.setSubmitterName(SysUserHolder.getUser().getUserName());
        lotRelease.setSubmitterTime(LocalDateTime.now());
        lotRelease.setStatus(LotReleaseStatus.PROCESSING);
        lotReleaseMapper.updateById(lotRelease);
        lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.SUBMIT, null, lotRelease.getFileUrl()));
    }

    @Override
    public CommonPage<LotReleaseAuditPageVO> queryAuditPage(LotReleaseAuditPageQuery pageQuery) {
        FlowAuditTaskDTO taskDTO = pageQuery.convertAuditTaskDTO();
        if (pageQuery.isExistsCondition()) {
            // 查询模板版本
            List<LotRelease> list = lotReleaseMapper.selectListWithCondition(pageQuery.getProductId(), pageQuery.getTemplateName(), pageQuery.getBatchNo());
            if (CollUtil.isEmpty(list)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery.convertBasePage());
            }
            List<String> instanceIdList = list.stream().map(lr -> String.valueOf(lr.getId())).collect(Collectors.toList());
            taskDTO.setBusinessKeyList(instanceIdList);
        }
        PageQueryResp<List<TaskListResp>> listPageQueryResp = flowAuditService.queryToDoListByCategory(taskDTO);
        Map<String, TaskListResp> map = listPageQueryResp.getData()
                .stream()
                .collect(Collectors.toMap(TaskListResp::getBusinessKey, Function.identity(), (v1, v2) -> v1));
        List<Long> ids = listPageQueryResp.getData().stream()
                .map(TaskListResp::getBusinessKey)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(ids)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery.convertBasePage());
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<LotRelease> list = lotReleaseMapper.selectAuditList(ids);
        CommonPage<LotRelease> result = CommonPage.convertPage(list);
        return LotReleaseConvert.INSTANCE.convertToVersionAuditVO(result, map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditCallback(Long id, Boolean pass, String comment, String auditorId) {
        LotRelease lotRelease = lotReleaseMapper.selectById(id);
        if (lotRelease == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_NOT_EXISTS);
        }
        if (pass == null) {
            // 非最终结果
            // 保存历史记录
        } else if (pass) {
            lotReleaseMapper.updateByTemplateIdAndVersion(lotRelease.getTemplateId(), lotRelease.getPlanId(), LotReleaseStatus.SCRAPED);
            // 审核通过
            lotRelease.setStatus(LotReleaseStatus.EFFECTIVE);
            lotRelease.setEffectTime(LocalDateTime.now());
            lotReleaseMapper.updateById(lotRelease);

            LotReleaseTemplate template = lotReleaseTemplateMapper.selectById(lotRelease.getTemplateId());
            if (template == null) {
                throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_NOT_EXIST);
            }
            // 更新激活的批签发记录
            template.setEffectiveLotReleaseId(lotRelease.getId());
            lotReleaseTemplateMapper.updateById(template);

            lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.PASS, null, lotRelease.getFileUrl()));
        } else {
            // 审核不通过
            lotRelease.setStatus(LotReleaseStatus.EDIT);
            lotReleaseMapper.updateById(lotRelease);

            lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.NOT_PASS, null, lotRelease.getFileUrl()));
        }
    }

    @Override
    public void downloadExcel(HttpServletResponse response, Long id) throws Exception {
        LotRelease lotRelease = lotReleaseMapper.selectById(id);
        if (lotRelease == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_NOT_EXISTS);
        }
        String fileUrl = lotRelease.getFileUrl();
        if (StrUtil.isBlank(fileUrl)) {
            return;
        }
        lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.DOWNLOAD, null, fileUrl));
        minioFileClient.download(MinioBucket.BMOS_LOT_RELEASE, fileUrl, response);
    }

    @Override
    public void downloadByUrl(HttpServletResponse response, String url) throws Exception {
        if (StrUtil.isBlank(url)) {
            return;
        }
        LotRelease lotRelease = lotReleaseMapper.selectByUrl(url);
        if (lotRelease != null){
            lotReleaseHistoryMapper.insert(LotReleaseHistory.create(lotRelease.getId(), LotReleaseOperateType.DOWNLOAD, null, url));
        }
        minioFileClient.download(MinioBucket.BMOS_LOT_RELEASE, url, response);
    }

    @Override
    public Map<String, String> renderTemplate(AssembleCompleteData data) {
        Map<String, String> result = new HashMap<>();
        if (CollectionUtil.isEmpty(data.getRenderTemplateUrl())) {
            return new HashMap<>();
        }
        String emptyPlaceholder = Optional.ofNullable(FeignUtils.handleRequest(code -> businessParameterFeign.detailByCode(code), BusinessParameterCodeConstants.MES_RELEASE_OVER_LEVEL_DATA).getData())
                .map(BusinessParameterDetailFeignVO::getValue)
                .orElse("-");
        String recordEmpty = Optional.ofNullable(FeignUtils.handleRequest(code -> businessParameterFeign.detailByCode(code), BusinessParameterCodeConstants.MES_RECORD_EMPTY_DATA).getData())
                .map(BusinessParameterDetailFeignVO::getValue)
                .orElse("-");
        JSONObject placeholderConfig = Optional.ofNullable(FeignUtils.handleRequest(code -> businessParameterFeign.detailByCode(code), BusinessParameterCodeConstants.MES_ARCHIVE_PHOTOS_REGULAR).getData())
                .map(BusinessParameterDetailFeignVO::getValue)
                .map(JSON::parseObject)
                .orElse(new JSONObject());
        for (RenderTemplateData renderTemplateData : data.getRenderTemplateUrl()) {
            String templateUrl = renderTemplateData.getRenderTemplateUrl();
            String[] fileName = getFileType(templateUrl);
            if (fileName == null) {
                continue;
            }
            String extInfo = renderTemplateData.getExtInfo();
            File tempFile;
            File outTemplateFile;
            try {
                tempFile = File.createTempFile(LOT_RELEASE_FILE_SUFFIX + "_" + fileName[0] + "_" + System.currentTimeMillis(), "." + fileName[1]);

                if (fileName[1].equals("xlsm") || fileName[1].equals("xlsx") || fileName[1].equals("xls")) {
                    minioFileClient.downLoadFile(MinioBucket.BMOS_LOT_RELEASE, templateUrl, Files.newOutputStream(tempFile.toPath()));
                    // 渲染excel
                    Map<String, List<XlsxRenderUtil.CellPosition>> placeHolderMap = XlsxRenderUtil.scanPlaceHolder(tempFile);
                    Map<String, DatasetTransValueData> params = DatasetTrans.getValuesMap(data, placeHolderMap.keySet());
                    Workbook sheets = XlsxRenderUtil.fillValue(tempFile, params, emptyPlaceholder);
                    outTemplateFile = File.createTempFile(UUID.randomUUID().toString(), "." + fileName[1]);
                    sheets.write(Files.newOutputStream(outTemplateFile.toPath()));
                    String path = minioFileClient.uploadFile(MinioBucket.BMOS_LOT_RELEASE, outTemplateFile, "/generate/" + fileName[1] + "/" + tempFile.getName());
                    result.put(extInfo, path);
                } else if (fileName[1].equals("docx") || fileName[1].equals("doc")) {
                    // word
                    minioFileClient.downLoadFile(MinioBucket.BMOS_BATCH_TEMPLATE, templateUrl, Files.newOutputStream(tempFile.toPath()));
                    List<String> placeHolders = DocxRenderUtil.scanPlaceHolder(tempFile);
                    Map<String, DatasetTransValueData> params = DatasetTrans.getValuesMap(data, placeHolders);
                    // 拍照取证数据
                    if (data.getDatasetTransList() == null){
                        data.setDatasetTransList(new ArrayList<>());
                    }
                    List<DocxTakePhotoLegendReplaceOption.TakePhotoData> evidencePhotoList =  buildDataFromDatasetTransList(data.getDatasetTransList());

                    Document document = DocxRenderUtil.fillValue(tempFile, params, placeHolders, emptyPlaceholder, recordEmpty, evidencePhotoList,
                            StringUtils.isBlank(placeholderConfig.getString("evidence_photo")) ? DocxRenderConstants.EVIDENCE_PLACEHOLDER2_DEFAULT : placeholderConfig.getString("evidence_photo"),
                            StringUtils.isBlank(placeholderConfig.getString("take_photo")) ? DocxRenderConstants.TAKE_PHOTO_PLACEHOLDER2_DEFAULT : placeholderConfig.getString("take_photo")
                    );
                    outTemplateFile = File.createTempFile(UUID.randomUUID().toString(), "." + fileName[1]);
                    document.save(outTemplateFile.getAbsolutePath(), SaveFormat.PDF);
                    document.save("/Users/liang/Downloads/output.docx");
                    String path = minioFileClient.uploadFile(MinioBucket.ARCHIVE_BUCKET, outTemplateFile, "/generate/" + fileName[0] + UUID.randomUUID() + ".pdf");
                    result.put(extInfo, path);
                }
            } catch (Exception e) {
                log.error("渲染失败", e);
                throw new RuntimeException("渲染失败");
            }
        }
        return result;
    }

    private List<DocxTakePhotoLegendReplaceOption.TakePhotoData> buildDataFromDatasetTransList(List<DatasetTrans> datasetTransList) {

        Hashtable<String, Integer> indexMap = new Hashtable<>();
        for (DatasetTrans datasetTrans : datasetTransList) {
            Integer index = indexMap.getOrDefault(datasetTrans.getProcessName(), -1);
            indexMap.put(datasetTrans.getProcessName(), index + 1);
        }

        List<DocxTakePhotoLegendReplaceOption.TakePhotoData> list = new ArrayList<>();
        for (int i = 0; i < datasetTransList.size(); i++) {
            DatasetTrans datasetTrans = datasetTransList.get(i);
            ProcessTakePhotoData takePhotoDataList = datasetTrans.getTakePhotoDataList();
            if (takePhotoDataList == null ||  CollectionUtil.isEmpty(takePhotoDataList.getProcedureTakePhotoData())){
                continue;
            }
            for (Map.Entry<String, ChangeNumberPhotoData> entry : takePhotoDataList.getProcedureTakePhotoData().entrySet()) {
                for (Table.Cell<Integer, Integer, List<DocxTakePhotoLegendReplaceOption.TakePhotoData>> integerIntegerListCell : entry.getValue().getPhotoTable()) {
                    for (DocxTakePhotoLegendReplaceOption.TakePhotoData takePhotoData : integerIntegerListCell.getValue()) {
                        DocxTakePhotoLegendReplaceOption.TakePhotoData data = new DocxTakePhotoLegendReplaceOption.TakePhotoData();
                        data.setBatchIndex(String.valueOf(indexMap.get(takePhotoDataList.getProcessName())));
                        data.setProcessName(takePhotoDataList.getProcessName());
                        data.setProcedureName(entry.getKey());
                        data.setProcessChangeNumber(integerIntegerListCell.getRowKey());
                        data.setProcedureChangeNumber(integerIntegerListCell.getColumnKey());
                        data.setImageUrl(takePhotoData.getImageUrl());
                        data.setCreator(takePhotoData.getCreator());
                        data.setTime(takePhotoData.getTime());
                        data.setRemark(takePhotoData.getRemark());
                        list.add(data);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<LotReleaseDynamicReportItemVO> getDynamicReportItem(LotReleaseQueryDynamicReportDTO dto) {
        LotReleaseTemplateVersion version = lotReleaseTemplateVersionMapper.selectByTemplateIdAndVersion(dto.getLotReleaseTemplateId(), dto.getLotReleaseVersion());
        if (version == null) {
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }
        List<DatasetPointTemplateRelation> list = datasetPointTemplateRelationMapper.selectByTemplateUrl(version.getTemplateUrl());
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<String> dpNoList = list.stream().map(DatasetPointTemplateRelation::getDatasetPointKeys)
                .map(json -> JSON.parseArray(json, String.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return datasetPointMapper.selectByDatasetPointKeys(dpNoList)
                .stream()
                .filter(dp -> Objects.equals(DatasetType.DYNAMIC_REPORT, dp.getType()))
                .map(dp -> {
                    LotReleaseDynamicReportItemVO item = new LotReleaseDynamicReportItemVO();
                    item.setDynamicDataType(dp.getDynamicDataType());
                    item.setDefaultValue(dp.getDefaultValue());
                    item.setName(dp.getName());
                    item.setDatasetPointKey(dp.getDatasetPointKey());
                    item.setDatasetKey(dp.getDatasetKey());
                    return item;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LogReleaseHistoryVO> showHistory(Long id) {
        List<LotReleaseHistory> histories = lotReleaseHistoryMapper.queryHistoryById(id);
        return LotReleaseConvert.INSTANCE.convertToHistoryVO(histories);
    }

    @Override
    public List<String> selectAuditBusinessKey(List<Long> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)){
            return new ArrayList<>();
        }
        Page<Object> localPage = PageHelper.getLocalPage();
        String orderBy = null;
        if (localPage != null) {
            orderBy = localPage.getOrderBy();
        }
        PageHelper.clearPage();
        List<Long> templateIdList = lotReleaseTemplateMapper.selectAuthByDeptIdList(deptIdList);
        if (CollectionUtil.isEmpty(templateIdList)){
            return new ArrayList<>();
        }
        if (orderBy != null) {
            PageHelper.orderBy(orderBy);
        }
        List<LotRelease> lotReleases = lotReleaseMapper.selectByTemplateIdList(templateIdList);
        if (CollectionUtil.isEmpty(lotReleases)){
            return new ArrayList<>();
        }
        return lotReleases.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
    }

    @Override
    public LotRelease selectOneById(Long businessId) {
        return lotReleaseMapper.selectById(businessId);
    }


    private ProductCategoryTreeNodeVO findTree(List<ProductCategoryTreeNodeVO> tree, Long id) {
        for (ProductCategoryTreeNodeVO node : tree) {
            if (Objects.equals(node.getId(), id)) {
                return node;
            }
            ProductCategoryTreeNodeVO findTree = findTree(node.getChildren(), id);
            if (Objects.nonNull(findTree)) {
                return findTree;
            }
        }
        return null;
    }

    private List<ProductCategoryTreeNodeVO> getAllChildren(List<ProductCategoryTreeNodeVO> tree) {
        List<ProductCategoryTreeNodeVO> children = tree.stream()
                .flatMap(node -> CollectionUtil.isNotEmpty(node.getChildren())
                        ? getAllChildren(node.getChildren()).stream()
                        : node.getChildren().stream())
                .collect(Collectors.toList());
        children.addAll(tree);
        return children;
    }

    /**
     * 查询前置条件(产品)
     *
     * @param productId         产品id
     * @param productCategoryId 产品分类id
     * @return 分类下所有的产品/某个产品信息
     */
    private List<ProductCategoryTreeNodeVO> getProductListCondition(Long productId, Long productCategoryId) {
        List<ProductCategoryTreeNodeVO> productTree = productMaterialService.getProductTree(CategoryInfoTypeEnum.PRODUCTION.getValue());
        List<ProductCategoryTreeNodeVO> productList = new ArrayList<>();
        if (productId != null) {
            ProductCategoryTreeNodeVO product = findTree(productTree, productId);
            productList.add(product);
        } else if (productCategoryId != null) {
            ProductCategoryTreeNodeVO categoryTree = findTree(productTree, productCategoryId);
            productList = getAllChildren(Collections.singletonList(categoryTree));
        } else {
            productList = getAllChildren(productTree);
        }
        return productList;
    }

    private String[] getFileType(String templateUrl) {
        if (StrUtil.isBlank(templateUrl)) {
            return null;
        }
        String baseName = FilenameUtils.getBaseName(templateUrl);
        String extension = FilenameUtils.getExtension(templateUrl);
        return new String[]{baseName, extension};
    }

    private String getLotReleaseSerial(String batchNo) {
        if (StrUtil.isBlank(batchNo)) {
            throw new ValidationException("批次号不能为空");
        }
        Map<String, String> params = new HashMap<>();
        params.put("batchNo", batchNo);
        return FeignUtils.handleRequest(data -> platformCodeFeign.getNextUseNo(data), NextUseCodeDTO.builder()

                        .code(PlatformCodeConstants.LOT_RELEASE_SERIAL)
                        .fields(params)
                        .build())
                .getData().getNo();
    }

    private void confirmLotReleaseSerial(String serial, String batchNo) {
        Map<String, String> params = new HashMap<>();
        params.put("batchNo", batchNo);
        FeignUtils.handleRequest(data -> platformCodeFeign.confirmNo(data), ConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.LOT_RELEASE_SERIAL)
                .fullNo(serial)
                .fields(params)
                .build());
    }
}
