package com.bmos.mes.service.plan.info.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.plan.PlanArchiveStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.utils.Graph;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.audit.vo.AuditCategoryCountVO;
import com.bmos.mes.service.config.minio.MinioProperties;
import com.bmos.mes.service.formula.dto.StorageMaterialReservedQuantityDTO;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.formula.vo.PlanReservedMaterialQuantityInfoVO;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.plan.info.constant.PlanConstant;
import com.bmos.mes.service.plan.info.convert.PlanConverter;
import com.bmos.mes.service.plan.info.dto.*;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.mapper.ProductPlanNoInfoMapper;
import com.bmos.mes.service.plan.info.mapper.ProductPlanRelationMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanNoInfo;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.mq.message.PlanStatusChangeMessage;
import com.bmos.mes.service.plan.info.mq.topic.PlanStatusChangeTopic;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mes.service.plan.instruction.service.InstructionService;
import com.bmos.mes.service.plan.production.constant.ProductionPlanConstant;
import com.bmos.mes.service.plan.production.mapper.ProductionPlanItemMapper;
import com.bmos.mes.service.plan.production.model.ProductionPlanItem;
import com.bmos.mes.service.plan.production.vo.ProcedureDetailVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.platform.plan.impl.PlatformCodeRuleClientImpl;
import com.bmos.mes.service.process.mapper.task.ProcedureTaskInstanceHistoryMapper;
import com.bmos.mes.service.process.mapper.task.ProcedureTaskInstanceMapper;
import com.bmos.mes.service.product.service.MaterialFieldService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.MaterialFieldInfoVO;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.storage.manage.dto.BatchReservedMaterialQueryDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import com.bmos.mes.service.utils.BigDecimalFormatUtil;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.utils.PlanArchivePathUtil;
import com.bmos.mes.service.workflow.dto.query.WorkflowTodoPageDTO;
import com.bmos.mes.service.workflow.dto.AppPlanHistoryDTO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.code.dto.BatchConfirmByCodeDTO;
import com.bmos.platform.facade.code.dto.ConfirmNoInfoDTO;
import com.bmos.platform.facade.code.dto.ReleaseConfirmedNoDTO;
import com.bmos.platform.facade.code.feign.CodeRuleFeign;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
    @Value("${parameter.product.plan.discard.code}")
    private String productPlanDiscardCode;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private PlatformCodeRuleClientImpl platformCodeRuleClientImpl;
    @Autowired
    private PlatformParameterClientImpl platformParameterClientImpl;
    @Autowired
    private FlowAuditService flowAuditService;
    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;
    @Autowired
    private ProductMaterialService productMaterialService;
    @Autowired
    @Lazy
    private InstructionService instructionService;

    @Autowired
    private OperationHistoryService logService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private ProductFormulaMaterialMapper productFormulaMaterialMapper;

    @Resource
    private IStorageMaterialReserveMapper storageMaterialReserveMapper;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private ProductPlanRelationService productPlanRelationService;

    @Resource
    private PlanStatusChangeTopic planStatusChangeTopic;
    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private ProcedureTaskInstanceMapper procedureTaskInstanceMapper;
    @Autowired
    private ProcedureTaskInstanceHistoryMapper procedureTaskInstanceHistoryMapper;
    @Autowired
    private ProductPlanRelationMapper productPlanRelationMapper;

    @Resource
    private ProductPlanNoInfoMapper planNoInfoMapper;

    @Resource
    private CodeRuleFeign codeRuleFeign;
    @Resource
    private ProductionPlanItemMapper planItemMapper;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private MaterialFieldService materialFieldService;


    @Override
    public List<PlanPageVO> page(PlanPageDTO dto) {
        //查询数据权限，使用工艺数据权限控制(管理员可以看到所有)
        if(!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> planIdS = selectPlanIdByDeptId();
            if (CollUtil.isEmpty(planIdS)){
                return Collections.emptyList();
            }
            dto.setPlanIds(planIdS);
        }
        if (Objects.nonNull(dto.getProductCategoryId())){
            dto.setProductIds(productMaterialService.getProductIdList(CategoryInfoTypeEnum.PRODUCTION,
                    dto.getProductCategoryId(), null));
            if (CollUtil.isEmpty(dto.getProductIds())) {
                return new ArrayList<>();
            }
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<PlanPageVO> page = planMapper.page(dto);
        page.forEach(p -> {
            p.setUnitName(unitCache.getGlobalUnitName(p.getUnitId()));
            p.setBatchQuantity(BigDecimalFormatUtil.formatBigDecimal(p.getBatchQuantityDecimal()));
        });
        return page;
    }

    private List<Long> selectPlanIdByDeptId(){
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new ArrayList<>();
        }
        List<Plan> planList = planMapper.getAuditBusinessKey(deptIds);
        if (CollUtil.isEmpty(planList)) {
            return Collections.emptyList();
        }
        return CollectionUtils.convertList(planList,Plan::getId);
    }

    @Override
    public List<AuditCategoryCountVO> waitTaskCount() {
        Map<String, Integer> flowAuditMap = flowAuditService
                .getAuditCategoryToDoCount(SysUserHolder.getUser().getUserId())
                .stream()
                .collect(
                        Collectors.toMap(AuditCategoryCountVO::getCategoryCode, AuditCategoryCountVO::getNumber, (t1,
                                                                                                                  t2) -> t1)
                );
        List<AuditCategoryCountVO> results = Arrays.stream(AuditCategoryCodeEnum.values())
                .map(auditCategoryCodeEnum -> AuditCategoryCountVO.builder()
                        .categoryCode(auditCategoryCodeEnum.getMenuCode())
                        .number(flowAuditMap.getOrDefault(auditCategoryCodeEnum.getCode(), 0))
                        .build())
                .collect(Collectors.toList());
        // 查询生产计划待确认数量
        results.add(
                AuditCategoryCountVO.builder()
                        .categoryCode("120030004") // 指令单确认菜单编码
                        .number(instructionService.waitTaskCount(SysUserHolder.getUser().getUserId()))
                        .build()
        );
        return results.stream().filter(result -> result.getNumber() > 0).collect(Collectors.toList());
    }

    @Override
    public CommonPage<PlanPageVO> pageTraceable(PlanTraceablePageDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> deptIds = platformApiAdaptor.getMineDeptIds();
            if (CollUtil.isEmpty(deptIds)) {
                return CommonPage.convertPage(PageInfo.emptyPageInfo());
            }
            dto.setDeptIds(deptIds);
        }
        dto.setProductCategoryId(CollUtil.isEmpty(dto.getProductIds()) ?
                Optional.ofNullable(dto.getProductCategoryId()).orElse(TreeUtil.parentId) : dto.getProductCategoryId());
        if (ObjectUtil.isNotNull(dto.getProductCategoryId())) {
            dto.setProductIds(productMaterialService.getProductIdList(CategoryInfoTypeEnum.PRODUCTION,
                    dto.getProductCategoryId(), dto.getFinishedProduct()));
            if (CollUtil.isEmpty(dto.getProductIds())) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
            }
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        CommonPage<PlanPageVO> planPageVOCommonPage = CommonPage.convertPage(planMapper.pageTraceable(dto));
        // 归档地址
        if (!CollUtil.isEmpty(planPageVOCommonPage.getList())) {
            List<FactoryLineDetailFeignVO> lines = FeignUtils.handleRequest(list -> factoryFeign.getLineDetailByLineIds(list, true), CollectionUtils.convertList(planPageVOCommonPage.getList(), PlanPageVO::getProductionLineId)).getData();
            Map<Long, FactoryLineDetailFeignVO> lineMap = CollectionUtils.convertMap(lines, FactoryLineDetailFeignVO::getId);
            planPageVOCommonPage.getList().forEach(planPageVO -> {
                Optional.ofNullable(planPageVO.getProductionLineId())
                        .map(lineMap::get)
                        .ifPresent(line -> {
                            planPageVO.setProductionLineCode(line.getCode());
                            planPageVO.setProductionLineName(line.getName());
                        });

            });
            this.setArchiveFileUrl(planPageVOCommonPage.getList());
        }
        return planPageVOCommonPage;
    }


    private void setArchiveFileUrl(List<PlanPageVO> list) {
        list.forEach(planPageVO -> {
            if (PlanArchiveStatusEnum.ARCHIVE_SUCCESS == planPageVO.getArchiveStatus()) {
                planPageVO.setArchiveFileUrl(PlanArchivePathUtil.getPlanMinioCompleteFilePath(minioProperties.getBuckets().getArchive(),
                        planPageVO.getId()));
            }
        });
    }

    @Override
    public CommonPage<PlanAuditPageVO> auditPage(PlanAuditPageDTO dto) {
        FlowAuditTaskDTO flowAuditTaskDTO = dto.convertAuditTaskDTO();
        if (dto.isExistsSearchCondition()) {
            List<Long> longs = planMapper.selectAudit(dto);
            if (CollUtil.isEmpty(longs)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, dto.convertBasePage());
            }
            flowAuditTaskDTO.setBusinessKeyList(CollectionUtils.convertList(longs, String::valueOf));
        }
        // 查询代办列表
        PageQueryResp<List<TaskListResp>> taskListResps = flowAuditService.queryToDoListByCategory(flowAuditTaskDTO);
        if (CollUtil.isEmpty(taskListResps.getData())) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto.convertBasePage());
        }
        List<String> processInstanceIds = taskListResps.getData()
                .stream()
                .map(TaskListResp::getProcessInstanceId)
                .collect(Collectors.toList());
        // 根据实例id查询生产计划信息
        Map<String, Plan> planMapKeyIsProcessInstanceId = planMapper.selectByProcessInstanceIds(processInstanceIds)
                .stream().collect(Collectors.toMap(Plan::getProcessInstanceId, Function.identity()));
        List<PlanAuditPageVO> records = PlanConverter.INSTANCE.convertList(taskListResps.getData(),
                planMapKeyIsProcessInstanceId);
        records.forEach(record -> record.setUnitName(unitCache.getGlobalUnitName(record.getUnitId())));
        return CommonPage.CommonPage(records, taskListResps.getTotal(), dto.convertBasePage());
    }

    @Override
    public List<Plan> productManagePage(PlanPageDTO dto) {
        if (ObjectUtil.isNotNull(dto.getProductCategoryId())) {
            List<Long> productIdList = productMaterialService.getProductIdList(CategoryInfoTypeEnum.PRODUCTION,
                    dto.getProductCategoryId(), null);
            if (CollUtil.isEmpty(productIdList)) {
                return Collections.emptyList();
            }
            dto.setProductIds(productIdList);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return planMapper.productManagePage(dto);
    }

    @Override
    public List<PlanStartPageVO> startPage(PlanStartPageDTO dto) {
        return planMapper.startPage(dto);
    }

    @Override
    public PlanDetailVO detail(Long id) {
        Plan plan = planMapper.selectById(id);
        ProductionPlanItem productionPlanItem = planItemMapper.selectById(plan.getProductionPlanItemId());
        PlanDetailVO result = PlanConverter.INSTANCE.convertVO(plan);
        if (Objects.nonNull(result.getUnitId())){
            result.setUnitName(unitCache.getGlobalUnitName(result.getUnitId()));
        }
        if (productionPlanItem != null) {
            result.setProcedureList(JsonUtils.parseArray(productionPlanItem.getProcedureList(), ProcedureDetailVO.class));
        }
        ResponseInfo<List<FactoryLineFeignVO>> listResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.queryLineListByLineIds(data), Lists.newArrayList(plan.getProductionLineId()));
        if (!listResponseInfo.isSuccess()){
            return result;
        }
        List<FactoryLineFeignVO> factoryLineFeignVOS = listResponseInfo.getData();
        if (CollUtil.isEmpty(factoryLineFeignVOS)){
            return result;
        }
        result.setProductionLineCode(factoryLineFeignVOS.get(0).getCode());
        result.setProductionLineName(factoryLineFeignVOS.get(0).getName());
        return result;
    }

    private static List<ProductPlanRelation> convertToProductPlanRelation(List<ProductPlanRelationDTO> relationPlanList, Long planId) {
        return relationPlanList.stream().map(detail ->
                detail.getPlanIds().stream().map(relationPlanId -> ProductPlanRelation.builder()
                .productPlanId(planId)
                .relationProductPlanId(relationPlanId)
                .processId(detail.getProcessId())
                .isDirectRelation(BooleanEnum.TRUE)
                .build()
        ).collect(Collectors.toList())).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_PLAN, operationType = OperationType.REDACT, businessId = "#dto" +
            ".id")
    public void update(PlanUpdateDTO dto) {
        Plan plan = planMapper.selectById(dto.getId());
        if (ProductPlanStatusEnum.EDIT != plan.getStatus()) {
            throw new BmosException(MesResponseCode.EDIT_STATUS_CAN_EDIT);
        }
        // 处理编号
        handlePlanNoAndBatchNo(dto, plan);
        planMapper.updateById(PlanConverter.INSTANCE.convertDO(dto));
        List<ProductPlanRelation> relations = convertToProductPlanRelation(dto.getRelationPlanList(), plan.getId());
        productPlanRelationService.updateProductPlanRelation(relations, plan.getId());
    }

    private void handlePlanNoAndBatchNo(PlanUpdateDTO dto, Plan plan) {
        boolean batchNoChange = !Objects.equals(plan.getBatchNo(), dto.getBatchNo()) && StrUtil.isNotEmpty(dto.getBatchNoCode());
        boolean planNoChange = !Objects.equals(plan.getPlanNo(), dto.getPlanNo()) && StrUtil.isNotEmpty(dto.getPlanNoCode());
        if (!batchNoChange && !planNoChange) {
            return;
        }
        // 若修改了编号则释放原有的编号
        ProductPlanNoInfo noInfo = planNoInfoMapper.selectByProductPlanId(plan.getId());
        releasePlanNoAndBatchNo(noInfo, batchNoChange, planNoChange, dto);
        // 记录更新生产指令单和编号的关系
        if (noInfo == null) {
            saveProductNoInfo(dto, plan);
        } else {
            if (batchNoChange) {
                noInfo.setPlanNoCode(dto.getPlanNoCode());
                noInfo.setPlanNo(dto.getPlanNo());
            }
            if (planNoChange) {
                noInfo.setBatchNoCode(dto.getBatchNoCode());
                noInfo.setBatchNo(dto.getBatchNo());
            }
            planNoInfoMapper.updateById(noInfo);
        }
    }

    private void saveProductNoInfo(PlanUpdateDTO dto, Plan plan) {
        ProductPlanNoInfo insert = new ProductPlanNoInfo();
        insert.setProductPlanId(plan.getId());
        insert.setFields(JsonUtils.toJsonString(dto));
        insert.setPlanNoCode(dto.getPlanNoCode());
        insert.setPlanNo(dto.getPlanNo());
        insert.setBatchNoCode(dto.getBatchNoCode());
        insert.setBatchNo(dto.getBatchNo());
        planNoInfoMapper.insert(insert);
    }

    private void releasePlanNoAndBatchNo(ProductPlanNoInfo noInfo, boolean batchNoChange,
                                         boolean planNoChange, PlanUpdateDTO dto) {
        ProductPlanNoInfo productPlanNoInfo = new ProductPlanNoInfo();
        BeanUtil.copyProperties(noInfo, productPlanNoInfo);
        if (noInfo == null) {
            productPlanNoInfo.setPlanNoCode(dto.getPlanNoCode());
            productPlanNoInfo.setPlanNo(dto.getPlanNo());
            productPlanNoInfo.setBatchNo(dto.getBatchNo());
            productPlanNoInfo.setBatchNoCode(dto.getBatchNoCode());
            productPlanNoInfo.setFields(JsonUtils.toJsonString(dto));
        }
        if (batchNoChange) {
            ReleaseConfirmedNoDTO build = ReleaseConfirmedNoDTO.builder().no(productPlanNoInfo.getBatchNo())
                    .code(productPlanNoInfo.getBatchNoCode())
                    .fields(JsonUtils.parseObject(productPlanNoInfo.getFields(), HashMap.class)).build();
            codeRuleFeign.releaseConfirmedNO(build);
        }
        if (planNoChange) {
            ReleaseConfirmedNoDTO.ReleaseConfirmedNoDTOBuilder builder = ReleaseConfirmedNoDTO.builder();
            ReleaseConfirmedNoDTO build = builder.no(productPlanNoInfo.getPlanNo())
                    .code(productPlanNoInfo.getPlanNoCode())
                    .fields(JsonUtils.parseObject(productPlanNoInfo.getFields(), HashMap.class)).build();
            codeRuleFeign.releaseConfirmedNO(build);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_PLAN, operationType = OperationType.NULLIFY, businessId = "#id")
    public void discard(Long id) {
        Plan plan = planMapper.selectById(id);
        if (ProductPlanStatusEnum.EDIT != plan.getStatus() && ProductPlanStatusEnum.CONFIRM != plan.getStatus()) {
            throw new BmosException(MesResponseCode.EDIT_AND_CONFIRM_STATUS_CAN_DISCARD);
        }
        if (ProductPlanStartEnum.WAIT != plan.getStart() && ProductPlanStartEnum.TERMINATION != plan.getStart()) {
            throw new BmosException(MesResponseCode.START_PRODUCT_NOT_DISCARD);
        }
        String valueByCode = platformParameterClientImpl.getValueByCode(productPlanDiscardCode);
        try {
            planMapper.discard(id, getDiscardFiled(plan.getPlanNo(), valueByCode), getDiscardFiled(plan.getBatchNo(),
                    valueByCode));
            // 释放掉当前的批号和计划编号
            releaseBatchNoAndPlanNo(id);
        } catch (DuplicateKeyException exception) {
            // 校验编号和批号在同一工序下是否重复
            throw new BmosException(PlanConstant.findException(exception));
        }
    }

    private void releaseBatchNoAndPlanNo(Long id) {
        ProductPlanNoInfo noInfo = planNoInfoMapper.selectByProductPlanId(id);
        if (noInfo == null) {
            return;
        }
        HashMap<String, Object> originalMap = JsonUtils.parseObject(noInfo.getFields(), HashMap.class);
        HashMap<String, String> stringMap = getStringMap(originalMap);
        if (StrUtil.isNotBlank(noInfo.getPlanNoCode()) && StrUtil.isNotBlank(noInfo.getPlanNo())) {
            codeRuleFeign.releaseConfirmedNO(ReleaseConfirmedNoDTO.builder()
                    .no(noInfo.getPlanNo())
                    .code(noInfo.getPlanNoCode())
                    .fields(stringMap)
                    .build());
        }
        if (StrUtil.isNotBlank(noInfo.getBatchNoCode()) && StrUtil.isNotBlank(noInfo.getBatchNo())) {
            codeRuleFeign.releaseConfirmedNO(ReleaseConfirmedNoDTO.builder()
                    .no(noInfo.getBatchNo())
                    .code(noInfo.getBatchNoCode())
                    .fields(stringMap)
                    .build());
        }
    }

    @NotNull
    private HashMap<String, String> getStringMap(HashMap<String, Object> originalMap) {
        HashMap<String, String> stringMap = originalMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Object value = entry.getValue();
                            if (value == null) {
                                return StrUtil.EMPTY;
                            }
                            if (value instanceof List) {
                                // ArrayList 转换为逗号分隔的字符串
                                return ((List<?>) value).stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(","));
                            }
                            return value.toString(); // 其他类型转换为字符串
                        },
                        (oldValue, newValue) -> oldValue, // 处理键冲突的情况
                        HashMap::new // 返回 HashMap
                ));
        return stringMap;
    }

    private String getDiscardFiled(String no, String valueByCode) {
        return no + valueByCode + LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.createFormatter(
                "yyyyMMddHHmm"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_PLAN, operationType = OperationType.SUBMIT_AUDIT, businessId = "#id")
    public void approve(Long id) {
        Plan plan = planMapper.selectById(id);
        if (ProductPlanStatusEnum.EDIT != plan.getStatus()) {
            throw new BmosException(MesResponseCode.EDIT_STATUS_CAN_AUDIT);
        }
        FlowStartDTO flowStartDTO = new FlowStartDTO();
        flowStartDTO.setBusinessKey(id.toString());
        flowStartDTO.setName(plan.getProductName());
        flowStartDTO.setCode(AuditCategoryCodeEnum.PRODUCT_PLAN.getCode());
        flowStartDTO.setCategoryCode(AuditCategoryCodeEnum.PRODUCT_PLAN.getCode());
        flowStartDTO.setExtField(plan.getBatchNo());
        planMapper.approve(id, flowAuditService.flowAuditStart(flowStartDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditSuccess(String processInstanceId) {
        planMapper.auditSuccess(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_PLAN, operationType = OperationType.REJECT_AUDIT, businessId =
            "#businessKey",
            remark = "#remark", nodeName = "#nodeName", comment = "#comment")
    public void auditTermination(String remark, String processInstanceId, String nodeName, String comment,
                                 Long businessKey) {
        planMapper.auditTermination(processInstanceId);
    }

    @Override
    public void auditPlanLog(String businessKey, String remark, String userId, String nodeName, String comment) {
        saveRecordExecutionHistoryLog(remark, userId, Long.valueOf(businessKey), nodeName, comment);
    }

    private void saveRecordExecutionHistoryLog(String remark, String userId, Long businessId, String nodeName,
                                               String comment) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.PRODUCT_PLAN.name())
                .businessId(businessId)
                .operationType(OperationType.APPROVE_AUDIT.getValue())
                .remark(remark)
                .comment(comment)
                .nodeName(nodeName)
                .createBy(userId)
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeCallBackSuccess(String executeProcessInstanceId) {
        Plan plan = planMapper.selectByExecuteProcessInstanceId(executeProcessInstanceId);
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        planMapper.executeCallBackSuccess(executeProcessInstanceId);
        // 批生产结束物料件自动取消预定
        storageMaterialService.cancelReserveByProductPlanId(plan.getId());
        planStatusChangeTopic.product(PlanStatusChangeMessage.builder()
                .currentPlanStatus(ProductPlanStartEnum.END).plan(plan).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeCallBackTermination(String executeProcessInstanceId) {
        Plan plan = planMapper.selectByExecuteProcessInstanceId(executeProcessInstanceId);
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        planMapper.executeCallBackTermination(executeProcessInstanceId);
        // 批生产结束物料件自动取消预定
        storageMaterialService.cancelReserveByProductPlanId(plan.getId());
        planStatusChangeTopic.product(PlanStatusChangeMessage.builder()
                .currentPlanStatus(ProductPlanStartEnum.TERMINATION).plan(plan).build());
    }

    @Override
    public List<Plan> selectByProcessInstanceIds(List<String> processInstanceIds) {
        return planMapper.selectByProcessInstanceIds(processInstanceIds);
    }

    @Override
    public Plan getById(Long id) {
        return planMapper.selectById(id);
    }

    @Override
    public List<String> getAuditBusinessKey(List<Long> deptIdList) {
        List<Plan> plans = CollectionUtils.filterList(planMapper.getAuditBusinessKey(deptIdList), plan ->
                StrUtil.equals(plan.getStatus().getValue(), ProductPlanStatusEnum.AUDIT.getValue()));
        if (CollUtil.isEmpty(plans)){
            return Collections.emptyList();
        }
        return plans.stream().map(item-> String.valueOf(item.getId())).collect(Collectors.toList());
    }

    @Override
    public void pauseExecute(Long id) {
        Plan plan = planMapper.selectById(id);
        if (ObjectUtil.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        plan.setExecutePaused(true);
        planMapper.updateById(plan);
    }

    @Override
    public void recoveryExecute(Long id) {
        Plan plan = planMapper.selectById(id);
        if (ObjectUtil.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        plan.setExecutePaused(false);
        planMapper.updateById(plan);
    }

    @Override
    public List<PlanEasyInfoVO> batchListByPlanStart() {
        List<Plan> planList = planMapper.batchListByPlanStart();
        return PlanConverter.INSTANCE.convert2EasyVO(planList);
    }

    @Override
    @Deprecated
    public PlanReservedMaterialQuantityInfoVO queryPlanReservedStorageMaterialQuantity(StorageMaterialReservedQuantityDTO dto) {
        Long formulaMaterialId = dto.getFormulaMaterialId();
        ProductFormulaMaterial formulaMaterial = productFormulaMaterialMapper.selectById(formulaMaterialId);
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }
        ProductFormulaVersion productFormulaVersion = productFormulaConfigureService.getVersionById(formulaMaterial.getVersionId());
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        PlanReservedMaterialQuantityInfoVO result = new PlanReservedMaterialQuantityInfoVO();
        BatchReservedMaterialQueryDTO queryDTO = new BatchReservedMaterialQueryDTO();
        queryDTO.setProductPlanId(dto.getProductPlanId());
        queryDTO.setMaterialId(formulaMaterial.getMaterialId());
        List<BatchReservedMaterialVO> reservedMaterialVOS =
                storageMaterialReserveMapper.queryBatchReservedMaterial(queryDTO);
        List<BatchReservedMaterialVO> filter =
                reservedMaterialVOS.stream().filter(BatchReservedMaterialVO::isAvailable).collect(Collectors.toList());
        filter.forEach(e -> {
            BigDecimal quantity = MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getReserveQuantity(),
                    formulaMaterial.getUnitId()),
                    formulaMaterial);
            e.setQuantity(quantity);
            e.setReserveQuantity(quantity);
            // 理论量
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(e.getReserveQuantity(),
                    e.getHydration(), e.getNoHydrationContent(), formulaMaterial));
        });
        BigDecimal reduce =
                filter.stream().map(BatchReservedMaterialVO::getTheoreticalQuantity).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        result.setReservedQuantity(reduce);
        formulaMaterial.setQuantity(dto.getCheckQuantity());
        BigDecimal calculateQuantity = MaterialQuantityCalculateUtil.calculateQuantity(plan.getBatchQuantity(),
                productFormulaVersion.getBatchQuantity(), formulaMaterial);
        result.setPlanNeedQuantity(calculateQuantity);
        return result;
    }

    @Override
    public List<PlanReservedMaterialQuantityInfoVO> queryPlanReservedStorageMaterialQuantity(Long productPlanId,
                                                                                             List<StorageMaterialReservedQuantityDTO> formulaMaterialList) {
        ArrayList<PlanReservedMaterialQuantityInfoVO> result = new ArrayList<>();
        if (CollUtil.isEmpty(formulaMaterialList)) {
            return result;
        }
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(productPlanId);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = formulaInfo.getMaterialMap();
        List<Long> materialIdList = new ArrayList<>();
        for (StorageMaterialReservedQuantityDTO materialDto : formulaMaterialList) {
            Long formulaMaterialId = materialDto.getFormulaMaterialId();
            PlanReservedMaterialQuantityInfoVO vo = new PlanReservedMaterialQuantityInfoVO();
            ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(formulaMaterialId);
            if (formulaMaterial == null) {
                throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
            }
            materialIdList.add(formulaMaterial.getMaterialId());
            formulaMaterial.setQuantity(materialDto.getCheckQuantity());
            vo.setPlanNeedQuantity(MaterialQuantityCalculateUtil.calculateQuantity(plan.getBatchQuantity(),
                    formulaInfo.getBatchQuantity(), formulaMaterial));
            vo.setMaterialId(formulaMaterial.getMaterialId());
            vo.setFormulaMaterialId(formulaMaterialId);
            result.add(vo);
        }
        // 查询已预订暂存物料
        List<BatchReservedMaterialVO> reservedMaterialVOS =
                storageMaterialReserveMapper.queryBatchReservedMaterialByMaterialIds(productPlanId,
                        materialIdList);
        List<BatchReservedMaterialVO> filter =
                reservedMaterialVOS.stream().filter(BatchReservedMaterialVO::isAvailable).collect(Collectors.toList());
        Map<Long, ProductFormulaMaterial> materialIdMap = formulaInfo.getMaterialIdMap();
        // 根据配方修约与水分含量计算理论量
        filter.forEach(e -> {
            ProductFormulaMaterial formulaMaterial = materialIdMap.get(e.getMaterialId());
            BigDecimal quantity = MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getReserveQuantity(),
                    formulaMaterial.getUnitId()),
                    formulaMaterial);
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(quantity,
                    e.getHydration(), e.getNoHydrationContent(), formulaMaterial));
        });
        Map<Long, List<BatchReservedMaterialVO>> materialMap = CollectionUtils.convertMultiMap(filter,
                BatchReservedMaterialVO::getMaterialId);
        for (PlanReservedMaterialQuantityInfoVO vo : result) {
            List<BatchReservedMaterialVO> reserved = materialMap.get(vo.getMaterialId());
            if (CollUtil.isNotEmpty(reserved)) {
                vo.setReservedQuantity(reserved.stream().map(BatchReservedMaterialVO::getTheoreticalQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
            } else {
                vo.setReservedQuantity(BigDecimal.ZERO);
            }
        }
        return result;
    }

    @Override
    public List<PlanEasyInfoVO> startPlanList(PlanStartQueryDTO dto) {
        List<Plan> planList = planMapper.selectByStartList(dto);
        return PlanConverter.INSTANCE.convert2EasyVO(planList);
    }

    @Override
    public Plan selectByExecuteProcessInstanceId(String processInstanceId) {
        return planMapper.selectByExecuteProcessInstanceId(processInstanceId);
    }

    @Override
    public List<Plan> productManagePageHistory(AppPlanHistoryDTO pageDTO) {
        return planMapper.productManagePageHistory(pageDTO);
    }

    @Override
    public List<PlanSimpleVO> queryBatchListByProductIdAndProcessId(Long productId, Long processId) {
        List<Plan> plans = planMapper.queryBatchListByProductIdAndProcessId(productId, processId);
        if (CollectionUtil.isEmpty(plans)){
            return new ArrayList<>();
        }
        return PlanConverter.INSTANCE.convertToSimpleVO(plans);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveBatch(PlanApproveBatchDTO dto) {
        List<Plan> plans = planMapper.selectBatchIds(dto.getPlanIdList());
        boolean hasNotEdit = plans.stream().anyMatch(e -> e.getStatus() != ProductPlanStatusEnum.EDIT);
        if (hasNotEdit) {
            throw new BmosException(MesResponseCode.EDIT_STATUS_CAN_AUDIT);
        }
        for (Plan plan : plans) {
            FlowStartDTO flowStartDTO = new FlowStartDTO();
            flowStartDTO.setBusinessKey(plan.getId().toString());
            flowStartDTO.setName(plan.getProductName());
            flowStartDTO.setCode(AuditCategoryCodeEnum.PRODUCT_PLAN.getCode());
            flowStartDTO.setCategoryCode(AuditCategoryCodeEnum.PRODUCT_PLAN.getCode());
            flowStartDTO.setExtField(plan.getBatchNo());
            planMapper.approve(plan.getId(), flowAuditService.flowAuditStart(flowStartDTO));
        }
    }

    @Override
    public List<Plan> selectByProcessIdList(List<Long> processIdList, List<String> planStartEnumList) {
        return planMapper.selectByProcessIdList(processIdList, planStartEnumList);
    }

    @Override
    public List<Plan> getByIds(Collection<Long> idList) {
        if (CollUtil.isEmpty(idList)){
            return new ArrayList<>();
        }
        return planMapper.selectBatchIds(idList);
    }


    @Override
    public List<PlanListVO> queryPlanListByProcess(PlanListByProcessDTO dto) {
        return PlanConverter.INSTANCE.convert2PlanListVO(planMapper.selectByProcessInfo(dto));
    }

    @Override
    public CommonPage<ProductionAuditProgressPageVO> queryProductionAuditProgressPage(ProductionAuditProgressQueryDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> deptIds = platformApiAdaptor.getMineDeptIds();
            if (CollUtil.isEmpty(deptIds)) {
                return CommonPage.convertPage(PageInfo.emptyPageInfo());
            }
            dto.setDeptIds(deptIds);
        }
        if (BooleanUtil.isTrue(dto.getCategoryFlag())) {
            dto.setProductIds(productMaterialService.getProductIdList(CategoryInfoTypeEnum.PRODUCTION,
                    dto.getId(), null));
            if (CollUtil.isEmpty(dto.getProductIds())) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
            }
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
            List<ProductionAuditProgressPageVO> result = planMapper.selectProductionAuditProgressPage(dto);
            handleAuditProgress(result);
            return CommonPage.convertPage(result);
        }
        dto.setProductIds(Collections.singletonList(dto.getId()));
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<ProductionAuditProgressPageVO> result = planMapper.selectProductionAuditProgressPage(dto);
        handleAuditProgress(result);
        return CommonPage.convertPage(result);
    }

    /**
     * 处理工艺审核、工序审核审核中计数
     * @param result
     */
    private void handleAuditProgress(List<ProductionAuditProgressPageVO> result) {
        List<ProductionAuditProgressPageVO> endList = new ArrayList<>();
        List<ProductionAuditProgressPageVO> runningList = new ArrayList<>();
        for (ProductionAuditProgressPageVO vo : result) {
            if (Objects.equals(vo.getStart(), ProductPlanStartEnum.STARTING)) {
                runningList.add(vo);
            } else {
                endList.add(vo);
            }
        }
        ArrayList<PlanAuditingCountVO> countList = new ArrayList<>();
        if (CollUtil.isNotEmpty(endList)) {
            List<Long> ids = CollectionUtils.convertList(endList, ProductionAuditProgressPageVO::getId);
            countList.addAll(procedureTaskInstanceHistoryMapper.selectAuditingCount(ids));
        }
        if (CollUtil.isNotEmpty(runningList)) {
            List<Long> ids = CollectionUtils.convertList(runningList, ProductionAuditProgressPageVO::getId);
            countList.addAll(procedureTaskInstanceMapper.selectAuditingCount(ids));
        }
        Map<Long, PlanAuditingCountVO> map = CollectionUtils.convertMap(countList, PlanAuditingCountVO::getId);
        result.forEach(e->{
            e.setAuditingCount(map.getOrDefault(e.getId(), new PlanAuditingCountVO()).getAuditingCount());
        });
    }

    @Override
    public List<PlanAuditProgressDetailVO> queryPlanAuditDetailList(PlanAuditProgressDetailQueryDTO dto) {
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProductPlanStartEnum start = plan.getStart();
        // 进行中的计划任务实例
        if (Objects.equals(start, ProductPlanStartEnum.STARTING)) {
            List<PlanAuditProgressDetailVO> runningList = procedureTaskInstanceMapper.selectAuditProgressDetailVO(dto);
            return runningList;
        }
        if (Objects.equals(start, ProductPlanStartEnum.WAIT)) {
            return new ArrayList<>();
        }
        // 已完成、终止的计划任务实例
        List<PlanAuditProgressDetailVO> historyList =
                procedureTaskInstanceHistoryMapper.selectAuditProgressDetailVO(dto);
        return historyList;
    }

    @Override
    public List<PlanListVO> listUnTerminatePlanByProcessId(Long processId) {
        return PlanConverter.INSTANCE.convert2PlanListVO(planMapper.selectByProcessIdNotTermination(processId));
    }

    @Override
    public List<PlanEasyInfoVO> relationPlan(Long planId) {
        List<ProductPlanRelation> list = productPlanRelationService.getList(planId);
        if (CollUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        List<Long> planIdList = list.stream().map(ProductPlanRelation::getRelationProductPlanId).collect(Collectors.toList());
        List<Plan> plans = planMapper.selectBatchIds(planIdList);
        // 根据生产时间排序 若生产时间为空则放在最后
        plans.sort(Comparator.comparing(Plan::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
        List<PlanEasyInfoVO> planEasyInfoVOS = PlanConverter.INSTANCE.convert2PlanEasyInfoVO(plans);
        // 补充归档文件的链接
        planEasyInfoVOS.forEach(item -> {
            if (PlanArchiveStatusEnum.ARCHIVE_SUCCESS == item.getArchiveStatus()) {
                item.setArchiveFileUrl(PlanArchivePathUtil.getPlanMinioCompleteFilePath(minioProperties.getBuckets().getArchive(),
                        item.getId()));
            }
        });
        return planEasyInfoVOS;
    }


    @Override
    public List<Plan> queryByProductionPlanIdS(List<Long> planItemIdS) {
        return planMapper.queryByProductionPlanIdS(planItemIdS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<PlanSaveDTO> saveDTOS) {
        try {
            List<Plan> plans = PlanConverter.INSTANCE.convertDO(saveDTOS);
            planMapper.insertBatch(plans);
            // 查询物料信息自定义字段
            Map<Long, List<MaterialFieldInfoVO>> fieldList = materialFieldService.getMaterialFieldInfo(CollectionUtils.convertList(plans, Plan::getProductId))
                    .stream()
                    .collect(Collectors.groupingBy(MaterialFieldInfoVO::getMaterialId, Collectors.toList()));
            // 保存指令单与编号的关系 作废、编辑时用来释放编号
            handlePlanNoInfo(saveDTOS, fieldList);
            // 调用平台接口 批量确认编号
            batchConfirmNo(saveDTOS, fieldList);
            // 处理批次关联
            handleProductPlanRelation(saveDTOS);
            // 处理操作日志
            List<OperationLogModel> collect = plans.stream().map(e -> OperationLogModel.builder()
                    .module(BusinessModule.PRODUCT_PLAN.name())
                    .businessId(e.getId())
                    .operationType(OperationType.SAVE.getValue())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build()).collect(Collectors.toList());
            logService.saveBatch(collect);
        } catch (DuplicateKeyException exception) {
            // 校验计划编号编号是否重复和生产批号在同一工艺下是否重复
            throw new BmosException(PlanConstant.findException(exception));
        }
    }

    @Override
    public void updateRelation(PlanRelationUpdateDTO dto) {
        List<ProductPlanRelation> relations = convertToProductPlanRelation(dto.getRelationPlanList(), dto.getProductPlanId());
        productPlanRelationService.updateProductPlanRelation(relations , dto.getProductPlanId());
    }

    @Override
    public List<PlanListVO> listAllPlanByProductId(Long productId) {
        return PlanConverter.INSTANCE.convert2PlanListVO(planMapper.selectByProductId(productId));
    }

    @Override
    public Plan getPlanByPoductionPlanItemId(Long productionPlanItemId) {
        return planMapper.getPlanByPoductionPlanItemId(productionPlanItemId);
    }

    @Override
    public List<Plan> selectByProductionPlanItemId(List<Long> productionItemId) {
        return planMapper.selectByProductionPlanItemId(productionItemId);
    }

    @Override
    public List<Plan> getPlanListByProcedureVersionId(Long processId, Collection<String> processVersionList, int n) {
        return planMapper.selectPlanListByProcedureVersionId(processId, processVersionList, n);
    }

    @Override
    public List<PlanPageVO> getTodoPlanStart(WorkflowTodoPageDTO dto, List<Long> processIds,String todoType) {
        return planMapper.getTodoPlanStart(todoType,dto.getBatchNo(),processIds,dto.getProductIdList(),dto.getLineIdList());
    }

    @Override
    public Integer productManagePageCount(PlanPageDTO dto) {
        if (ObjectUtil.isNotNull(dto.getProductCategoryId())) {
            List<Long> productIdList = productMaterialService.getProductIdList(CategoryInfoTypeEnum.PRODUCTION,
                    dto.getProductCategoryId(), null);
            if (CollUtil.isEmpty(productIdList)) {
                return 0;
            }
            dto.setProductIds(productIdList);
        }
        return planMapper.productManagePageCount(BeanUtil.toBean(dto,PlanPageCountDTO.class)).size();
    }

    @Override
    public void savePlanNoInfo(ProductPlanNoInfo productPlanNoInfo) {
        planNoInfoMapper.insert(productPlanNoInfo);
    }

    private void handleProductPlanRelation(List<PlanSaveDTO> saveDTOS) {
        Map<Long, PlanSaveDTO> saveMap = CollectionUtils.convertMap(saveDTOS, PlanSaveDTO::getId);
        Graph<Long> relationGraph = new Graph<>();
        for (PlanSaveDTO saveDTO : saveDTOS) {
            List<Long> relationIds =
                    saveDTO.getRelationPlanList().stream().map(ProductPlanRelationDTO::getPlanIds)
                            .flatMap(List::stream).collect(Collectors.toList());
            relationGraph.addWithValidateCycle(saveDTO.getId(), relationIds);
        }
        List<Long> sortedIdList = relationGraph.sortKahn();
        for (Long l : sortedIdList) {
            PlanSaveDTO saveDTO = saveMap.get(l);
            if (saveDTO != null) {
                List<ProductPlanRelation> relations = convertToProductPlanRelation(saveDTO.getRelationPlanList(), saveDTO.getId());
                productPlanRelationService.saveProductPlanRelation(relations, saveDTO.getId());
            }
        }
    }

    /**
     * 批量确认编号
     *
     * @param saveDTOS
     * @param fieldList
     */
    private void batchConfirmNo(List<PlanSaveDTO> saveDTOS, Map<Long, List<MaterialFieldInfoVO>> fieldList) {
        List<ConfirmNoInfoDTO> codeList = new ArrayList<>();
        for (PlanSaveDTO saveDTO : saveDTOS) {
            Map<String, String> fields = buildParamMap(saveDTO, fieldList);
            if (StrUtil.isNotEmpty(saveDTO.getPlanNoCode())) {
                codeList.add(ConfirmNoInfoDTO.builder()
                        .code(saveDTO.getPlanNoCode())
                        .fullNo(saveDTO.getPlanNo())
                        .fields(fields)
                        .codeApplyTime(saveDTO.getPlanNoCodeApplyTime())
                        .build());
            }
            if (StrUtil.isNotEmpty(saveDTO.getBatchNoCode())) {
                codeList.add(ConfirmNoInfoDTO.builder()
                        .code(saveDTO.getBatchNoCode())
                        .fullNo(saveDTO.getBatchNo())
                        .codeApplyTime(saveDTO.getProductDate())
                        .fields(fields)
                        .build());
            }
        }
        if (CollUtil.isNotEmpty(codeList)) {
            BatchConfirmByCodeDTO dto = new BatchConfirmByCodeDTO();
            dto.setList(codeList);
            FeignUtils.handleRequest(codeRuleFeign::batchConfirmByCodeList, dto);
        }
    }

    private Map<String, String> buildParamMap(PlanSaveDTO saveDTO, Map<Long, List<MaterialFieldInfoVO>> fieldMap) {
        Map<String, String> fields = new HashMap<>();
        fields.put(ProductionPlanConstant.PRODUCT_MERGE_CODE, saveDTO.getProductMergeCode());
        fields.put(ProductionPlanConstant.PRODUCT_NAME, saveDTO.getProductName());
        fields.put(ProductionPlanConstant.PRODUCT_PLAN_TYPE, saveDTO.getProductPlanType());
        fields.put(ProductionPlanConstant.PRODUCTION_LINE_CODE, saveDTO.getProductionLineCode());
        fields.put(ProductionPlanConstant.INNER_PACKING_SPECIFICATION, saveDTO.getInnerPackingSpecification());
        fields.put(ProductionPlanConstant.PACKING_SPECIFICATION, saveDTO.getPackingSpecification());
        fields.put(ProductionPlanConstant.PRODUCTION_STAGE_CODE, saveDTO.getProductionStageCode());
        fields.put(ProductionPlanConstant.PRODUCT_MARK, saveDTO.getProductMark());
        List<MaterialFieldInfoVO> fieldList = fieldMap.get(saveDTO.getProductId());
        if (CollUtil.isNotEmpty(fieldList)) {
            fieldList.forEach(e->fields.put(e.getField(), e.getFieldValue()));
        }
        return fields;
    }

    /**
     * 处理指令单和编号关联信息
     *
     * @param saveDTOS
     * @param fieldMap
     */
    private void handlePlanNoInfo(List<PlanSaveDTO> saveDTOS, Map<Long, List<MaterialFieldInfoVO>> fieldMap) {
        List<ProductPlanNoInfo> noInfos = new ArrayList<>();
        for (PlanSaveDTO saveDTO : saveDTOS) {
            ProductPlanNoInfo productPlanNoInfo = new ProductPlanNoInfo();
            productPlanNoInfo.setProductPlanId(saveDTO.getId());
            if (StrUtil.isAllEmpty(saveDTO.getPlanNoCode(), saveDTO.getBatchNoCode())) {
                continue;
            }
            JSONObject jsonObject = JSONUtil.parseObj(saveDTO);
            List<MaterialFieldInfoVO> fields = fieldMap.get(saveDTO.getProductId());
            if (CollUtil.isNotEmpty(fields)) {
                fields.forEach(e->jsonObject.set(e.getField(), e.getFieldValue()));
            }
            productPlanNoInfo.setFields(JsonUtils.toJsonString(jsonObject));
            if (StrUtil.isNotEmpty(saveDTO.getPlanNoCode())) {
                productPlanNoInfo.setPlanNoCode(saveDTO.getPlanNoCode());
                productPlanNoInfo.setPlanNo(saveDTO.getPlanNo());
            }
            if (StrUtil.isNotEmpty(saveDTO.getBatchNoCode())) {
                productPlanNoInfo.setBatchNoCode(saveDTO.getBatchNoCode());
                productPlanNoInfo.setBatchNo(saveDTO.getBatchNo());
            }
            noInfos.add(productPlanNoInfo);
        }
        if (CollUtil.isNotEmpty(noInfos)) {
            planNoInfoMapper.insertBatch(noInfos);
        }
    }
}
