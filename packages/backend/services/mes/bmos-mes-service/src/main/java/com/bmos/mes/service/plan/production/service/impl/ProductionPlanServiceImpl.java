package com.bmos.mes.service.plan.production.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.plan.*;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.plan.info.constant.PlanConstant;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanNoInfo;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.instruction.service.InstructionService;
import com.bmos.mes.service.plan.production.constant.ProductionPlanConstant;
import com.bmos.mes.service.plan.production.convert.ProductionPlanConverter;
import com.bmos.mes.service.plan.production.dto.*;
import com.bmos.mes.service.plan.production.mapper.ProductionPlanMapper;
import com.bmos.mes.service.plan.production.model.ProductionPlan;
import com.bmos.mes.service.plan.production.model.ProductionPlanItem;
import com.bmos.mes.service.plan.production.service.ProductionPlanItemService;
import com.bmos.mes.service.plan.production.service.ProductionPlanService;
import com.bmos.mes.service.plan.production.vo.*;
import com.bmos.mes.service.plan.rule.model.CodeRule;
import com.bmos.mes.service.plan.rule.service.CodeRuleService;
import com.bmos.mes.service.plan.template.model.PlanTemplate;
import com.bmos.mes.service.plan.template.service.PlanTemplateService;
import com.bmos.mes.service.plan.template.vo.PlanTemplateDetailBatchVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplateDetailVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.plan.PlatformCodeRuleClient;
import com.bmos.mes.service.platform.plan.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.plan.vo.NextCodeVO;
import com.bmos.mes.service.process.dto.query.ProcessDetailQueryDTO;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.process.vo.ProcessDetailVO;
import com.bmos.mes.service.product.service.MaterialFieldService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.MaterialFieldInfoVO;
import com.bmos.mes.service.product.vo.ProductMaterialDetailVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.code.dto.BatchCodeNoDTO;
import com.bmos.platform.facade.code.dto.BatchConfirmByCodeDTO;
import com.bmos.platform.facade.code.dto.ConfirmNoInfoDTO;
import com.bmos.platform.facade.code.feign.CodeRuleFeign;
import com.bmos.platform.facade.code.vo.BatchCodeNoVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.github.pagehelper.PageHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {

    @Resource
    private ProductionPlanMapper productionPlanMapper;

    @Resource
    private ProductionPlanItemService itemService;

    @Resource
    private PlanTemplateService templateService;

    @Resource
    private CodeRuleService ruleService;

    @Resource
    private ProcessService processService;

    @Resource
    private CodeRuleFeign codeRuleFeign;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private MaterialFieldService materialFieldService;

    @Resource
    private PlatformCodeRuleClient platformCodeRuleClient;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private PlanService planService;

    @Resource
    private InstructionService instructionService;

    @Resource
    private ProductMaterialService productMaterialService;

    @Override
    public CommonPage<ProductionPlanPageVO> listPage(ProductionPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<ProductionPlan> productionPlans = productionPlanMapper.listPage(dto);
        List<Long> productionPlanIds = CollectionUtils.convertList(productionPlans, ProductionPlan::getId);
        if (CollUtil.isEmpty(productionPlanIds)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        List<ProductionPlanItem> itemList = itemService.queryListByProductionPlanIdS(productionPlanIds);
        Map<Long, List<ProductionPlanItem>> itemMap = CollectionUtils.convertMultiMap(itemList, ProductionPlanItem::getProductionPlanId);
        CommonPage<ProductionPlan> planCommonPage = CommonPage.convertPage(productionPlans);
        CommonPage<ProductionPlanPageVO> planPageVo = ProductionPlanConverter.INSTANCE.convertToPageVo(planCommonPage);
        planPageVo.getList().forEach(item -> {
            ProductionPlanItem planItem = CollectionUtils.getFirst(itemMap.get(item.getId()));
            if (ObjectUtil.isNull(planItem)) {
                return;
            }
            item.setPlanEndDate(planItem.getEndTime());
        });
        return planPageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void planNullify(Long id) {
        ProductionPlan productionPlan = productionPlanMapper.selectById(id);
        if (ObjectUtil.isEmpty(productionPlan)) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_ERROR);
        }
        if (productionPlan.getPlanState().equals(ProductionPlanStateEnum.NULLIFY.getValue())) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_NULLIFY_ERROR);
        }
        //查询生产指令单是否开始生产
        itemService.checkStartingPlanList(productionPlan.getId());
        productionPlan.setPlanState(ProductionPlanStateEnum.NULLIFY.getValue());
        itemService.nullifyPlan(id);
        productionPlanMapper.updateById(productionPlan);
    }

    @Override
    public ProductionPlanDetailVO listPlanDetail(Long id) {
        ProductionPlanDetailVO vo = productionPlanMapper.selectDetailById(id);
        //查询计划详情数据
        vo.setPlanDetailVOList(itemService.selectDetailByProductionPlanId(id));
        return vo;
    }

    @Override
    public List<List<ProductionPlanItemDetailVO>> buildPlan(BuildPlanDTO dto) {
        if (!dto.getConfirmed()) {
            throw new BmosException(MesResponseCode.PLAN_TEMPLATE_CONFIRMED_ERROR);
        }
        //校验工艺版本是否匹配生效版本
        if (!templateService.validateProcessVersionMatch(dto.getTemplateId())) {
            throw new BmosException(MesResponseCode.PLAN_TEMPLATE_CONFIRMED_ERROR);
        }
        //获取模板数据
        PlanTemplateDetailVO planTemplateDetail = templateService.getPlanTemplateDetail(dto.getTemplateId());
        if (ObjectUtil.isEmpty(planTemplateDetail)) {
            throw new BmosException(MesResponseCode.PLAN_TEMPLATE_ERROR);
        }
        //根据批量判断生成几批计划
        List<List<ProductionPlanItemDetailVO>> detailListVo = new ArrayList<>();
        for (int i = 0; i < dto.getPlanNumber(); i++) {
            List<ProductionPlanItemDetailVO> detailList = new ArrayList<>();
            List<PlanTemplateDetailBatchVO> templateBatchList = planTemplateDetail.getTemplateBatchList();
            Integer batchNumber = i;
            templateBatchList.forEach(template -> {
                //构建工艺数据
                LocalDate parse = LocalDate.parse(dto.getPlanFirstDate(), DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
                ProductionPlanItemDetailVO detailVO = ProductionPlanConverter.INSTANCE.convertToPlanItemDetail(template,
                        batchNumber, dto.getDuration(), parse);
                //构建关联生成批次
                if (CollUtil.isNotEmpty(detailVO.getRelationBatchSortList())) {
                    List<PlanTemplateDetailBatchVO> relationList = CollectionUtils.filterList(templateBatchList, item -> detailVO.getRelationBatchSortList().contains(item.getSort()));
                    List<String> processName = CollectionUtils.convertList(relationList, PlanTemplateDetailBatchVO::getProcessName);
                    detailVO.setProductionBatchList(String.join(",", processName));
                }
                //构建工序数据
                detailVO.setProcedureListDetail(ProductionPlanConverter.INSTANCE.convertToProcedureDetail(template.getProcedureList(), detailVO.getStartTime()));
                // 修正批次结束时间
                detailVO.setEndTime(detailVO.getProcedureListDetail().stream()
                        .max(Comparator.comparing(ProcedureDetailVO::getEndTime))
                        .map(ProcedureDetailVO::getEndTime)
                        .orElse(detailVO.getEndTime()));
                detailList.add(detailVO);
            });
            detailListVo.add(detailList);
        }
        return detailListVo;
    }

    @Override
    public PlanBatchNextNoMessageVO buildBatchNo(List<List<PlanBatchNoDTO>> dto) {
        if (CollUtil.isEmpty(dto)) {
            return null;
        }
        List<PlanBatchNoDTO> planBatchNoList = dto.stream().flatMap(Collection::stream).collect(Collectors.toList());
        Set<Long> processIdList = CollectionUtils.convertSet(planBatchNoList, PlanBatchNoDTO::getProcessId);
        //查询编码规则
        List<CodeRule> codeRuleList = ruleService.getCodeRuleListByProcessIdAndType(processIdList,
                Arrays.asList(CodeRuleTypeEnum.PRODUCT_PLAN_NO.getValue(), CodeRuleTypeEnum.PRODUCT_PLAN_BATCH_NO.getValue()));
        if (CollUtil.isEmpty(codeRuleList)) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_BATCH_ERROR);
        }
        Map<Long, Map<CodeRuleTypeEnum, String>> codeRuleMap = codeRuleList.stream()
                .collect(Collectors.groupingBy(CodeRule::getProcessId,
                        Collectors.toMap(CodeRule::getType, CodeRule::getCodeRuleCode)));
        List<PlanBatchNextNoVO> vos = ProductionPlanConverter.INSTANCE.convertToBatchNextNoList(planBatchNoList, codeRuleMap);
        //构建生成批号所需数据
        List<BuildPlanBatchNextNoVO> nextNoVo = processService.selectProductListByProcessIdS(processIdList);
        // 查询物料信息自定义字段
        Map<Long, List<MaterialFieldInfoVO>> fieldList = materialFieldService.getMaterialFieldInfo(CollectionUtils.convertList(nextNoVo, BuildPlanBatchNextNoVO::getMaterialId))
                .stream()
                .collect(Collectors.groupingBy(MaterialFieldInfoVO::getMaterialId, Collectors.toList()));
        nextNoVo.forEach(item -> {
            item.setCustomFieldInfoList(fieldList.getOrDefault(item.getMaterialId(), new ArrayList<>()));
        });
        Map<Long, BuildPlanBatchNextNoVO> productMap = CollectionUtils.convertMap(nextNoVo, BuildPlanBatchNextNoVO::getProcessId);
        //生产指令单编号
        Map<String, List<PlanBatchNextNoVO>> planCodeMap = vos.stream()
                .filter(item -> StrUtil.isNotBlank(item.getPlanNoCode()))
                .collect(Collectors.groupingBy(PlanBatchNextNoVO::getPlanNoCode));
        //生产批号编号
        Map<String, List<PlanBatchNextNoVO>> batchNoCodeMap = vos.stream()
                .filter(item -> StrUtil.isNotBlank(item.getBatchNoCode()))
                .collect(Collectors.groupingBy(PlanBatchNextNoVO::getBatchNoCode));
        if (CollUtil.isNotEmpty(planCodeMap)) {
            planCodeMap.forEach((planNoCode, list) -> {
                //构建参数
                ResponseInfo<BatchCodeNoVO> responseInfo = FeignUtils.handleRequest(codeRuleFeign::batchGetSameTypeNo, buildUseCodeDto(planNoCode, list, productMap, true));
                BatchCodeNoVO data = responseInfo.getData();
                if (MesResponseCode.PARAMETER_NOT_FULL.getCode() == responseInfo.getCode()) {
                    throw new BmosException(MesResponseCode.PARAMETER_NOT_FULL);
                }
                if (ObjectUtil.isEmpty(data) || BooleanUtil.isTrue(data.getRuleNotExist())) {
                    return;
                }
                buildProductPlanNo(vos, data, list, CodeRuleTypeEnum.PRODUCT_PLAN_NO);
            });
        }
        if (CollUtil.isNotEmpty(batchNoCodeMap)) {
            batchNoCodeMap.forEach((batchNoCode, list) -> {
                //构建参数
                ResponseInfo<BatchCodeNoVO> responseInfo = FeignUtils.handleRequest(codeRuleFeign::batchGetSameTypeNo, buildUseCodeDto(batchNoCode, list, productMap, false));
                BatchCodeNoVO data = responseInfo.getData();
                if (MesResponseCode.PARAMETER_NOT_FULL.getCode() == responseInfo.getCode()) {
                    throw new BmosException(MesResponseCode.PARAMETER_NOT_FULL);
                }
                if (ObjectUtil.isEmpty(data) || BooleanUtil.isTrue(data.getRuleNotExist())) {
                    return;
                }
                buildProductPlanNo(vos, data, list, CodeRuleTypeEnum.PRODUCT_PLAN_BATCH_NO);
            });
        }
        handelBatchNextNoVO(vos);
        vos.sort(Comparator.comparing(PlanBatchNextNoVO::getKey));
        StringBuilder meg = new StringBuilder();
        List<String> list = CollectionUtils.convertList(vos, PlanBatchNextNoVO::getProcessName, item ->
                StrUtil.isBlank(item.getBatchNo()) && StrUtil.isBlank(item.getPlanNo()));
        if (CollUtil.isNotEmpty(list)) {
            meg.append(MessageFormat.format(MesResponseCode.PLAN_NO_AND_BATCH_NO_ERROR.getMessage(), String.join("、", list)));
        }
        List<String> batchNoMeg = CollectionUtils.convertList(vos, PlanBatchNextNoVO::getProcessName, item ->
                StrUtil.isBlank(item.getBatchNo()) && StrUtil.isNotBlank(item.getPlanNo()));
        if (CollUtil.isNotEmpty(batchNoMeg)) {
            meg.append(MessageFormat.format(MesResponseCode.BATCH_NO_ERROR.getMessage(), String.join("、", batchNoMeg)));
        }
        List<String> planNoMeg = CollectionUtils.convertList(vos, PlanBatchNextNoVO::getProcessName, item ->
                StrUtil.isNotBlank(item.getBatchNo()) && StrUtil.isBlank(item.getPlanNo()));
        if (CollUtil.isNotEmpty(planNoMeg)) {
            meg.append(MessageFormat.format(MesResponseCode.PLAN_NO_ERROR.getMessage(), String.join("、", planNoMeg)));
        }
        return PlanBatchNextNoMessageVO.builder().meg(meg.toString()).list(vos).build();
    }

    /**
     * 构建指令单编号参数
     *
     * @param code 编号规则
     * @param list 参数
     * @param map  产品信息
     * @param applyTimeNow 是否使用当天时间作为编码生成时间参数(指令单编号使用当天时间、生产批号使用批次计划开始时间)
     * @return
     */
    public BatchCodeNoDTO buildUseCodeDto(String code, List<PlanBatchNextNoVO> list, Map<Long, BuildPlanBatchNextNoVO> map, boolean applyTimeNow) {
        BatchCodeNoDTO codeDTO = new BatchCodeNoDTO();
        codeDTO.setCode(code);
        List<Map<String, String>> fieldsList = new ArrayList<>();
        list.forEach(item -> {
            BuildPlanBatchNextNoVO productData = map.get(item.getProcessId());
            Map<String, String> fields = new HashMap<>();
            fieldsList.add(fields);
            putProcessBaseInfo(productData, fields);
            fields.put(ProductionPlanConstant.PRODUCT_PLAN_TYPE, item.getProductPlanType());
            fields.put(ProductionPlanConstant.PRODUCTION_LINE_CODE, item.getProductionLineCode());
            fields.put(ProductionPlanConstant.APPLY_TIME, LocalDateTimeUtil.format(applyTimeNow ? LocalDate.now() : item.getStartTime(), "yyyy-MM-dd"));
        });
        codeDTO.setFields(fieldsList);
        return codeDTO;
    }

    private static void putProcessBaseInfo(BuildPlanBatchNextNoVO productData, Map<String, String> fields) {
        // 物料信息自定义字段
        List<MaterialFieldInfoVO> customFieldInfoList = productData.getCustomFieldInfoList();
        for (MaterialFieldInfoVO fieldInfo : customFieldInfoList) {
            fields.put(fieldInfo.getField(), fieldInfo.getFieldValue());
        }
        fields.put(ProductionPlanConstant.PRODUCT_MERGE_CODE, productData.getProductMergeCode());
        fields.put(ProductionPlanConstant.PRODUCT_NAME, productData.getProductName());
        fields.put(ProductionPlanConstant.INNER_PACKING_SPECIFICATION, productData.getInnerPackingSpecification());
        fields.put(ProductionPlanConstant.PACKING_SPECIFICATION, productData.getPackingSpecification());
        fields.put(ProductionPlanConstant.PRODUCTION_STAGE_CODE, productData.getProductionStageCode());
        fields.put(ProductionPlanConstant.PRODUCT_MARK, productData.getProductMark());
    }

    /**
     * 构建生成的批号
     *
     * @param vos              原始数据
     * @param batchCodeNoVO    批号
     * @param freshList        本次生成的批号数据
     * @param codeRuleTypeEnum 生成的批号类型
     * @return
     */
    private void buildProductPlanNo(List<PlanBatchNextNoVO> vos, BatchCodeNoVO batchCodeNoVO,
                                    List<PlanBatchNextNoVO> freshList, CodeRuleTypeEnum codeRuleTypeEnum) {
        List<String> nos = batchCodeNoVO.getNos();
        AtomicInteger index = new AtomicInteger();
        freshList.forEach(item -> {
            if (codeRuleTypeEnum.equals(CodeRuleTypeEnum.PRODUCT_PLAN_NO)) {
                item.setPlanNoCode(batchCodeNoVO.getCode());
                item.setPlanNo(nos.get(index.getAndIncrement()));
            } else {
                item.setBatchNoCode(batchCodeNoVO.getCode());
                item.setBatchNo(nos.get(index.getAndIncrement()));
            }
        });
        Map<Integer, PlanBatchNextNoVO> freshMap = CollectionUtils.convertMap(freshList, PlanBatchNextNoVO::getKey);
        vos.forEach(item -> {
            PlanBatchNextNoVO vo = freshMap.get(item.getKey());
            if (ObjectUtil.isEmpty(vo)) {
                return;
            }
            item.setBatchNo(Optional.ofNullable(vo.getBatchNo()).orElse(null));
            item.setPlanNoCodeApplyTime(LocalDate.now());
        });
    }

    /**
     * 处理沿用关系
     *
     * @param vos
     */
    public void handelBatchNextNoVO(List<PlanBatchNextNoVO> vos) {
        vos.forEach(item -> {
            List<String> productionBatch = new ArrayList<>();
            int relationNumber = item.getRelationBatchSortList().size();
            if (CollUtil.isNotEmpty(item.getBatchNoList())) {
                relationNumber = relationNumber + item.getBatchNoList().size();
            }
            item.setIsFlay(true);
            if (checkReuseBatch(item)) {
                //判断是沿用关联批次以及历史批次的
                List<String> batchNoList = item.getBatchNoList();
                if (CollUtil.isNotEmpty(batchNoList)) {
                    item.setBatchNo(CollectionUtils.getFirst(batchNoList));
                    item.setProductionBatchList(item.getRelatedBatchInfo());
                    return;
                }
                //沿用是当前生产计划下的
                List<PlanBatchNextNoVO> relationList = CollectionUtils.filterList(vos, items ->
                        item.getRelationBatchSortList().contains(items.getSort()) && item.getGroupNumber().equals(items.getGroupNumber()));
                PlanBatchNextNoVO batchNo = CollectionUtils.getFirst(relationList);
                if (ObjectUtils.isNotEmpty(batchNo)) {
                    //沿用的批号存在沿用关系但是是沿用批号往后排
                    if (checkReuseBatch(batchNo) && (ObjectUtil.isNull(batchNo.getIsFlay()) || BooleanUtil.isFalse(batchNo.getIsFlay()))) {
                        item.setIsFlay(false);
                        return;
                    }
                    item.setBatchNoCode(Optional.ofNullable(batchNo.getBatchNoCode()).orElse(item.getBatchNoCode()));
                    item.setBatchNo(Optional.ofNullable(batchNo.getBatchNo()).orElse(item.getBatchNo()));
                    String batchNoString = StrUtil.isNotBlank(item.getBatchNo()) ? StrUtil.DASHED + item.getBatchNo() : "";
                    productionBatch = relationList.stream().map(relation -> relation.getProcessName() + batchNoString)
                            .collect(Collectors.toList());
                }else {
                    item.setProductionBatchList(null);
                    return;
                }
            }
            if (relationNumber > 1) {
                item.setIsFlay(true);
            }
            item.setProductionBatchList(CollUtil.isNotEmpty(productionBatch) ? String.join(",", productionBatch) : item.getRelatedBatchInfo());
        });
        //找到循环沿用的数据
        List<PlanBatchNextNoVO> notReuseBatch = CollectionUtils.filterList(vos, item -> BooleanUtil.isFalse(item.getIsFlay()));
        if (CollUtil.isNotEmpty(notReuseBatch)) {
            handelBatchNextNoVO(vos);
        }
    }

    private Boolean checkReuseBatch(PlanBatchNextNoVO item) {
        int relationNumber = item.getRelationBatchSortList().size();
        if (CollUtil.isNotEmpty(item.getBatchNoList())) {
            relationNumber = relationNumber + item.getBatchNoList().size();
        }
        if (item.getReuseBatchNumber() && relationNumber == 1) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.planTemplateId")
    public ProductionPlanIssueResVO issueProductionPlan(ProductionPlanIssueDTO dto) {
        ProductionPlanIssueResVO result = new ProductionPlanIssueResVO();
        // 校验模板工艺版本与工艺生效版本是否匹配
        if (!templateService.validateProcessVersionMatch(dto.getPlanTemplateId())) {
            throw new BmosException(MesResponseCode.PLAN_TEMPLATE_CONFIRMED_ERROR);
        }
        // 校验名称
        if (productionPlanMapper.existedProductionPlanName(dto.getPlanName())) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_NAME_EXISTED);
        }
        // 校验选中的生产计划模板状态是否正确
        PlanTemplate template = templateService.getById(dto.getPlanTemplateId());
        if (template == null || !template.getState()) {
            throw new BmosException(MesResponseCode.TEMPLATE_DISABLE_OR_DELETED);
        }
        // 生成计划
        ProductionPlan plan = ProductionPlanConverter.INSTANCE.convert2ProductionPlan(dto);
        // 校验编码是否重复
        if (validateAnyNoExisted(dto, result)) {
            return result;
        }
        productionPlanMapper.insert(plan);
        // 生成生产计划详情并生成指令单
        for (List<ProductionPlanItemSaveDTO> productionPlanItemSaveDTOS : dto.getItemList()) {
            if (CollUtil.isNotEmpty(productionPlanItemSaveDTOS)) {
                itemService.issueProductionPlanItem(productionPlanItemSaveDTOS, plan.getId(), dto.getPlanType());
            }
        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public DirectlyCreateBuildNoVO buildPlanNoAndBatchNo(DirectlyCreateBuildNoDTO dto) {
        DirectlyCreateBuildNoVO result = new DirectlyCreateBuildNoVO();
        // 指令单编号规则
        CodeRule planCode = ruleService.selectByProcessIdAndType(dto.getProcessId(), CodeRuleTypeEnum.PRODUCT_PLAN_NO.getValue());
        // 生产批号编号规则
        CodeRule batchCode = ruleService.selectByProcessIdAndType(dto.getProcessId(), CodeRuleTypeEnum.PRODUCT_PLAN_BATCH_NO.getValue());
        // 组装参数
        Map<String, String> fields = buildCodeRuleParams(dto);
        if (planCode != null) {
            NextUseCodeDTO nextUseCodeDTO = new NextUseCodeDTO();
            nextUseCodeDTO.setCode(planCode.getCodeRuleCode());
            nextUseCodeDTO.setFields(fields);
            NextCodeVO data = FeignUtils.handleRequest(platformCodeRuleClient::getNextUseNo, nextUseCodeDTO).getData();
            result.setPlanNo(data.getNo());
            result.setPlanNoCode(planCode.getCodeRuleCode());
        }
        if (batchCode != null) {
            NextUseCodeDTO nextUseCodeDTO = new NextUseCodeDTO();
            nextUseCodeDTO.setCode(batchCode.getCodeRuleCode());
            nextUseCodeDTO.setFields(fields);
            NextCodeVO data = FeignUtils.handleRequest(platformCodeRuleClient::getNextUseNo, nextUseCodeDTO).getData();
            result.setBatchNo(data.getNo());
            result.setBatchNoCode(batchCode.getCodeRuleCode());
        }
        result.setCodeApplyTime(LocalDate.now());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_PLAN, operationType = OperationType.SAVE, businessId = "#getId")
    public void directlyCreatePlan(DirectlyCreatePlanDTO dto) {
        if (planMapper.planNoExists(dto.getPlanNo())) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NO_DUPLICATE);
        }
        if (planMapper.batchNoExists(dto.getProcessId(), dto.getBatchNo())) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_BATCH_NO_DUPLICATE);
        }
        // 生成指令单
        Plan plan = buildPlan(dto);
        planMapper.insert(plan);
        // 编号确认
        confirmNo(dto, plan);
        // 指令单生成并自动确认
        instructionService.generate(plan.getId(), true);
        // 指令单下发
        instructionService.send(plan.getId());
        // 历史
        OperationHistoryContext.putVariable(plan, Plan::getId);
    }

    private Plan buildPlan(DirectlyCreatePlanDTO dto) {
        ProcessDetailQueryDTO processDTO = new ProcessDetailQueryDTO();
        processDTO.setProcessId(dto.getProcessId());
        processDTO.setVersion(dto.getProcessVersion());
        ProcessDetailVO detail = processService.getDetail(processDTO);
        ProductMaterialDetailVO material = productMaterialService.getDetail(detail.getProductId());
        Plan plan = ProductionPlanConverter.INSTANCE.convert2Plan(dto, detail, material);
        // 生成productionPlanItem保存工序配置
        ProductionPlanItem item = itemService.SaveDirectlyCreatedPlanItem(plan, detail);
        plan.setProductionPlanItemId(item.getId());
        return plan;
    }

    private void confirmNo(DirectlyCreatePlanDTO dto, Plan plan) {
        Map<String, String> fields = buildCodeRuleParams(ProductionPlanConverter.INSTANCE.convert2DirectlyCreateBuildNoDTO(dto));
        ProductPlanNoInfo productPlanNoInfo = new ProductPlanNoInfo();
        productPlanNoInfo.setFields(JsonUtils.toJsonString(fields));
        productPlanNoInfo.setPlanNo(dto.getPlanNo());
        productPlanNoInfo.setBatchNo(dto.getBatchNo());
        productPlanNoInfo.setProductPlanId(plan.getId());
        List<ConfirmNoInfoDTO> list = new ArrayList<>();
        if (StrUtil.isNotEmpty(dto.getPlanNoCode())) {
            ConfirmNoInfoDTO confirmNoInfoDTO = new ConfirmNoInfoDTO();
            confirmNoInfoDTO.setCode(dto.getPlanNoCode());
            confirmNoInfoDTO.setFullNo(dto.getPlanNo());
            confirmNoInfoDTO.setCodeApplyTime(Objects.isNull(dto.getCodeApplyTime()) ? LocalDate.now() : dto.getCodeApplyTime());
            confirmNoInfoDTO.setFields(fields);
            list.add(confirmNoInfoDTO);
            productPlanNoInfo.setPlanNoCode(dto.getPlanNoCode());
        }
        if (StrUtil.isNotEmpty(dto.getBatchNoCode())) {
            ConfirmNoInfoDTO confirmNoInfoDTO = new ConfirmNoInfoDTO();
            confirmNoInfoDTO.setCode(dto.getBatchNoCode());
            confirmNoInfoDTO.setFullNo(dto.getBatchNo());
            confirmNoInfoDTO.setCodeApplyTime(Objects.isNull(dto.getCodeApplyTime()) ? LocalDate.now() : dto.getCodeApplyTime());
            confirmNoInfoDTO.setFields(fields);
            list.add(confirmNoInfoDTO);
            productPlanNoInfo.setBatchNoCode(dto.getBatchNoCode());
        }
        if (CollUtil.isEmpty(list)) {
            return;
        }
        BatchConfirmByCodeDTO confirmByCodeDTO = new BatchConfirmByCodeDTO();
        confirmByCodeDTO.setList(list);
        // 批量确认编号
        FeignUtils.handleRequest(codeRuleFeign::batchConfirmByCodeList, confirmByCodeDTO);
        planService.savePlanNoInfo(productPlanNoInfo);
    }

    private  Map<String, String> buildCodeRuleParams(DirectlyCreateBuildNoDTO dto) {
        BuildPlanBatchNextNoVO processInfo = processService.selectProductListByProcessIdS(Collections.singleton(dto.getProcessId())).get(0);
        List<MaterialFieldInfoVO> materialFieldInfo = materialFieldService.getMaterialFieldInfo(processInfo.getMaterialId());
        processInfo.setCustomFieldInfoList(materialFieldInfo);
        Map<String, String> fields = new HashMap<>();
        putProcessBaseInfo(processInfo, fields);
        fields.put(ProductionPlanConstant.PRODUCT_PLAN_TYPE, CommonEnum.getEnumByValue(ProductPlanTypeEnum.class, dto.getProductPlanType()).getCodeParamMapping());
        List<FactoryLineFeignVO> lineData = FeignUtils.handleRequest(factoryFeign::queryLineListByLineIds, Collections.singletonList(dto.getProductionLineId())).getData();
        FactoryLineFeignVO line = CollUtil.getFirst(lineData);
        Optional.ofNullable(line)
                .ifPresent(l -> fields.put(ProductionPlanConstant.PRODUCTION_LINE_CODE, l.getCode()));
        fields.put(ProductionPlanConstant.APPLY_TIME, LocalDateTimeUtil.format(LocalDate.now(), "yyyy-MM-dd"));
        return fields;
    }

    /**
     * 校验任意编号重复
     *
     * @param dto
     * @param result
     * @return 存在重复返回true否则返回false
     */
    private Boolean validateAnyNoExisted(ProductionPlanIssueDTO dto, ProductionPlanIssueResVO result) {
        Set<String> currentPlanNos = new HashSet<>();
        // 指令单编号重复编码
        Set<String> duplicatesPlanNos = new HashSet<>();
        Set<String> currentBatchNos = new HashSet<>();
        // 生产批号重复编码
        Set<String> duplicatesBatchNos = new HashSet<>();
        PlanTemplateDetailVO templateDetail = templateService.getPlanTemplateDetail(dto.getPlanTemplateId());
        List<PlanTemplateDetailBatchVO> batchList = templateDetail.getTemplateBatchList();
        Map<Long, Long> idMap = CollectionUtils.convertMap(batchList, PlanTemplateDetailBatchVO::getTemplateBatchId,
                PlanTemplateDetailBatchVO::getProcessId);
        dto.getItemList().stream().flatMap(List::stream).forEach(e -> {
            ;
            if (!currentPlanNos.add(e.getPlanNo())) {
                duplicatesPlanNos.add(e.getPlanNo());
            }
            String fullBatchNo = idMap.get(e.getTemplateBatchId()) + StrUtil.DASHED + e.getBatchNo();
            if (!currentBatchNos.add(fullBatchNo)) {
                duplicatesBatchNos.add(fullBatchNo);
            }
        });
        // 校验指令单编码重复
        List<Plan> plans = planMapper.selectByPlanNos(currentPlanNos);
        duplicatesPlanNos.addAll(CollectionUtils.convertList(plans, Plan::getPlanNo));
        // 校验生产批号重复
        List<Plan> plans1 = planMapper.selectByFullBatchNos(currentBatchNos);
        duplicatesBatchNos.addAll(CollectionUtils.convertList(plans1, plan -> plan.getProcessId() + StrUtil.DASHED + plan.getBatchNo()));
        if (CollUtil.isEmpty(duplicatesPlanNos) && CollUtil.isEmpty(duplicatesBatchNos)) {
            return false;
        }
        result.setPlanNoList(new ArrayList<>(duplicatesPlanNos));
        Map<Long, String> nameMap = CollectionUtils.convertMap(batchList, PlanTemplateDetailBatchVO::getProcessId, PlanTemplateDetailBatchVO::getProcessName);
        result.setBatchNoList(duplicatesBatchNos.stream().map(e -> {
            ProductionPlanIssueResVO.ProcessBatchNoVO vo = new ProductionPlanIssueResVO.ProcessBatchNoVO();
            List<String> split = StrUtil.split(e, '-', 2);
            vo.setProcessId(Long.valueOf(CollUtil.getFirst(split)));
            vo.setBatchNo(CollUtil.getLast(split));
            vo.setProcessName(nameMap.get(vo.getProcessId()));
            return vo;
        }).collect(Collectors.toList()));
        result.setSuccess(false);
        return true;
    }

}
