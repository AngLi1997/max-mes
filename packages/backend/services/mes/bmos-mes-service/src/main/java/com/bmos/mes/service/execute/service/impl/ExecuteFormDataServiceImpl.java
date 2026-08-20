package com.bmos.mes.service.execute.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.execute.ExceptionRecordModeEnum;
import com.bmos.mes.common.enums.execute.ExceptionStatusEnum;
import com.bmos.mes.common.enums.execute.ExceptionTypeDictEnum;
import com.bmos.mes.common.enums.execute.ModifyExceptionEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.enums.record.FormulaValueTakeTypeEnum;
import com.bmos.mes.common.enums.record.RecordItemTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.ComponentDetail;
import com.bmos.mes.common.model.component.ScopeLimitConfig;
import com.bmos.mes.common.utils.Graph;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.exception.dto.RecordModifyExceptionDTO;
import com.bmos.mes.service.exception.dto.RecordModifyItemDTO;
import com.bmos.mes.service.exception.model.ExecuteException;
import com.bmos.mes.service.exception.service.ExceptionManageService;
import com.bmos.mes.service.execute.constant.ExecuteFormDataConstant;
import com.bmos.mes.service.execute.constant.RedissionKeyConstant;
import com.bmos.mes.service.execute.convert.ExecuteAttachmentConvert;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.mapper.ExecuteFormDataMapper;
import com.bmos.mes.service.execute.mapper.ExecuteSubsidiaryRecordMapper;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.model.ExecuteSubsidiaryRecord;
import com.bmos.mes.service.execute.model.calculate.CalculateParam;
import com.bmos.mes.service.execute.model.calculate.CalculateResult;
import com.bmos.mes.service.execute.redis.ExecuteRedisKeyDefine;
import com.bmos.mes.service.execute.service.ExecuteAttachmentService;
import com.bmos.mes.service.execute.service.ExecuteFormDataHandleService;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.execute.vo.*;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.plan.team.vo.ProcedureStepChangeVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.preparation.measure.dto.FormDataFilterDTO;
import com.bmos.mes.service.process.dto.ProcedureStepModelQueryDTO;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessRecordOrderQueryDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessRecordOrder;
import com.bmos.mes.service.process.service.*;
import com.bmos.mes.service.process.vo.FieldConfigVO;
import com.bmos.mes.service.process.vo.ProcedureStepModelDetailVO;
import com.bmos.mes.service.process.vo.ProcessRecordOrderVO;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProcessDetailInfo;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.dto.FormulaFieldDTO;
import com.bmos.mes.service.record.enums.ComponentFormulaTypeEnum;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.model.formula.ComponentFormulaConfig;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordItemService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.record.vo.RecordItemVO;
import com.bmos.mes.service.utils.DateCalculateVO;
import com.bmos.mes.service.utils.ExecuteDateCalculateUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.service.execute.constant.ExecuteFormDataConstant.DEFAULT_COPY_VERSION;
import static com.bmos.mes.service.execute.constant.ExecuteFormDataConstant.FORMULA_PROCEDURE_STEP_ID;

@Service
@Slf4j
public class ExecuteFormDataServiceImpl implements ExecuteFormDataService {
    @Value("${parameter.record.error-data}")
    private String recordErrorData;
    @Value("${parameter.record.empty-data}")
    private String recordEmptyDataCode;
    @Autowired
    private PlatformParameterClientImpl platformParameterClientImpl;
    @Autowired
    private ExecuteFormDataMapper executeFormDataMapper;

    @Autowired
    private ExecuteRecordCopyService executeRecordCopyService;

    @Autowired
    private ProductPlanRelationService productPlanRelationService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private BatchRecordComponentService batchRecordComponentService;
    @Autowired
    private ExpressionCalculator expressionCalculator;
    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcessRecordOrderService processRecordOrderService;
    @Autowired
    private PlanService planService;
    @Autowired
    private ExecuteAttachmentService executeAttachmentService;

    @Autowired
    private BatchRecordItemService batchRecordItemService;
    @Autowired
    private ProcessVersionService processVersionService;
    @Autowired
    private ProductFormulaConfigureService formulaConfigureService;
    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private Map<String, BusinessComponentStrategy> strategyMap;

    @Autowired
    private ProcedureModelService procedureModelService;

    @Autowired
    private UnitCache unitCache;

    @Resource
    private MinioFileClient minioFileClient;

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private ExceptionManageService exceptionManageService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private BusinessParameterFeign businessParameterFeign;

    @Resource
    private ExecuteSubsidiaryRecordMapper executeSubsidiaryRecordMapper;

    @Resource
    private InstructionTeamService instructionTeamService;

    @Resource
    private ExecuteFormDataHandleService formDataHandleService;

    private static final String CUSTOM_LIMIT_CONFIG_FIELD = "equipmentDataAttrList";

    @Resource
    @Lazy
    private BusinessComponentManager businessComponentManager;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(FormDataBatchSaveDTO dto) {
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                dto.getProductPlanId()));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        try {
            List<ExecuteFormData> dataList = ExecuteFormDataConverter.INSTANCE.convert(dto);
            //判断是否是存在拍照上传组件，拍照上传组件保存图片附件，value保存附件id
            if (CollUtil.isNotEmpty(dataList)) {
                savePicture(dataList,dto.getRecordVersionId());
            }
            executeFormDataMapper.insertBatch(dataList);
            List<ExecuteFormData> resultData = calculateData(
                    ExecuteFormDataConverter.INSTANCE.convert(dto),
                    ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
            if (CollUtil.isNotEmpty(resultData)) {
                Long nextRev = selectMaxRev(dto.getProductPlanId(), CollectionUtils.convertSet(resultData, ExecuteFormData::getFieldId));
                resultData.forEach(e -> e.setRev(nextRev));
                executeFormDataMapper.insertBatch(resultData);
            }
            exceptionAutoRecord(dataList, resultData, dto.getProcedureStepId());
            RecordModifyExceptionDTO build = RecordModifyExceptionDTO.builder()
                    .productPlanId(dto.getProductPlanId())
                    .procedureStepId(dto.getProcedureStepId())
                    .modifyException(ModifyExceptionEnum.SAVE)
                    .itemList(dataList.stream().map(e-> RecordModifyItemDTO.builder()
                            .value(e.getValue())
                            .operationTime(e.getOperationTime())
                            .userId(e.getOperationUser())
                            .reviewerId(e.getReviewUser())
                            .build()).collect(Collectors.toList()))
                    .build();
            exceptionManageService.recordModifyException(build);
        } catch (DuplicateKeyException e) {
            log.info("生产计划[{}]记录数据重复:{}", dto.getProductPlanId(), e.getMessage());
            throw new BmosException(MesResponseCode.EXECUTE_DATA_EXIST);
        } catch (DataIntegrityViolationException e) {
            handleDataTooLong(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @Author: Ren Jin Guang
     * @Description: 保存拍照上传组件图片
     * @Param: dataList
     * @return:
     * @Date: 2024-07-31 18:12:09
     */
    private void savePicture(List<ExecuteFormData> dataList,Long recordVersionId) {
        List<ExecuteAttachment> pictureList = new ArrayList<>();
        dataList.forEach(item -> {
            if (StrUtil.equals(item.getComponentType(),BasicComponentTypeEnum.PHOTO.getValue()) && StrUtil.isNotEmpty(item.getValue())){
                if (BooleanUtil.isTrue(item.getEmptyValue())){
                    return;
                }
                List<ExecuteAttachment> executeAttachments = ExecuteAttachmentConvert.INSTANCE.convertVoList(JsonUtils.parseArray(item.getValue(),AttachmentVO.class), item,recordVersionId);
                pictureList.addAll(executeAttachments);
                String value = String.join(",", CollectionUtils.convertList(executeAttachments, e -> String.valueOf(e.getId())));
                item.setValue(value);
            }
        });
        //保存附件
        executeAttachmentService.saveOrUpdateBatch(pictureList);
    }

    private static void handleDataTooLong(DataIntegrityViolationException e) {
        Throwable cause = e.getCause().getCause();
        if (Objects.equals(cause.getClass(), MysqlDataTruncation.class)) {
            throw new BmosException(MesResponseCode.CALCULATE_RESULT_TOO_LONG_FOR_COLUMN);
        }
        throw e;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(FormDataModifyDTO dto) {
        handleModifyOrUpdate(dto, ExecuteFormDataType.MODIFY);
        // 处理生产信息修订数量
        this.handlePlanModifyCount(dto.getProductPlanId());
        // 记录修订异常 非进行中的数据进行修订 需要记录异常
        RecordModifyExceptionDTO build = RecordModifyExceptionDTO.builder()
                .productPlanId(dto.getProductPlanId())
                .procedureStepId(dto.getProcedureStepId())
                .modifyException(ModifyExceptionEnum.MODIFY)
                .itemList(Lists.newArrayList(RecordModifyItemDTO.builder()
                                .value(dto.getValue())
                                .originalValue(dto.getOriginalValue())
                                .operationTime(dto.getOperationTime())
                                .userId(dto.getOperationUser())
                                .reviewerId(dto.getReviewUser())
                        .build()))
                .build();
        exceptionManageService.recordModifyException(build);
    }

    @Override
    public void handlePlanModifyCount(Long productPlanId) {
        Plan plan = planService.getById(productPlanId);
        Integer count = executeFormDataMapper.countModifyFieldByPlanId(productPlanId);
        if (Objects.equals(plan.getModifyCount(), count)) {
            return;
        }
        // 如果count有变动
        plan.setModifyCount(count);
        planService.updateById(plan);
        // 查询已经执行的步骤记录页
        List<ExecuteRecordCopy> recordCopyList = executeRecordCopyService.getList(plan.getId(), null);
        // 获取生产信息组件修订数量组件值
        List<ExecuteFormData> insertList = generateCurrentPlanModifyCountFieldFormData(plan, recordCopyList);
        // 处理关联组件的节点
        Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> dataMap =
                insertList.stream().collect(Collectors.groupingBy(ExecuteFormData::getRecordItemId,
                        Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                                Collectors.groupingBy(ExecuteFormData::getCopyVersion))));
        List<ProcedureStepModel> procedureStepModelList =
                procedureStepModelService.getStepModelByProcessIdAndVersion(plan.getProcessId(),
                        plan.getProcessVersion());
        Map<Long, ProcedureStepModel> stepMap = CollectionUtils.convertMap(procedureStepModelList,
                ProcedureStepModel::getProcedureStepId);
        Map<Long, List<ProcedureStepModel>> itemMap =
                procedureStepModelList.stream()
                        .filter(e -> BooleanUtil.isTrue(e.getReusable()))
                        .collect(Collectors.groupingBy(ProcedureStepModel::getRecordItemId));
        for (ExecuteRecordCopy copy : recordCopyList) {
            List<ExecuteFormData> thisList =
                    dataMap.getOrDefault(copy.getRecordItemId(), new HashMap<>())
                            .getOrDefault(copy.getProcedureStepId(), new HashMap<>())
                            .get(copy.getVersion());
            if (CollUtil.isEmpty(thisList)) {
                continue;
            }
            ProcedureStepModel procedureStepModel = copy.getReuse() ?
                    CollUtil.getFirst(itemMap.get(copy.getRecordItemId())) : stepMap.get(copy.getProcedureStepId());
            saveAndCalculateResults(thisList, copy.getVersion(), plan, procedureStepModel);
        }
    }

    @Override
    public List<ExecuteFormData> selectByPlanIdList(List<Long> planIdList) {
        return executeFormDataMapper.selectByPlanIdList(planIdList);
    }

    /**
     * 组装当前生产批次下已存的修订数量组件列表
     *
     * @param plan
     * @param recordCopyList
     * @return
     */
    private List<ExecuteFormData> generateCurrentPlanModifyCountFieldFormData(Plan plan,
                                                                              List<ExecuteRecordCopy> recordCopyList) {
        List<ExecuteFormData> executeFormData =
                executeFormDataMapper.selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                        .eq(ExecuteFormData::getProductPlanId, plan.getId())
                        .eq(ExecuteFormData::getComponentType,
                                BusinessComponentTypeEnum.BUSINESS_PRODUCT_INFO_REVISION_NUMBER.getValue()));
        // 记录项-工步-copyVersion
        Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> map =
                executeFormData.stream().collect(Collectors.groupingBy(ExecuteFormData::getRecordItemId,
                        Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                                Collectors.groupingBy(ExecuteFormData::getCopyVersion))));
        List<ExecuteFormData> insertList = new ArrayList<>();
        for (ExecuteRecordCopy copy : recordCopyList) {
            Long recordItemId = copy.getRecordItemId();
            List<ExecuteFormData> dataList =
                    map.getOrDefault(recordItemId, new HashMap<>()).getOrDefault(copy.getProcedureStepId(),
                            new HashMap<>()).get(copy.getVersion());
            if (CollUtil.isEmpty(dataList)) {
                continue;
            }
            Set<Long> existedFieldIds = new HashSet<>();
            // 处理当前页的数据
            for (ExecuteFormData data : dataList) {
                if (!existedFieldIds.contains(data.getFieldId())) {
                    existedFieldIds.add(data.getFieldId());
                    ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(copy);
                    formData.setId(IdUtils.getSnowflake());
                    formData.setFieldId(data.getFieldId());
                    formData.setComponentType(data.getComponentType());
                    formData.setValue(String.valueOf(plan.getModifyCount()));
                    formData.setCopyVersion(copy.getVersion());
                    insertList.add(formData);
                }
            }
        }
        return insertList;
    }


    @Override
    public List<ExecuteFormData> calculateData(List<ExecuteFormData> saveData, CalculateDataQueryDTO query) {
        // 根据版本id获取图
        Graph<Long> graph = batchRecordComponentService.getGraph(query.getRecordVersionId());
        Set<Long> saveFields = CollectionUtils.convertSet(saveData, ExecuteFormData::getFieldId);
        // 获取该版本图中所有数据单元格
        Set<Long> dataElementsSet = graph.getDataElements();
        saveFields.retainAll(dataElementsSet);
        // 如果要保存的数据，都不在图元素中，直接返回
        if (CollUtil.isEmpty(saveFields)) {
            return new ArrayList<>();
        }
        LocalDateTime operateTime = LocalDateTime.now();
        saveData.forEach(e -> e.setOperationTime(operateTime));
        // 查询 图中所有元素中结果组件的公式配置
        Set<Long> allFields = new HashSet<>(graph.getAllElements());
        // 查询需要计算单元格的公式
        Map<Long, BatchRecordComponent> batchRecordComponentMap = batchRecordComponentService
                .selectByRecordVersionIdAndFields(query.getRecordVersionId(), allFields, true)
                .stream()
                .collect(Collectors.toMap(BatchRecordComponent::getFieldId, Function.identity(), (t1, t2) -> t1));

        return calculateAndHandle(CalculateBasicContext.builder()
                .saveData(saveData)
                .query(query)
                .graph(graph)
                .batchRecordComponentMap(batchRecordComponentMap)
                .build());
    }


    /**
     * 计算基础上下文
     */
    @Builder
    @Getter
    public static class CalculateBasicContext {
        private List<ExecuteFormData> saveData;

        private Graph<Long> graph;

        private CalculateDataQueryDTO query;

        private Map<Long, BatchRecordComponent> batchRecordComponentMap;

    }

    private List<ExecuteFormData> calculateAndHandle(CalculateBasicContext basicContext) {
        // 查询图中所有字段的值 该字段与最新保存的字段需合并，合并
        // graph.getAllElements()根据图中所有字段，查询数据
        List<FieldConfigVO> fieldConfigs = procedureStepModelService.getFieldsConfig(basicContext.query, basicContext.graph.getAllElements());

        // 根据工步id列表查询换班列表
        List<Long> stepIds = fieldConfigs.stream().map(FieldConfigVO::getProcedureStepId).distinct().collect(Collectors.toList());
        List<ProcedureStepChangeVO> stepChangeList = instructionTeamService.queryByPlanIdAndStepIds(basicContext.getQuery().getProductPlanId(), stepIds);
        Map<Long, List<FieldConfigVO>> fieldMultiMap = CollectionUtils.convertMultiMap(fieldConfigs,
                FieldConfigVO::getFieldId);
        List<ExecuteRecordCopy> copies = executeRecordCopyService.getListByRecordVersion(basicContext.query.getProductPlanId(),
                basicContext.query.getRecordVersionId());
        //查询生产中在图中的所有记录项产生的数据R
        Set<Long> recordItemIds = CollectionUtils.convertSet(fieldConfigs, FieldConfigVO::getRecordItemId);
        List<ExecuteFormData> needData =
                CollUtil.isEmpty(recordItemIds) ? new ArrayList<>() :
                        executeFormDataMapper.selectByProductPlanIdAndItemIdsWithDiscard(basicContext.query.getProductPlanId(), recordItemIds);
        Set<Long> fieldsInDB = CollectionUtils.convertSet(needData, ExecuteFormData::getFieldId);
        // 新的数据需要取最新的
        needData.addAll(basicContext.saveData);

        List<ExecuteFormData> calculateDataList = this.calculateRelationData(basicContext, needData, copies,
                fieldMultiMap, stepChangeList);

        LocalDateTime operationTime = LocalDateTime.now();
        List<ExecuteFormData> result =
                calculateDataList.stream().map(e -> ExecuteFormDataConverter.INSTANCE.buildFormData(basicContext.query, fieldsInDB,
                operationTime, e)).collect(Collectors.toList());
        return result;
    }

    /**
     * @param valueMap       key:节点; value:引用该节点的节点列表
     * @param copies         生产计划产生的记录项
     * @param context        计算上下文
     * @return 计算结果
     */
    public List<ExecuteFormData> calculateNodeValues(Map<Long, List<Long>> valueMap,
                                                     List<ExecuteRecordCopy> copies,
                                                     CalculateContext context) {

        // 初始化入度表 各节点入度初始为0
        Map<Long, Integer> inDegree = new HashMap<>();
        for (Long node : valueMap.keySet()) {
            inDegree.put(node, 0);
        }
        Map<String, ExecuteFormData> dbDataMap = new HashMap<>(context.fullPathMap);
        // 根据记录项id、工步id、工艺班次拼接工序班次分组
        Map<Long, Map<Long, Map<String, List<ExecuteRecordCopy>>>> copyMap = copies.stream().collect(Collectors.groupingBy(ExecuteRecordCopy::getRecordItemId,
                Collectors.groupingBy(ExecuteRecordCopy::getProcedureStepId,
                        Collectors.groupingBy(copy -> copy.getProcessChangeNumber() + "-" + copy.getProcedureChangeNumber()))));
        // 计算各节点入度并更新
        for (List<Long> adjNodes : valueMap.values()) {
            for (Long adjNode : adjNodes) {
                inDegree.put(adjNode, inDegree.getOrDefault(adjNode, 0) + 1);
            }
        }

        // 初始化队列，将所有入度为0的节点加入队列
        Queue<Long> queue = new LinkedList<>();
        for (Map.Entry<Long, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            Long currentNode = queue.poll();
            // 更新所有引用当前节点的值
            for (Long adjNode : valueMap.getOrDefault(currentNode, new ArrayList<>())) {
                // 获取组件公式类型并计算
                BatchRecordComponent batchRecordComponent = context.componentMap.get(adjNode);
                if (batchRecordComponent == null) {
                    continue;
                }
                // 组件列表 按工步区分 列表
                List<FieldConfigVO> fieldConfigVOS = context.getFieldConfigMap().get(adjNode);
                // 处理同一field在不同记录页上的值
                if (CollUtil.isNotEmpty(fieldConfigVOS)) {
                    handleStepsFieldValue(context, fieldConfigVOS, copyMap, batchRecordComponent);
                }
                // 更新入度且当入度更新为0时入队
                inDegree.put(adjNode, inDegree.get(adjNode) - 1);
                if (inDegree.get(adjNode) == 0) {
                    queue.add(adjNode);
                }
            }
        }
        List<ExecuteFormData> result = new ArrayList<>();
        for (Map.Entry<String, ExecuteFormData> entry : context.getFullPathMap().entrySet()) {
            ExecuteFormData value = entry.getValue();
            if (StrUtil.isNotEmpty(value.getValue())
                    && !Objects.equals(dbDataMap.getOrDefault(getFullPath(value), new ExecuteFormData()).getValue(), value.getValue())
                    && !Objects.equals(value.getCopyVersion(), ExecuteFormDataConstant.CALCULATE_DEFAULT_COPY_VERSION)) {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * 处理同一组件在不同步骤上的计算结果
     * 含不复用、复制、换班
     * @param context
     * @param fieldConfigVOS
     * @param copyMap
     * @param batchRecordComponent
     */
    private void handleStepsFieldValue(CalculateContext context, List<FieldConfigVO> fieldConfigVOS,
                                       Map<Long, Map<Long, Map<String, List<ExecuteRecordCopy>>>> copyMap,
                                       BatchRecordComponent batchRecordComponent) {
        for (FieldConfigVO fieldConfigVO : fieldConfigVOS) {
            Long tempStepId = fieldConfigVO.getReuse() ? FORMULA_PROCEDURE_STEP_ID : fieldConfigVO.getProcedureStepId();
            Map<String, List<ExecuteRecordCopy>> changeCopyMap = copyMap.getOrDefault(fieldConfigVO.getRecordItemId()
                    , new HashMap<>()).getOrDefault(tempStepId, new HashMap<>());
            List<ProcedureStepChangeVO> changeList = context.stepChangeMap.get(fieldConfigVO.getProcedureStepId());
            for (ProcedureStepChangeVO change : changeList) {
                List<ExecuteRecordCopy> copies = changeCopyMap.get(change.getChangeNumberStr());
                context.processChangeNumber = change.getProcessChangeNumber();
                context.procedureChangeNumber = change.getProcedureChangeNumber();
                context.nodeReuse = fieldConfigVO.getReuse();
                if (CollUtil.isEmpty(copies)) {
                    handleRecordFieldValue(context, null, batchRecordComponent, fieldConfigVO);
                    continue;
                }
                for (ExecuteRecordCopy nodeCopyItem : copies) {
                    handleRecordFieldValue(context, nodeCopyItem, batchRecordComponent, fieldConfigVO);
                }
            }
        }
    }

    /**
     * 处理计算缓存field的值
     * @param context
     * @param nodeCopyItem
     * @param batchRecordComponent
     */
    private void handleRecordFieldValue(CalculateContext context, ExecuteRecordCopy nodeCopyItem,
                                        BatchRecordComponent batchRecordComponent, FieldConfigVO fieldConfigVO) {
        ExecuteFormData tempResult = getComponentCalculateResult(context, batchRecordComponent,
                nodeCopyItem, fieldConfigVO);
        if (tempResult != null) {
            tempResult.setProcedureStepId(fieldConfigVO.getReuse() ? FORMULA_PROCEDURE_STEP_ID : fieldConfigVO.getProcedureStepId());
            // 按具体位置更新
            context.getGroupMap().computeIfAbsent(tempResult.getFieldId(),
                    k -> new HashMap<>()).computeIfAbsent(tempResult.getProcedureStepId(), k -> new HashMap<>())
                    .computeIfAbsent(tempResult.getCopyVersion(), k -> new ArrayList<>()).add(tempResult);
            // 班次map值更新
            context.getChangeNumberDataMap().computeIfAbsent(context.getProcessChangeNumber(),
                            k -> new HashMap<>()).computeIfAbsent(context.getProcedureChangeNumber(), k -> new HashMap<>())
                    .computeIfAbsent(tempResult.getFieldId(), k -> new HashMap<>())
                    .computeIfAbsent(tempResult.getProcedureStepId(), k -> new HashMap<>())
                    .computeIfAbsent(tempResult.getCopyVersion(), k -> new ArrayList<>())
                    .add(tempResult);
            String path = getFullPath(tempResult);
            ExecuteFormData existed = context.getFullPathMap().get(path);
            // 过滤出原先就有的值
            if(existed == null || !Objects.equals(existed.getValue(), tempResult.getValue())) {
                context.getFullPathMap().put(path, tempResult);
                context.getLatestDataMap()
                        .computeIfAbsent(tempResult.getProcessChangeNumber(), k -> new HashMap<>())
                        .computeIfAbsent(tempResult.getProcedureChangeNumber(), k -> new HashMap<>())
                        .put(tempResult.getFieldId(), tempResult);
            }
            // 更新班次中的组件最新值
            context.getLatestDataMap().computeIfAbsent(tempResult.getProcessChangeNumber(), k -> new HashMap<>())
                    .computeIfAbsent(tempResult.getProcedureChangeNumber(), k -> new HashMap<>())
                    .put(tempResult.getFieldId(), tempResult);
            // 更新组件最新值
            context.getFieldMap().put(tempResult.getFieldId(), tempResult);
            // 更新复用组件值
            if (tempResult.getReuse()) {
                context.getReuseDataMap().put(tempResult.getFieldId(), tempResult);
            }
        }
    }

    @Builder
    @Getter
    public static class CalculateContext {
        // 计算错误值
        private String recordErrorDataValue;
        // 空值
        private String emptyData;
        // 签名公式时间格式
        private String signatureTimeFormat;
        // 数据分组map field:procedureStepId:copyVersion
        private Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> groupMap;
        // field配置map 区分步骤
        private Map<Long, List<FieldConfigVO>> fieldConfigMap;
        // 全路径值map 区分步骤和版本
        private Map<String, ExecuteFormData> fullPathMap;
        // 复用组件map
        private Map<Long, ExecuteFormData> reuseDataMap;
        // 最新值map 工艺班次:工序班次:fieldId
        private Map<Integer, Map<Integer, Map<Long, ExecuteFormData>>> latestDataMap;
        // 组件配置 用于取公式配置
        private Map<Long, BatchRecordComponent> componentMap;
        // 当前计算工艺换班班次
        private Integer processChangeNumber;
        //当前计算工序换班班次
        private Integer procedureChangeNumber;
        // 当前计算的节点所在记录步骤是否复用
        private Boolean nodeReuse;
        // 组件最新值 不区分复用复制页
        private Map<Long, ExecuteFormData> fieldMap;
        // 换班班次数据 工艺换班次数:工序换班次数:field:procedureStepId:copyVersion
        private Map<Integer, Map<Integer, Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>>>> changeNumberDataMap;
        // 步骤班次map
        private Map<Long, List<ProcedureStepChangeVO>> stepChangeMap;
    }

    /**
     * @param context 计算上下文
     * @param batchRecordComponent 当前计算的组件
     * @param copy
     * @return
     */
    private ExecuteFormData getComponentCalculateResult(CalculateContext context, BatchRecordComponent batchRecordComponent,
                                                        ExecuteRecordCopy copy, FieldConfigVO fieldConfigVO) {
        ComponentFormulaTypeEnum componentType =
                ComponentFormulaTypeEnum.getEnumByValue(batchRecordComponent.getFormulaType());

        List<FormulaFieldDTO> formulaFieldList = JsonUtils.parseArray(
                batchRecordComponent.getFormulaField(),
                FormulaFieldDTO.class
        );
        // 取出节点计算所需值
        List<ExecuteFormData> fieldsValue;
        // 签名公式取值特殊 需要带出历史值
        if (Objects.equals(componentType, ComponentFormulaTypeEnum.SIGN)) {
            fieldsValue = getFieldsValueWithHistory(formulaFieldList,context, batchRecordComponent, copy);
        } else {
            ComponentFormulaConfig formulaConfig = batchRecordComponent.getFormulaConfig();
            if (formulaConfig != null &&
                    Objects.equals(formulaConfig.getValueTakeType(), FormulaValueTakeTypeEnum.ALL_EFFECTIVE.getValue())) {
                fieldsValue = getFieldsAllEffectiveValue(formulaFieldList,context);
            } else {
                fieldsValue = getFieldsValue(formulaFieldList,context, batchRecordComponent, copy);
            }
        }
        List<String> keyList = CollectionUtils.convertList(formulaFieldList, FormulaFieldDTO::getKey);
        CalculateResult calculateResult = new CalculateResult();
        boolean allEmpty = false;
        try {
            fieldsValue = fieldsValue.stream()
                    .filter(item -> item != null && StrUtil.isNotEmpty(item.getValue()))
                    .collect(Collectors.toList());
            // 完全无参数值则返回不计算
            if (CollUtil.isEmpty(fieldsValue)) {
                return null;
            }
            allEmpty = fieldsValue.stream().allMatch(e -> BooleanUtil.isTrue(e.getEmptyValue())
                    || StrUtil.equals(e.getValue(), context.getEmptyData()));
            // 空值、空字符串、参数配置的空值都赋值为公式的默认值
            // 复制一份使用 不修改原值
            List<CalculateParam> tempValueList = BeanUtil.copyToList(fieldsValue, CalculateParam.class);
            for (CalculateParam e : tempValueList) {
                e.setTimeFormat(context.getSignatureTimeFormat());
                if (BooleanUtil.isTrue(e.getEmptyValue()) || (StrUtil.equals(e.getValue(), context.getEmptyData()))
                        || StrUtil.isEmpty(e.getValue())) {
                    e.setValue(componentType.getDefaultEmptyValue());
                }
                e.setEmptyValue(e.getEmptyValue());
            }
            calculateResult = allEmpty ? calculateResult.emptyValue(getEmptyResult(batchRecordComponent,
                    context.getEmptyData())) : componentType.getFunction().apply(batchRecordComponent, keyList,
                    tempValueList, expressionCalculator::evaluate);
        } catch (Exception e) {
            log.error("公式计算错误", e);
            // 参数全为配置空值 结果为空值
            if (allEmpty) {
                calculateResult.emptyValue(getEmptyResult(batchRecordComponent, context.getEmptyData()));
            } else {
                calculateResult.calculateError(context.getRecordErrorDataValue());
            }
        }
        if (calculateResult == null || StrUtil.isEmpty(calculateResult.getValue())) {
            return null;
        }
        ExecuteFormData result = new ExecuteFormData();
        result.setValue(calculateResult.getValue());
        result.setExtInfo(calculateResult.getExtInfo());
        result.setValueExtension(calculateResult.getExtInfo());
        result.setFieldId(batchRecordComponent.getFieldId());
        result.setOperationUser(SysUserHolder.getUser().getUserId());
        // 按照复制记录来生成值 如果没有复制记录 说明还没进入该节点 生成一条通用值
        boolean noCopy = copy == null;
        result.setOperationTime(LocalDateTime.now());
        result.setCopyVersion(noCopy ? ExecuteFormDataConstant.CALCULATE_DEFAULT_COPY_VERSION : copy.getVersion());
        result.setReuse(fieldConfigVO.getReuse());
        result.setRecordItemId(fieldConfigVO.getRecordItemId());
        result.setComponentType(batchRecordComponent.getComponentType());
        result.setProcessChangeNumber(context.processChangeNumber);
        result.setProcedureChangeNumber(context.procedureChangeNumber);
        result.setEmptyValue(calculateResult.getEmptyValue());
        return result;
    }

    private String getEmptyResult(BatchRecordComponent batchRecordComponent, String emptyData) {
        if (StrUtil.equals(batchRecordComponent.getComponentType(), BasicComponentTypeEnum.CHECKBOX.getValue())) {
            return JsonUtils.toJsonString(Collections.singletonList(emptyData));
        }
        return emptyData;
    }

    /**
     * 获取所有有效值 如同一个组件存在记录复制记录非复用等多个数据来源 全部去除
     * @param fields
     * @param context
     * @return
     */
    private List<ExecuteFormData> getFieldsAllEffectiveValue(List<FormulaFieldDTO> fields,
                                                             CalculateContext context) {
        return fields.stream().map(e -> {
            FieldConfigVO fieldConfigVO = CollUtil.getFirst(context.getFieldConfigMap().get(e.getFieldId()));
            if (fieldConfigVO == null) {
                return null;
            }
            // 在所有有效取值方式下 不区分当前页和其他页 取出所有复制或复用页当前组件的有效值
            Map<Long, Map<Long, List<ExecuteFormData>>> fieldDataMap = context.getGroupMap().getOrDefault(e.getFieldId(), new HashMap<>());
            return fieldDataMap.values()
                    .stream()
                    .map(Map::values)
                    .flatMap(Collection::stream)
                    .map(CollUtil::getLast)
                    .collect(Collectors.toList());
        }).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 非常规取值 暂时只有签名公式用 需要取得组件的历史值
     * @param fields
     * @param context
     * @param batchRecordComponent
     * @param copy
     * @return
     */
    private List<ExecuteFormData> getFieldsValueWithHistory(List<FormulaFieldDTO> fields,
                                                            CalculateContext context,
                                                            BatchRecordComponent batchRecordComponent,
                                                            ExecuteRecordCopy copy) {

        return fields.stream().map(e -> {
            FieldConfigVO fieldConfigVO = CollUtil.getFirst(context.getFieldConfigMap().get(e.getFieldId()));
            if (fieldConfigVO == null) {
                return null;
            }
            // 若是当前页则取当前页数据 否则取最新数据
            if (Objects.equals(batchRecordComponent.getRecordItemId(), fieldConfigVO.getRecordItemId())) {
                if (copy != null) {
                    if (BooleanUtil.isTrue(context.nodeReuse)) {
                        return context.getGroupMap()
                                .getOrDefault(e.getFieldId(), new HashMap<>())
                                .getOrDefault(ExecuteFormDataConstant.FORMULA_PROCEDURE_STEP_ID, new HashMap<>())
                                .getOrDefault(copy.getVersion(), new ArrayList<>());
                    }
                    return context.getChangeNumberDataMap().getOrDefault(context.getProcessChangeNumber(), new HashMap<>())
                            .getOrDefault(context.getProcedureChangeNumber(), new HashMap<>())
                            .getOrDefault(e.getFieldId(), new HashMap<>())
                             .getOrDefault(copy.getProcedureStepId(), new HashMap<>()).get(copy.getVersion());
                } else {
                    return null;
                }
            } else {
                // 签名公式限制只允许关联当前页 不存在其他页数据 所以直接返回空
                return null;
            }
        }).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 常规取值 只取某个组件最新值
     * @param fields
     * @param context
     * @param batchRecordComponent
     * @param copy
     * @return
     */
    private List<ExecuteFormData> getFieldsValue(List<FormulaFieldDTO> fields, CalculateContext context,
                                                 BatchRecordComponent batchRecordComponent, ExecuteRecordCopy copy) {
        return fields.stream().map(e -> {
            FieldConfigVO fieldConfigVO = CollUtil.getFirst(context.getFieldConfigMap().get(e.getFieldId()));
            if (fieldConfigVO == null) {
                return null;
            }
            // 若是当前页则取当前页数据 否则取最新数据
            if (Objects.equals(batchRecordComponent.getRecordItemId(), fieldConfigVO.getRecordItemId())) {
                boolean copyNull = copy == null;
                // 复用时 忽略掉班次信息
                if (BooleanUtil.isTrue(context.nodeReuse)) {
                    return CollUtil.getLast(context.getGroupMap()
                            .getOrDefault(e.getFieldId(), new HashMap<>())
                            .getOrDefault(ExecuteFormDataConstant.FORMULA_PROCEDURE_STEP_ID, new HashMap<>())
                            .getOrDefault(copyNull ? ExecuteFormDataConstant.CALCULATE_DEFAULT_COPY_VERSION : copy.getVersion(), new ArrayList<>()));

                }
                 if (!copyNull) {
                    return CollUtil.getLast(context.getChangeNumberDataMap()
                            .getOrDefault(context.getProcessChangeNumber(), new HashMap<>())
                            .getOrDefault(context.getProcedureChangeNumber(), new HashMap<>())
                            .getOrDefault(e.getFieldId(), new HashMap<>())
                            .getOrDefault(copy.getProcedureStepId(), new HashMap<>())
                            .get(copy.getVersion()));
                } else {
                    return CollUtil.getLast(context.getChangeNumberDataMap().getOrDefault(context.getProcessChangeNumber(), new HashMap<>())
                            .getOrDefault(context.getProcedureChangeNumber(), new HashMap<>())
                            .getOrDefault(e.getFieldId(), new HashMap<>())
                            .getOrDefault(fieldConfigVO.getProcedureStepId(), new HashMap<>())
                            .get(ExecuteFormDataConstant.CALCULATE_DEFAULT_COPY_VERSION));
                }
            } else {
                // 若结果页为复用 则查询所有值中最新的(不区分班次不区分复用不区分复制)
                if (BooleanUtil.isTrue(context.nodeReuse)) {
                    return context.getFieldMap().get(e.getFieldId());
                }
                // 若结果页不复用 取复用的及当前班次的数据中最新的
                ExecuteFormData executeFormData = context.getLatestDataMap().getOrDefault(context.getProcessChangeNumber(), new HashMap<>())
                        .getOrDefault(context.getProcedureChangeNumber(), new HashMap<>()).get(e.getFieldId());
                ExecuteFormData reuse = context.getReuseDataMap().get(e.getFieldId());
                if (executeFormData != null && reuse != null) {
                    return executeFormData.getOperationTime().isAfter(reuse.getOperationTime()) ? executeFormData : reuse;
                }
                return executeFormData == null ? reuse : executeFormData;
            }
        }).collect(Collectors.toList());
    }


    /**
     * @param basicContext    基础上下文
     * @param currentDataList 保存值与查询值列表合集
     * @param copies          产生的记录页
     * @param fieldConfigMap  组件配置map
     * @param stepChangeList  步骤的班次列表
     * @return
     */
    private List<ExecuteFormData> calculateRelationData(CalculateBasicContext basicContext,
                                                        List<ExecuteFormData> currentDataList,
                                                        List<ExecuteRecordCopy> copies,
                                                        Map<Long, List<FieldConfigVO>> fieldConfigMap,
                                                        List<ProcedureStepChangeVO> stepChangeList) {
        Map<Long, ExecuteFormData> fieldDataMap = currentDataList.stream()
                .collect(Collectors.toMap(
                        ExecuteFormData::getFieldId, // 按 id 分组
                        e -> e, // 当前元素
                        (existing, replacement) -> existing.getOperationTime().isBefore(replacement.getOperationTime()) ? replacement : existing // 保留操作时间最新的值
                ));
        // 处理复用数据
        Map<Long, ExecuteFormData> reuseFieldDataMap = getReuseFieldDataMap(currentDataList);
        // 按照工艺班次工序班次进行分组
        Map<Integer, Map<Integer, Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>>>> currentDataMap = getChangeNumberDataMap(currentDataList);
        // 按照fieldId和procedureStepId和copyVersion先后分组并取最新值
        // 此处按时间升序排 取值取最新 更新时往list后追加元素 因为签名公式及后续公式会用到历史值 所以需要将历史值也往下传
        Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> groupMap = getAllDataMap(currentDataList);
        // 按照班次区分的最新数据map
        Map<Integer, Map<Integer, Map<Long, ExecuteFormData>>> latestDataMap = getLatestDataMap(currentDataList);
        // 初始化全路径值map 用于确定唯一值及保存结果
        Map<String, ExecuteFormData> fullPathMap = initFullPathMap(currentDataList);
        // 图引用关系
        Map<Long, List<Long>> valueMap = basicContext.graph.getValueMap();
        // 公式计算错误时的错误值
        String recordErrorDataValue = platformParameterClientImpl.getValueByCode(recordErrorData);
        String emptyData = platformParameterClientImpl.getValueByCode(recordEmptyDataCode);
        String signatureTimeFormat = platformParameterClientImpl.getValueByCode(BusinessParameterCodeConstants.PLATFORM_SIGNATURE_TIME_FORMAT);
        // 组装计算上下文
        CalculateContext context = CalculateContext.builder()
                .recordErrorDataValue(recordErrorDataValue)
                .emptyData(emptyData)
                .groupMap(groupMap)
                .fieldConfigMap(fieldConfigMap)
                .fullPathMap(fullPathMap)
                .componentMap(basicContext.batchRecordComponentMap)
                .changeNumberDataMap(currentDataMap)
                .latestDataMap(latestDataMap)
                .reuseDataMap(reuseFieldDataMap)
                .fieldMap(fieldDataMap)
                .stepChangeMap(CollectionUtils.convertMultiMap(stepChangeList, ProcedureStepChangeVO::getProcedureStepId))
                .signatureTimeFormat(signatureTimeFormat)
                .build();
        // 计算每个节点的值
        return calculateNodeValues(valueMap, copies, context);
    }


    private Map<Integer, Map<Integer, Map<Long, ExecuteFormData>>> getLatestDataMap(List<ExecuteFormData> currentDataList) {
        Map<Integer, Map<Integer, Map<Long, ExecuteFormData>>> latestDataMap =
                currentDataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getProcessChangeNumber,
                Collectors.groupingBy(ExecuteFormData::getProcedureChangeNumber,
                        Collectors.groupingBy(ExecuteFormData::getFieldId,
                        Collectors.collectingAndThen(
                                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(ExecuteFormData::getOperationTime)), Optional::get),
                                obj -> obj
                        )
                ))));
        return latestDataMap;
    }

    @NotNull
    private static Map<String, ExecuteFormData> initFullPathMap(List<ExecuteFormData> currentDataList) {
        Map<String, ExecuteFormData> fullPathMap = new HashMap<>();
        currentDataList.forEach(e->{
            String path = getFullPath(e);
            ExecuteFormData executeFormData = fullPathMap.get(path);
            if (executeFormData != null) {
                if (e.getOperationTime().isAfter(executeFormData.getOperationTime())) {
                    fullPathMap.put(path, e);
                }
            } else {
                fullPathMap.put(path, e);
            }
        });
        return fullPathMap;
    }

    @NotNull
    private static String getFullPath(ExecuteFormData e) {
        return e.getFieldId() + StrUtil.DASHED + e.getProcedureStepId() + e.getCopyVersion();
    }

    @NotNull
    private static Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> getAllDataMap(List<ExecuteFormData> currentDataList) {
        Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> groupMap =
                currentDataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getFieldId,
                                Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                                        Collectors.groupingBy(ExecuteFormData::getCopyVersion,
                                                Collectors.collectingAndThen(Collectors.toList(), list -> {
                                                    list.sort(Comparator.comparing(ExecuteFormData::getOperationTime));
                                                    return list;
                                                })
                                        )
                                )
                        )
                );
        return groupMap;
    }

    /**
     * 获取按班次数据分组map
     * @param dataList
     * @return
     */
    private static Map<Integer, Map<Integer, Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>>>> getChangeNumberDataMap(List<ExecuteFormData> dataList) {
        Map<Integer, Map<Integer, Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>>>> collect = dataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getProcessChangeNumber,
                Collectors.groupingBy(ExecuteFormData::getProcedureChangeNumber,
                        Collectors.groupingBy(ExecuteFormData::getFieldId,
                        Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                        Collectors.groupingBy(ExecuteFormData::getCopyVersion,
                                Collectors.collectingAndThen(Collectors.toList(), list -> {
                            list.sort(Comparator.comparing(ExecuteFormData::getOperationTime));
                            return list;
                        })))))));
        return collect;
    }

    /**
     * 处理复用数据 取最新
     * @param currentDataList
     * @return
     */
    private Map<Long, ExecuteFormData> getReuseFieldDataMap(List<ExecuteFormData> currentDataList) {
        Map<Long, ExecuteFormData> map = new HashMap<>();
        currentDataList.forEach(e->{
            if (e.getReuse()) {
                map.compute(e.getFieldId(), (aLong, executeFormData) -> {
                    if (executeFormData == null) {
                        return e;
                    }
                    return executeFormData.getOperationTime().isAfter(e.getOperationTime()) ? executeFormData : e;
                });
            }
        });
        return map;
    }

    @Override
    public List<FormDataVO> getFieldList(FormDataListQueryDTO dto) {
        List<ExecuteFormData> list = executeFormDataMapper.selectListByCondition(dto);
        return ExecuteFormDataConverter.INSTANCE.convertList(list);
    }

    @Override
    public List<FormDataItemVO> getRecordItemLatestData(RecordItemLatestDataQueryDTO dto) {
        List<ExecuteFormData> dataList = executeFormDataMapper.selectByStep(dto);
        //判断是否是存在图片组件，图片组件查询图片信息
        String emptyData = platformParameterClientImpl.getValueByCode(recordEmptyDataCode);
        List<ExecuteFormData> executeFormData = CollectionUtils.filterList(dataList, item ->
                StrUtil.equals(item.getComponentType(), BasicComponentTypeEnum.PHOTO.getValue()) &&
                !item.getValue().equals(emptyData));
        List<AttachmentVO> attachmentVOList = executeAttachmentService.getListByIdList(executeFormData);
        return ExecuteFormDataConverter.INSTANCE.filterLatestAndConvert(dataList,attachmentVOList,emptyData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyRecordItem(RecordCopySaveDTO dto) {
        ExecuteRecordCopy copyVersion = executeRecordCopyService.copyRecordItem(dto);
        CalculateDataQueryDTO build = CalculateDataQueryDTO.builder()
                .copyVersion(copyVersion.getVersion())
                .batchNo(dto.getBatchNo())
                .recordVersionId(dto.getRecordVersionId())
                .processId(dto.getProcessId())
                .processVersion(dto.getProcessVersion())
                .productPlanId(dto.getProductPlanId())
                .procedureStepId(dto.getProcedureStepId())
                .reuse(dto.getReuse())
                .recordItemId(dto.getRecordItemId())
                .build();
        // 处理复制的记录页计算数据
        ExecuteRecordCopy copy = BeanUtil.copyProperties(copyVersion, ExecuteRecordCopy.class);
        copy.setProcedureStepId(dto.getProcedureStepId());
        calculateAndSaveWithNoParam(build, copy);
        return copyVersion.getVersion();
    }

    /**
     * 在无数值更新保存的情况下计算当前复制的记录页的数据
     *
     * @param query
     * @param copy
     * @return
     */
    private List<ExecuteFormData> calculateDataWithNoParams(CalculateDataQueryDTO query, ExecuteRecordCopy copy) {
        // 根据版本id获取图
        Graph<Long> graph = batchRecordComponentService.getGraph(query.getRecordVersionId());
        // 查询 图中所有元素中结果组件的公式配置
        Set<Long> allFields = new HashSet<>(graph.getAllElements());
        if (CollectionUtil.isEmpty(allFields)){
            return new ArrayList<>();
        }
        // 查询需要计算单元格的公式
        Map<Long, BatchRecordComponent> batchRecordComponentMap = batchRecordComponentService
                .selectByRecordVersionIdAndFields(query.getRecordVersionId(), allFields, true)
                .stream()
                .collect(Collectors.toMap(BatchRecordComponent::getFieldId, Function.identity(), (t1, t2) -> t1));
        List<ExecuteFormData> executeFormData = calculateAndHandle(CalculateBasicContext.builder()
                .saveData(new ArrayList<>())
                .query(query)
                .graph(graph)
                .batchRecordComponentMap(batchRecordComponentMap)
                .build());
        return executeFormData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CopyRecordItemVO> getCopyVersionList(RecordCopyQueryDTO dto) {
        List<CopyRecordItemVO> versionList = executeRecordCopyService.getCopyVersionList(dto);
        // 处理辅助记录功能节点
        handleSubRecord(dto);
        if (CollUtil.isEmpty(versionList)) {
            if (!batchRecordItemService.existRecordItem(dto.getRecordVersionId(), dto.getRecordItemId())) {
                throw new BmosException(MesResponseCode.RECORD_ITEM_NOT_EXIST);
            }
            //获取当前version最大值
            Long versionMaxValue = Optional.ofNullable(executeRecordCopyService.getVersionMaxValue(dto)).orElse(-1L);
            Plan plan = planService.getById(dto.getProductPlanId());
            ExecuteRecordCopy insert = ExecuteFormDataConverter.INSTANCE.convertCopy(dto, plan, versionMaxValue);
            executeRecordCopyService.save(insert);
            CopyRecordItemVO vo = new CopyRecordItemVO();
            vo.setVersion(dto.getReuse() ? DEFAULT_COPY_VERSION : versionMaxValue + 1);
            vo.setDiscard(false);
            CalculateDataQueryDTO build = CalculateDataQueryDTO.builder()
                    .copyVersion(vo.getVersion())
                    .batchNo(plan.getBatchNo())
                    .recordVersionId(dto.getRecordVersionId())
                    .processId(plan.getProcessId())
                    .processVersion(plan.getProcessVersion())
                    .productPlanId(dto.getProductPlanId())
                    .procedureStepId(dto.getProcedureStepId())
                    .reuse(dto.getReuse())
                    .recordItemId(dto.getRecordItemId())
                    .build();
            ExecuteRecordCopy copy = BeanUtil.copyProperties(insert, ExecuteRecordCopy.class);
            copy.setProcedureStepId(dto.getProcedureStepId());
            calculateAndSaveWithNoParam(build, copy);
            if (insert.getVersion() > 0){
                // 复制业务组件实例(businessComponent会在下发指令时初始化好 所以这里只考虑复制版本的情况)
                businessComponentManager.copyComponentInstance(dto.getProductPlanId(),
                        dto.getProcedureStepId(),
                        dto.getRecordItemId(),
                        dto.getRecordVersionId(),
                        dto.getReuse(),
                        insert.getVersion() - 1,
                        insert.getVersion());
            }
            return Collections.singletonList(vo);
        }
        return versionList;
    }

    /**
     * 进入记录页无参数计算结果值 并且记录异常
     * @param build
     * @param copy
     */
    private void calculateAndSaveWithNoParam(CalculateDataQueryDTO build, ExecuteRecordCopy copy) {
        List<ExecuteFormData> resultData = calculateDataWithNoParams(build, copy);
        if (CollUtil.isNotEmpty(resultData)) {
            Long nextRev = selectMaxRev(build.getProductPlanId(), CollectionUtils.convertSet(resultData, ExecuteFormData::getFieldId));
            resultData.forEach(e -> e.setRev(nextRev));
            executeFormDataMapper.insertBatch(resultData);
        }
        exceptionAutoRecord(new ArrayList<>(), resultData, copy.getProcedureStepId());
    }

    /**
     * 处理辅助记录功能节点
     * @param dto
     */
    private void handleSubRecord(RecordCopyQueryDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (!Objects.equals(procedureStepModel.getNodeFunction(), ProcedureStepNodeFunctionEnum.SUB_RECORD.getValue())) {
            return;
        }
        SubRecordNodeQueryDTO build = SubRecordNodeQueryDTO.builder()
                .productPlanId(dto.getProductPlanId())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .processChangeNumber(dto.getProcessChangeNumber())
                .procedureChangeNumber(dto.getProcedureChangeNumber()).build();
        if(executeSubsidiaryRecordMapper.existedRecordNode(build)) {
            return;
        }
        ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
        ExecuteSubsidiaryRecord subRecord = new ExecuteSubsidiaryRecord();
        subRecord.setProductPlanId(dto.getProductPlanId());
        subRecord.setProcedureName(procedureModel.getName());
        subRecord.setProcedureModelId(procedureModel.getId());
        subRecord.setProcedureStepId(procedureStepModel.getProcedureStepId());
        subRecord.setProcedureStepModelId(procedureStepModel.getId());
        subRecord.setReuse(procedureStepModel.getReusable());
        subRecord.setProcedureStepName(procedureStepModel.getName());
        subRecord.setRecordItemId(procedureStepModel.getRecordItemId());
        subRecord.setRecordVersionId(procedureStepModel.getRecordVersionId());
        subRecord.setProcessChangeNumber(dto.getProcessChangeNumber());
        subRecord.setProcedureChangeNumber(dto.getProcedureChangeNumber());
        subRecord.setStartTime(LocalDateTime.now());
        executeSubsidiaryRecordMapper.insert(subRecord);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void discardRecordItem(FormDataDiscardDTO dto) {
        //作废记录项
        executeRecordCopyService.discardRecordItem(dto);
        //作废记录项数据
        executeFormDataMapper.discardFields(dto);
    }

    @Override
    public List<IntactFormDataVO> getIntactMergedList(IntactMergeListQueryDTO dto) {
        //内容
        List<IntactFormDataVO> records = procedureStepModelService.getRecordContents(dto);

        Set<Long> recordVersionIds =
                records.stream().map(IntactFormDataVO::getRecordVersionId).collect(Collectors.toSet());
        List<BatchRecordItem> headerFooters =
                batchRecordItemService.getHeaderFooterByRecordVersionIds(recordVersionIds);
        Map<Long, List<BatchRecordItem>> headerFootersMap = CollectionUtils.convertMultiMap(headerFooters,
                BatchRecordItem::getRecordVersionId);
        //排序
        List<ProcessRecordOrder> orders = processRecordOrderService.getRecordItems(dto.getProcessId(),
                dto.getProcessVersion());
        Map<Long, Map<Long, Long>> orderMap = orders.stream()
                .collect(Collectors.groupingBy(ProcessRecordOrder::getRecordItemId,
                        Collectors.groupingBy(ProcessRecordOrder::getProcedureStepModelId,
                                Collectors.collectingAndThen(
                                        Collectors.collectingAndThen(
                                                Collectors.maxBy(Comparator.comparing(ProcessRecordOrder::getRecordItemOrder)),
                                                Optional::get),
                                        ProcessRecordOrder::getRecordItemOrder)
                        )
                        )
                );

        //数据
        List<ExecuteFormData> dataList = executeFormDataMapper.selectByProductPlanId(dto.getProductPlanId());
        // 只根据步骤id分组会将不同copyVersion的版本数据都取出来 所以先根据记录分组 再根据步骤id分组 再根据copyVersion分组
        Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> dataMap =
                dataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getRecordItemId,
                        Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                                Collectors.groupingBy(ExecuteFormData::getCopyVersion))));
        //附件
        List<ExecuteAttachment> attachments = executeAttachmentService.getListByProductPlanId(dto.getProductPlanId());
        Map<Long, Map<Long, Map<Long, List<ExecuteAttachment>>>> attachmentMap = attachments.stream().collect(Collectors.groupingBy(ExecuteAttachment::getRecordItemId,
                Collectors.groupingBy(ExecuteAttachment::getProcedureStepId,
                        Collectors.groupingBy(ExecuteAttachment::getCopyVersion))));
        //填充
        for (int i = 0; i < records.size(); i++) {
            IntactFormDataVO e = records.get(i);
            Long order = orderMap.getOrDefault(e.getRecordItemId(), new HashMap<>()).getOrDefault(e.getReuse() ? 0L :
                    e.getProcedureStepModelId(), e.getProcedureStepModelId());
            e.setOrder(order == null ? i : order);
            List<ExecuteFormData> data = dataMap.getOrDefault(e.getRecordItemId(), new HashMap<>())
                    .getOrDefault(e.getProcedureStepId(), new HashMap<>()).getOrDefault(e.getCopyVersion(),
                            new ArrayList<>());
            if (CollUtil.isNotEmpty(data)) {
                e.setDataList(ExecuteFormDataConverter.INSTANCE.convertList2(data));
            }
            List<ExecuteAttachment> orDefault = attachmentMap.getOrDefault(e.getRecordItemId(), new HashMap<>())
                    .getOrDefault(e.getProcedureStepId(), new HashMap<>()).getOrDefault(e.getCopyVersion(),
                            new ArrayList<>());
            if (CollUtil.isNotEmpty(orDefault)) {
                e.setAttachments(ExecuteAttachmentConvert.INSTANCE.convertList2(orDefault));
            }
            List<BatchRecordItem> headerFooter = headerFootersMap.get(e.getRecordVersionId());
            if (CollUtil.isNotEmpty(headerFooter)) {
                e.setHeaderContent(headerFooter.stream().filter(header -> RecordItemTypeEnum.HEADER_TYPE.getType().equals(header.getItemType())).findFirst().orElse(new BatchRecordItem()).getFileContent());
                e.setFooterContent(headerFooter.stream().filter(header -> RecordItemTypeEnum.FOOTER_TYPE.getType().equals(header.getItemType())).findFirst().orElse(new BatchRecordItem()).getFileContent());
            }
        }
        records.sort(Comparator.comparing(IntactFormDataVO::getOrder));
        return records;
    }

    @Override
    public String getServerTime() {
        return LocalDateTimeUtil.formatNormal(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBusinessComponentsData(BusinessComponentBatchSaveDTO dto) {
        List<ExecuteFormData> results = new ArrayList<>();
        Long productPlanId = dto.getProductPlanId();
        ProductionDetailInfo info = new ProductionDetailInfo();
        Plan plan = planService.getById(productPlanId);
        ProductMaterial productMaterial = productMaterialService.selectById(plan.getProductId());
        ProcessDetailInfo processDetailInfo = processVersionService.getProcessDetailInfo(plan.getProcessId(),
                plan.getProcessVersion());
        ProductFormulaInfo formulaInfo =
                formulaConfigureService.getProductFormulaInfo(processDetailInfo.getFormulaVersionId());
        Long procedureStepModelId = dto.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        info.setPlan(plan);
        info.setDto(dto);
        info.setProduct(productMaterial);
        info.setProcess(processDetailInfo);
        info.setFormulaInfo(formulaInfo);
        info.setUnitCache(unitCache);
        List<ComponentListVO> tree =
                batchRecordComponentService.selectAutoFillComponentTree(procedureStepModel.getRecordVersionId(),
                        procedureStepModel.getRecordItemId());
        tree.forEach(component -> {
            strategyMap.get(component.getComponentType()).handleBusinessComponent(results, component, info, configMap
                    , null);
        });
        results.forEach(e -> {
            e.setOperationType(ExecuteFormDataType.SAVE.getValue());
            e.setOperationUser(SysUserHolder.getUser().getUserId());
            e.setOperationTime(LocalDateTime.now());
            e.setSystemCreate(true);
            e.setProcedureChangeNumber(dto.getProcedureChangeNumber());
            e.setProcessChangeNumber(dto.getProcessChangeNumber());
        });
        if (CollUtil.isNotEmpty(results)) {

            List<ExecuteFormData> resultData = calculateData(results,
                    ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
            if (CollUtil.isNotEmpty(resultData)) {
                executeFormDataMapper.insertBatch(resultData);
            }
            executeFormDataMapper.insertBatch(results);
        }
    }

    @Override
    public Boolean checkBusinessComponentsSaved(BusinessComponentsCheckSavedDTO dto) {
        Long procedureStepModelId = dto.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        Long recordItemId = procedureStepModel.getRecordItemId();
        Long recordVersionId = procedureStepModel.getRecordVersionId();
        List<BatchRecordComponent> components = batchRecordComponentService.selectByVersionAndItem(recordVersionId,
                recordItemId);
        Set<String> basicComponentSet = Arrays.stream(BasicComponentTypeEnum.values())
                .map(BasicComponentTypeEnum::getValue)
                .collect(Collectors.toSet());
        List<Long> fieldIds = components.stream()
                .filter(component -> !basicComponentSet.contains(component.getComponentType()))
                .map(BatchRecordComponent::getFieldId)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(fieldIds)) {
            return true;
        }
        dto.setFieldIds(fieldIds);
        return executeFormDataMapper.existedCurrentStepRecordData(procedureStepModel, dto.getProductPlanId(),
                dto.getCopyVersion(), fieldIds);
    }

    @Override
    public void insertBatch(List<ExecuteFormData> results) {
        executeFormDataMapper.insertBatch(results);
    }

    @Override
    public Boolean existHistoryData(List<ExecuteFormData> results) {
        return executeFormDataMapper.existHistoryData(results);
    }

    @Override
    public Long selectMaxRev(Long productPlanId, Set<Long> fields) {
        Long rev = executeFormDataMapper.selectMaxRev(productPlanId, fields);
        return rev == null ? 0L : rev + 1L;
    }

    @Override
    public void  saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto) {
        saveResultsAndHandleRelationComponentData(results, dto, true);
    }

    @Override
    public void  saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto, boolean filterNull) {
        this.saveResultsAndHandleRelationComponentData(results, dto.getProductPlanId(), dto.getProcedureStepModelId(), dto.getCopyVersion(), filterNull);
    }

    @Override
    public void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long productPlanId,
                                                          Long procedureStepModelId, Long copyVersion) {
        saveResultsAndHandleRelationComponentData(results, productPlanId, procedureStepModelId, copyVersion, true);
    }

    @Override
    public void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long productPlanId,
                                                          Long procedureStepModelId, Long copyVersion, boolean filterNull) {
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                productPlanId));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        Plan plan = planService.getById(productPlanId);
        try {
            saveAndCalculateResults(results, copyVersion, plan, procedureStepModel, filterNull);
            this.handlePlanModifyCount(productPlanId);
        } catch (DuplicateKeyException e) {
            log.info("生产计划[{}]记录数据重复:{}", productPlanId, e.getMessage());
            throw new BmosException(MesResponseCode.EXECUTE_DATA_EXIST);
        } catch (DataIntegrityViolationException e) {
            handleDataTooLong(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void saveResultsAndHandleRelationWithExceptionRecord(List<ExecuteFormData> results, Long productPlanId,Long componentId,
                                                                Long stepModelId, Long copyVersion) {
        results =
                results.stream().filter(e -> Objects.nonNull(e) && StrUtil.isNotEmpty(e.getValue())).collect(Collectors.toList());
        if (CollUtil.isEmpty(results)) {
            return;
        }
        RLock rLock = getrLock(productPlanId);
        try {
            ProcedureStepModel procedureStepModel = procedureStepModelService.getById(stepModelId);
            Plan plan = planService.getById(productPlanId);
            List<ExecuteFormData> calculateResults = saveAndCalculateResults(results, copyVersion,
                    plan, procedureStepModel);
            ComponentListVO componentListVO =
                    batchRecordComponentService.selectUsedComponentDetail(procedureStepModel.getRecordVersionId(),
                            procedureStepModel.getRecordItemId(), componentId);
            String configJson = procedureStepConfigService.getComponentConfigJson(stepModelId, componentId, procedureStepModel.getReusable(),
                    procedureStepModel.getProcessId(), procedureStepModel.getProcessVersion());
            Map<Long, ScopeLimitConfig> configMap = parseComponentExceptionRecordConfig(componentListVO, configJson);
            handleBusinessExceptionAutoRecord(results, calculateResults, procedureStepModel.getProcedureStepId(),
                    configMap, plan);
            this.handlePlanModifyCount(productPlanId);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 处理业务组件异常值记录
     * 与基础组件异常自动记录区别在阈值配置取值方式不同
     *
     * @param results          业务组件结果
     * @param calculateList 关联计算结果
     * @param procedureStepId  业务组件填报工步id
     * @param businessConfigMap 业务组件子组件阈值配置 已经过滤出自动记录
     * @param plan
     */
    private void handleBusinessExceptionAutoRecord(List<ExecuteFormData> results, List<ExecuteFormData> calculateList,
                                                   Long procedureStepId, Map<Long, ScopeLimitConfig> businessConfigMap,
                                                   Plan plan) {
        // 处理异常自动录入 过滤出录入的N/A空值
        String valueByCode = platformParameterClientImpl.getValueByCode(recordEmptyDataCode);
        results = results.stream().filter(e-> !Objects.equals(e.getValue(), valueByCode)).collect(Collectors.toList());
        calculateList = calculateList.stream().filter(e-> !Objects.equals(e.getValue(), valueByCode)).collect(Collectors.toList());
        List<ExecuteException> executeExceptions = new ArrayList<>();
        if (CollUtil.isNotEmpty(results)) {
            // 处理业务组件值异常
            for (ExecuteFormData result : results) {
                ScopeLimitConfig scopeLimitConfig = businessConfigMap.get(result.getFieldId());
                if (scopeLimitConfig == null) {
                    continue;
                }
                String exceptionDescription = scopeLimitConfig.checkNumberScopeLimit(result.getValue());
                if (StrUtil.isEmpty(exceptionDescription)) {
                    continue;
                }
                ExecuteException executeException = getExecuteException(result, exceptionDescription, plan);
                executeException.setProcedureStepId(procedureStepId);
                executeExceptions.add(executeException);
            }
        }
        // 处理计算值
        if (CollUtil.isNotEmpty(calculateList)) {
            List<ProcedureStepConfig> configs =
                    procedureStepConfigService.getListByProcessVersionAndFields(plan.getProcessId(),
                            plan.getProcessVersion(), CollectionUtils.convertList(calculateList, ExecuteFormData::getFieldId));
            Map<Long, Map<Long, ProcedureStepConfig>> configMap = getStepFieldConfigMap(configs);
            Map<Long, Long> itemStepMap = getOrderItemStepMap(plan.getProcessId(), plan.getProcessVersion());
            for (ExecuteFormData executeFormData : calculateList) {
                ExecuteException executeException = getExceptionWithBasicInfo(executeFormData, configMap, plan);
                if (executeException == null) continue;
                // 计算的异常 在复用时取归档顺序第一个步骤节点
                executeException.setProcedureStepId(!executeFormData.getReuse() ? executeFormData.getProcedureStepId() :
                        itemStepMap.get(executeFormData.getRecordItemId()));
                SysUser user = SysUserHolder.getUser();
                executeException.setRecordUserId(user.getUserId());
                executeException.setRecordUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
                executeExceptions.add(executeException);
            }
        }
        handleExceptionProcedureInfo(plan, executeExceptions);
        exceptionManageService.saveBatch(executeExceptions);
    }

    private void handleExceptionProcedureInfo(Plan plan, List<ExecuteException> executeExceptions) {
        if (CollUtil.isEmpty(executeExceptions)) {
            return;
        }
        List<ProcedureModel> procedureModels = procedureModelService.getByProcessIdAndVersion(plan.getProcessId(),
                plan.getProcessVersion());
        List<ProcedureStepModel> procedureStepModelList =
                procedureStepModelService.getStepModelByProcessIdAndVersion(plan.getProcessId(),
                        plan.getProcessVersion());
        Map<Long, ProcedureModel> procedureModelMap = CollectionUtils.convertMap(procedureModels,
                ProcedureModel::getId);
        Map<Long, ProcedureStepModel> stepMap = CollectionUtils.convertMap(procedureStepModelList,
                ProcedureStepModel::getProcedureStepId);
        executeExceptions.forEach(e -> {
            e.setBatchNo(plan.getBatchNo());
            e.setProcessId(plan.getProcessId());
            e.setProcessVersion(plan.getProcessVersion());
            setProcedureInfo(e, stepMap, procedureModelMap);
        });
    }

    private RLock getrLock(Long productPlanId) {
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                productPlanId));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        return lock;
    }

    private List<ExecuteFormData> saveAndCalculateResults(List<ExecuteFormData> results, Long copyVersion,
                                                          Plan plan, ProcedureStepModel procedureStepModel, boolean filterNull) {
        // 过滤值为空的 不进行更新
        if (filterNull){
            results = results.stream().filter(item -> StrUtil.isNotEmpty(item.getValue())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(results)) {
            return new ArrayList<>();
        }
        CalculateDataQueryDTO queryDTO = ExecuteFormDataConverter.INSTANCE.convert2CalculateQueryDto(plan, procedureStepModel, copyVersion);
        // 填充表单数据 (操作类型班组信息) 处理业务组件时间日期格式
        results  = formDataHandleService.fillFormDataAndFilter(FormDataFilterDTO.builder()
                .dataList(results)
                .procedureStepModelId(procedureStepModel.getId())
                .build());
        executeFormDataMapper.insertBatch(results);
         List<ExecuteFormData> resultData =
                this.calculateData(results, queryDTO);
        List<ExecuteFormData> filter = resultData.stream().filter(e -> StrUtil.isNotEmpty(e.getValue())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(filter)) {
            Long buildRev = this.selectMaxRev(plan.getId(), CollectionUtils.convertSet(filter, ExecuteFormData::getFieldId));
            filter.forEach(e -> {
                e.setRev(buildRev);
            });
            this.insertBatch(filter);
        }
        return filter;
    }

    private List<ExecuteFormData> saveAndCalculateResults(List<ExecuteFormData> results, Long copyVersion,
                                                          Plan plan, ProcedureStepModel procedureStepModel) {
        return saveAndCalculateResults(results, copyVersion, plan, procedureStepModel, true);
    }

    @Override
    public DateCalculateVO getCalculateDate(CalculateDateDTO dto) {
        return ExecuteDateCalculateUtil.getExecuteDateCalculate(dto.getStartTime(), dto.getEndTime(),
                dto.getDateType(), dto.getRoundingRule());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FormDataUpdateDTO dto) {
        handleModifyOrUpdate(ExecuteFormDataConverter.INSTANCE.convertToModifyDTO(dto), ExecuteFormDataType.UPDATE);
    }

    @Override
    public List<ExecuteFormData> selectByProductPlanIdAndItemIds(Long productPlanId, List<Long> recordItemIds) {
        return executeFormDataMapper.selectByProductPlanIdAndItemIds(productPlanId, recordItemIds);
    }

    @Override
    public List<ExecuteFormData> selectByProductPlanIdAndItemIdsAndCopyVersions(Long productPlanId, Collection<Long> recordItemIds, Collection<Long> copyVersions, Collection<Long> procedureStepIds) {
        return executeFormDataMapper.selectByProductPlanIdAndItemIdsAndCopyVersions(productPlanId, recordItemIds, copyVersions, procedureStepIds);
    }

    /**
     * 处理组件数据值修订或更新
     *
     * @param dto
     * @param type 数据操作类型 修订/更新
     */
    private void handleModifyOrUpdate(FormDataModifyDTO dto, ExecuteFormDataType type) {
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                dto.getProductPlanId()));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        try {
            ExecuteFormData data = ExecuteFormDataConverter.INSTANCE.convert(dto);
            data.setOperationType(type.getValue());
            data.setExtInfo(dto.getValueExtension());
            List<ExecuteFormData> calculateDataList = calculateData(CollUtil.toList(data),
                    ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
            Set<Long> fields = CollectionUtils.convertSet(calculateDataList, ExecuteFormData::getFieldId);
            fields.add(data.getFieldId());
            Long rev = executeFormDataMapper.selectMaxRev(dto.getProductPlanId(), fields);
            long maxRev = rev == null ? 0L : rev + 1L;
            calculateDataList.forEach(e -> e.setRev(maxRev));
            executeFormDataMapper.insertBatch(calculateDataList);
            data.setRev(maxRev);
            if (ObjectUtil.isNotNull(data)){
                savePicture(Collections.singletonList(data),dto.getRecordVersionId());
            }
            executeFormDataMapper.insert(data);
            exceptionAutoRecord(Collections.singletonList(data), calculateDataList, dto.getProcedureStepId());
        } catch (DuplicateKeyException e) {
            log.info("生产计划[{}]记录数据重复:{}", dto.getProductPlanId(), e.getMessage());
            throw new BmosException(MesResponseCode.EXECUTE_DATA_EXIST);
        } catch (DataIntegrityViolationException e) {
            handleDataTooLong(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public AttachmentVO upload(MultipartFile file) {
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            File files = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, suffix);
            file.transferTo(files);
            String key = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis();
            String bucketName = minioFileClient.getBucketName(MinioBucket.BMOS_PRODUCT);
            String uploadPatch = minioFileClient.uploadFile(MinioBucket.BMOS_PRODUCT, files, String.format("/%s" + suffix, key));
            return AttachmentVO.builder()
                    .id(IdUtils.getSnowflake())
                    .type(suffix)
                    .createBy(SysUserHolder.getUser().getUserId())
                    .createTime(LocalDateTime.now())
                    .path(bucketName + uploadPatch)
                    .build();
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.ATTACHMENT_FILE_ERROR);
        }
    }

    @Override
    public String pictureList(String value) {
        String emptyData = platformParameterClientImpl.getValueByCode(recordEmptyDataCode);
        if (StrUtil.isBlank(value) || StrUtil.equals(value,emptyData)){
            return null;
        }
        return executeAttachmentService.queryByIds(StrUtil.split(value, StrUtil.C_COMMA));
    }

    @Override
    public TrendAnalysisVO componentTrendAnalysis(ComponentTrendAnalysisDTO dto) {
        // 查询
        Plan plan = planService.getById(dto.getProductPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        List<ComponentTrendAnalysisVO> result = new ArrayList<>();
        TrendAnalysisVO trendAnalysisVO = new TrendAnalysisVO();
        trendAnalysisVO.setDataList(result);
        // 计算最大最小值
        analyzeMaxAndMin(dto.getMax(), dto.getMin(), trendAnalysisVO);
        int n = 0;
        ResponseInfo<BusinessParameterDetailFeignVO> feignVOResponseInfo = FeignUtils.handleRequest(
                data -> businessParameterFeign.detailByCode(data), BusinessParameterCodeConstants.MES_FIELD_TREND_ANALYSIS);
        if (Objects.isNull(feignVOResponseInfo.getData())){
            return trendAnalysisVO;
        }
        n = Integer.parseInt(feignVOResponseInfo.getData().getValue());
        Long recordItemId = batchRecordComponentService.getByFieldId(dto.getFieldId());
        if (Objects.isNull(recordItemId)){
            return trendAnalysisVO;
        }
        // 查找这个工艺id下具有该步骤id的所有版本
        List<String> processVersionList = procedureStepModelService.selectByProcessAndRecordItemId(plan.getProcessId(), recordItemId);
        if (CollUtil.isEmpty(processVersionList)){
            return trendAnalysisVO;
        }
        // 获取当前生产计划所用工艺的近n此的生产计划
        List<Plan> planList = planService.getPlanListByProcedureVersionId(plan.getProcessId(), processVersionList, n);
        if (CollUtil.isEmpty(planList)){
            return trendAnalysisVO;
        }
        // 获取这些生产计划下所有的formdata中的值
        List<ExecuteFormData> formDataList = executeFormDataMapper.selectByPlanIdsAndFieldId(planList.stream().map(Plan::getId).collect(Collectors.toList()),
                dto.getFieldId());
        assembleData(result, formDataList, planList);
        if (CollUtil.isNotEmpty(result)){
            // 获取result中value最大的值
            BigDecimal[] maxAndMin = findResultMaxAndMin(result);
            analyzeMaxAndMin(maxAndMin[0], maxAndMin[1], trendAnalysisVO);
        }
//        fixPrecision(trendAnalysisVO);
        return trendAnalysisVO;
    }

    private void fixPrecision(TrendAnalysisVO trendAnalysisVO) {
        // 根据dataList中的精度一致，若值为null则忽略
        if (CollUtil.isEmpty(trendAnalysisVO.getDataList())){
            return;
        }
        Integer scale = null;
        for (ComponentTrendAnalysisVO analysisVO : trendAnalysisVO.getDataList()) {
            if (Objects.isNull(analysisVO.getValue())){
                continue;
            }
            scale = Objects.isNull(scale) ? analysisVO.getValue().scale() : Math.max(scale, analysisVO.getValue().scale());
        }
        if (Objects.isNull(scale)){
            return;
        }
        if (Objects.nonNull(trendAnalysisVO.getMax())){
            trendAnalysisVO.setMax(trendAnalysisVO.getMax().setScale(scale, RoundingMode.HALF_EVEN));
        }
        if (Objects.nonNull(trendAnalysisVO.getMin())){
            trendAnalysisVO.setMin(trendAnalysisVO.getMin().setScale(scale, RoundingMode.HALF_EVEN));
        }
    }

    private BigDecimal[] findResultMaxAndMin(List<ComponentTrendAnalysisVO> result) {
        // 寻找result中value最大的值
        BigDecimal max = result.stream().map(ComponentTrendAnalysisVO::getValue).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(null);
        // 寻找result中value为最小的值
        BigDecimal min = result.stream().map(ComponentTrendAnalysisVO::getValue).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(null);
        return new BigDecimal[]{max, min};
    }

    private void analyzeMaxAndMin(BigDecimal max, BigDecimal min, TrendAnalysisVO trendAnalysisVO) {
        if (max == null && min == null){
            return ;
        }
        // 当最大值或最小值为负数时，组件在图中展示的最大值和数值组件配置的上限，两者取最大的再乘以95%作纵坐标的最大值；组件在图中展示的最小值和数值组件配置的下限，两者取最小的再乘以105%作为纵坐标的最小值
        if (min != null){
            // 若最小值小于0
            BigDecimal multiply;
            if (min.compareTo(BigDecimal.ZERO) < 0){
                multiply = min.multiply(new BigDecimal("1.05"));
            } else {
                multiply = min.multiply(new BigDecimal("0.95"));
            }
            multiply=multiply.setScale(min.scale(), RoundingMode.HALF_EVEN);
            BigDecimal curMin = trendAnalysisVO.getMin() == null ? multiply :  trendAnalysisVO.getMin().compareTo(multiply) < 0 ? trendAnalysisVO.getMin() : multiply;
            trendAnalysisVO.setMin(curMin);
        }
        if (max != null){
            // 若最大值小于0
            BigDecimal multiply;
            if (max.compareTo(BigDecimal.ZERO) < 0){
                multiply = max.multiply(new BigDecimal("0.95"));
            } else {
                multiply = max.multiply(new BigDecimal("1.05"));
            }
            multiply=multiply.setScale(max.scale(), RoundingMode.HALF_EVEN);
            BigDecimal curMax = trendAnalysisVO.getMax() == null ? multiply : trendAnalysisVO.getMax().compareTo(multiply) > 0 ? trendAnalysisVO.getMax() : multiply;
            trendAnalysisVO.setMax(curMax);
        }
    }

    /**
     * 组装数据
     * 根据批次id，班次、copyVersion、reuse、stepId 进行分组 只获取最新的数据
     *
     * @param analysisVOList
     * @param formDataList
     * @param planList
     */
    private void assembleData(List<ComponentTrendAnalysisVO> analysisVOList, List<ExecuteFormData> formDataList, List<Plan> planList) {
        if (CollUtil.isEmpty(formDataList)){
            return;
        }
        // 生产计划id+工艺班次+工序班次+copyVersion+reuse+stepId+复用 进行分组
        Map<String, List<ExecuteFormData>> map = formDataList.stream().collect(Collectors.groupingBy(e ->
                String.format("%s_%s_%s_%s_%s_%s_%s", e.getProductPlanId(), e.getProcessChangeNumber(), e.getProcedureChangeNumber(), e.getCopyVersion(), e.getReuse(), e.getProcedureStepId(), e.getProcedureChangeNumber())));
        Map<Long, List<ExecuteFormData>> planExecuteFormDataMap = new HashMap<>();
        for (String key : map.keySet()) {
            ExecuteFormData formData = map.get(key).get(0);
            Long planId = formData.getProductPlanId();
            if (planExecuteFormDataMap.containsKey(formData.getProductPlanId())){
                planExecuteFormDataMap.get(planId).add(formData);
            } else {
                planExecuteFormDataMap.put(planId, Lists.newArrayList(formData));
            }
        }
        for (int i = planList.size() - 1; i >= 0; i--) {
            Plan plan = planList.get(i);
            if (!planExecuteFormDataMap.containsKey(plan.getId())){
                continue;
            }
            List<ExecuteFormData> executeFormDataList = planExecuteFormDataMap.get(plan.getId());
            analysisVOList.addAll(ExecuteFormDataConverter.INSTANCE.convert2TrendAnalysisVO(executeFormDataList, plan));
        }
    }

    @Override
    public List<ProcedureViewVO> queryProcedureViewVO(ProcedureViewQueryDTO dto) {
        // 工序模型id不为空的情况下 为工序审核 只查当前工序
        if (dto.getProcedureModelId() != null) {
            return getProcedureViewVOSByProcedureModelId(dto.getProcedureModelId());
        }
        List<ProcedureModel> procedureModels = procedureModelService.getByProcessIdAndVersion(dto.getProcessId(),
                dto.getProcessVersion());
        List<ProcedureStepModel> stepModels =
                procedureStepModelService.getByProcedureModelIds(CollectionUtils.convertList(procedureModels,
                        ProcedureModel::getId));
        Map<Long, List<ProcedureStepModel>> stepMap = CollectionUtils.convertMultiMap(stepModels,
                ProcedureStepModel::getProcedureModelId);
        return procedureModels.stream()
                .sorted(Comparator.comparing(ProcedureModel::getSort, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(ProcedureModel::getId))
                .map(e-> generateProcedureViewVO(e, stepMap.getOrDefault(e.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    private ProcedureViewVO generateProcedureViewVO(ProcedureModel procedureModel, List<ProcedureStepModel> steps) {
        return ProcedureViewVO.builder()
                .procedureId(procedureModel.getProcedureId())
                .procedureName(procedureModel.getName())
                .procedureModelId(procedureModel.getId())
                .procedureStepViewList(steps.stream()
                        .sorted(Comparator.comparing(ProcedureStepModel::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(e -> {
                            ProcedureStepViewVO step = new ProcedureStepViewVO();
                            step.setProcedureStepName(e.getName());
                            step.setProcedureStepModelId(e.getId());
                            step.setProcedureStepId(e.getProcedureStepId());
                            step.setRecordItemId(e.getRecordItemId());
                            step.setRecordVersionId(e.getRecordVersionId());
                            step.setNodeId(e.getNodeId());
                            return step;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    private List<ProcedureViewVO> getProcedureViewVOSByProcedureModelId(Long procedureModelId) {
        ProcedureModel procedureModel = procedureModelService.getById(procedureModelId);
        if (procedureModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        List<ProcedureStepModel> steps = procedureStepModelService.getByProcedureModelId(procedureModelId);
        return Collections.singletonList(generateProcedureViewVO(procedureModel, steps));
    }

    @Override
    public List<CopyRecordItemVO> getExistedCopyVersionList(RecordCopyQueryDTO dto) {
        return executeRecordCopyService.getCopyVersionList(dto);
    }

    @Override
    public CommonPage<PlanFieldModifyVO> queryPlanModifyList(PlanFieldModifyQueryDTO dto) {
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        List<ProcedureStepModelDetailVO> stepList =
                procedureStepModelService.queryStepModelList(ProcedureStepModelQueryDTO.builder()
                        .processId(plan.getProcessId())
                        .processVersion(plan.getProcessVersion())
                        .procedureName(dto.getProcedureName())
                        .procedureStepName(dto.getProcedureStepName())
                        .build());
        stepList = stepList.stream().filter(e-> Objects.nonNull(e.getRecordItemId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(stepList)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        // 工步map 用来处理回显步骤名称工序名称
        Map<Long, ProcedureStepModelDetailVO> stepMap = CollectionUtils.convertMap(stepList, ProcedureStepModelDetailVO::getId);
        // 根据工步模型id和工步id查询修订分页记录 兼容非复用旧数据
        Set<Long> stepModelIds = CollectionUtils.convertSet(stepList, ProcedureStepModelDetailVO::getId);
        List<Long> procedureStepIds = stepList.stream()
                .map(ProcedureStepModelDetailVO::getProcedureStepId)
                .distinct()
                .collect(Collectors.toList());
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        // 查出当前分页修订记录
        List<ExecuteFormData> list = executeFormDataMapper.queryModifyRecordPage(dto.getProductPlanId(),
                procedureStepIds, stepModelIds);
        CommonPage<ExecuteFormData> tempPage = CommonPage.convertPage(list);
        if (CollUtil.isEmpty(list)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        List<Long> fieldIds = CollectionUtils.convertList(list, ExecuteFormData::getFieldId);
        // 查出所有相关数据 用来处理修订原值
        procedureStepIds.add(ProcessConstant.REUSE_PROCEDURE_STEP_ID);
        List<ExecuteFormData> relationDataList =
                executeFormDataMapper.selectByStepsAndFieldIds(dto.getProductPlanId(), procedureStepIds, fieldIds);
        // 根据工步、fieldId、copyVersion、Rev依次分组
        Map<Long, Map<Long, Map<Long, Map<Long, ExecuteFormData>>>> dataMap =
                relationDataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                        Collectors.groupingBy(ExecuteFormData::getFieldId,
                                Collectors.groupingBy(ExecuteFormData::getCopyVersion,
                                        Collectors.toMap(
                                                ExecuteFormData::getRev,
                                                Function.identity(),
                                                (existing, replacement) -> existing)))));
        List<PlanFieldModifyVO> result = list.stream().map(e -> {
            // rev依次递增 通过找前一个rev找到修订前的原值
            return getPlanFieldModifyVO(e, dataMap, stepMap);
        }).collect(Collectors.toList());
        return CommonPage.CommonPage(result, tempPage.getTotal().longValue(), dto);
    }

    @Override
    public List<SubsidiaryRecordDocVO> getSubsidiaryDocList(Long id) {
        ExecuteSubsidiaryRecord sub = executeSubsidiaryRecordMapper.selectById(id);
        // 记录页
        List<ExecuteRecordCopy> copyList =
                executeRecordCopyService.getListByRecordItemIds(sub.getProductPlanId(),
          Collections.singletonList(sub.getRecordItemId()));
        Long stepId = sub.getReuse() ? ProcessConstant.REUSE_PROCEDURE_STEP_ID : sub.getProcedureStepId();
        copyList = copyList.stream().filter(e -> {
            return Objects.equals(e.getProcedureStepId(), stepId) &&
                    Objects.equals(e.getProcessChangeNumber(), sub.getProcessChangeNumber()) &&
                    Objects.equals(e.getProcedureChangeNumber(), sub.getProcedureChangeNumber());
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(copyList)) {
            return new ArrayList<>();
        }
        RecordItemVO recordItemVO = batchRecordItemService.queryRecordItemByItemIdAndVersionId(sub.getRecordItemId(), sub.getRecordVersionId());
        // 数据
        List<ExecuteFormData> data =
                executeFormDataMapper.selectByPlanIdAndItemIdAndStepId(sub.getProductPlanId(),
          sub.getRecordItemId(), stepId);
        data = new ArrayList<>(data.stream()
                .collect(Collectors.toMap(
                        ExecuteFormData::getFieldId,    // 根据 id 去重
                        d -> d,   // 保留整个对象
                        (existing, replacement) -> existing  // 保留第一个（最新）对象
                ))
                .values());
        Map<Long, List<ExecuteFormData>> dataMap = CollectionUtils.convertMultiMap(data, ExecuteFormData::getCopyVersion);
        // 附件
        List<ExecuteAttachment> attachments = executeAttachmentService.getListByPlanIdAndItemIdAndStepId(sub.getProductPlanId(),
                sub.getRecordItemId(), stepId);
        Map<Long, List<ExecuteAttachment>> attachMap = CollectionUtils.convertMultiMap(attachments, ExecuteAttachment::getCopyVersion);
        // 填充
        return copyList.stream().map(e->{
            SubsidiaryRecordDocVO vo = ExecuteFormDataConverter.INSTANCE.convert2SubsidiaryRecordDocVO(recordItemVO);
            vo.setDataList(ExecuteFormDataConverter.INSTANCE.convertList2(dataMap.getOrDefault(e.getVersion(), new ArrayList<>())));
            vo.setAttachments(ExecuteAttachmentConvert.INSTANCE.convertList2(attachMap.getOrDefault(e.getVersion(), new ArrayList<>())));
            vo.setDiscard(e.getDiscard());
            vo.setCopyVersion(e.getVersion());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FormDataItemVO> getCalculationPreview(FormDataBatchSaveDTO dto) {
        if (CollUtil.isEmpty(dto.getItems())) {
            return new ArrayList<>();
        }
        List<ExecuteFormData> resultData = calculateData(
                ExecuteFormDataConverter.INSTANCE.convert(dto),
                ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
        if (CollUtil.isEmpty(resultData)) {
            return new ArrayList<>();
        }
        return resultData.stream().filter(e->{
            return Objects.equals(e.getCopyVersion(), dto.getCopyVersion()) &&
                    Objects.equals(e.getRecordItemId(), dto.getRecordItemId()) &&
                    (dto.getReuse() ? Objects.equals(e.getProcedureStepId(), ProcessConstant.REUSE_PROCEDURE_STEP_ID)
                            : Objects.equals(e.getProcedureStepId(), dto.getProcedureStepId()));
        }).map(ExecuteFormDataConverter.INSTANCE::convert2FormDataItemVO).collect(Collectors.toList());
    }

    @Override
    public List<FormDataProcedureInfo> selectProcessAndProcedureByFormDataIds(Collection<Long> formDataIds) {
        if (CollectionUtils.isAnyEmpty(formDataIds)){
            return new ArrayList<>();
        }
        return executeFormDataMapper.selectProcessAndProcedureByFormDataIds(formDataIds);
    }

    private static PlanFieldModifyVO getPlanFieldModifyVO(ExecuteFormData e,
                                                          Map<Long, Map<Long, Map<Long, Map<Long, ExecuteFormData>>>> dataMap,
                                                          Map<Long, ProcedureStepModelDetailVO> stepMap) {
        Long rev = e.getRev();
        PlanFieldModifyVO planFieldModifyVO = new PlanFieldModifyVO();
        Map<Long, ExecuteFormData> revMap = dataMap.getOrDefault(e.getProcedureStepId(), new HashMap<>())
                .getOrDefault(e.getFieldId(), new HashMap<>())
                .getOrDefault(e.getCopyVersion(), new HashMap<>());
        // 找到比当前rev小的最大的一个rev则为其修改源
        Long preRev = revMap.keySet().stream().filter(key -> key < rev).max(Long::compareTo).orElse(null);
        planFieldModifyVO.setFieldId(e.getFieldId());
        planFieldModifyVO.setOperationUser(e.getOperationUser());
        planFieldModifyVO.setReviewUser(e.getReviewUser());
        planFieldModifyVO.setNewValue(e.getValue());
        planFieldModifyVO.setRemark(e.getRemark());
        planFieldModifyVO.setOperationTime(e.getOperationTime());
        ProcedureStepModelDetailVO step = stepMap.get(e.getProcedureStepModelId());
        planFieldModifyVO.setProcedureName(step == null ? null : step.getProcedureName());
        planFieldModifyVO.setProcedureStepName(step == null ? null : step.getName());
        if (preRev != null && revMap.get(preRev) != null) {
            planFieldModifyVO.setOriginalValue(revMap.get(preRev).getValue());
        }
        return planFieldModifyVO;
    }

    @Override
    public void lockProcedureStep(LockStepDTO dto) {
        String lockKey = formatLockKey(dto);
        Object value = redisService.get(lockKey, ExecuteRedisKeyDefine.LOCK_STEP);
        if (ObjectUtil.isNotNull(value)) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_LOCKED);
        }
        redisService.set(lockKey, lockKey, ExecuteRedisKeyDefine.LOCK_STEP);
    }

    @Override
    public void unLockProcedureStep(LockStepDTO dto) {
        String lockKey = formatLockKey(dto);
        redisService.delete(lockKey, ExecuteRedisKeyDefine.LOCK_STEP);
    }

    /**
     * 通过计划id和记录id集合获取数据
     *
     * @param planId        计划id
     * @param recordItemIds 记录项id集合
     * @return 获取结果
     */
    @Override
    public List<ExecuteFormData> getDataByPlanAndItemIds(Long planId, Collection<Long> recordItemIds) {
        List<ExecuteFormData> dataList = executeFormDataMapper.selectByProductPlanIdAndItemIds(planId,
                recordItemIds);
        if (CollUtil.isEmpty(dataList)){
            return Lists.newArrayList();
        }
        Map<Long, List<ExecuteFormData>> map = dataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getProcedureStepId));
        return map.values().stream().flatMap(dataList1 -> ExecuteFormDataConverter.INSTANCE.filterLatest(dataList1).stream()).collect(Collectors.toList());
    }

    private String formatLockKey(LockStepDTO dto) {
        return String.format("%s_%s", dto.getProductPlanId(), dto.getProcedureStepId());
    }

    private List<Long> getRelationPlans(Long productPlanId) {
        List<Long> planIds = new ArrayList<>();
        planIds.add(productPlanId);
        List<ProductPlanRelation> relations = productPlanRelationService.getList(productPlanId);
        if (CollUtil.isNotEmpty(relations)) {
            planIds.addAll(relations.stream().map(ProductPlanRelation::getRelationProductPlanId).collect(Collectors.toList()));
        }
        return planIds;
    }



    /**
     * 处理异常信息自动录入
     * @param list 填报值列表
     * @param calculateList 计算值列表
     * @param currentProcedureStepId 当前填报工步id
     */
    private void exceptionAutoRecord(List<ExecuteFormData> list, List<ExecuteFormData> calculateList,
                                     Long currentProcedureStepId) {
        // 处理异常自动录入 过滤出录入的N/A空值
        String valueByCode = platformParameterClientImpl.getValueByCode(recordEmptyDataCode);
        list = list.stream().filter(e-> !StrUtil.equals(e.getValue(), valueByCode)).collect(Collectors.toList());
        calculateList = calculateList.stream().filter(e-> !StrUtil.equals(e.getValue(), valueByCode)).collect(Collectors.toList());
        if (CollUtil.isEmpty(list) && CollUtil.isEmpty(calculateList)) {
            return;
        }
        ExecuteFormData first = CollUtil.isEmpty(list) ? CollUtil.getFirst(calculateList) : CollUtil.getFirst(list);
        List<Long> fieldIds = CollectionUtils.convertList(list, ExecuteFormData::getFieldId);
        fieldIds.addAll(CollectionUtils.convertList(calculateList, ExecuteFormData::getFieldId));
        List<ProcedureStepConfig> configs =
                procedureStepConfigService.getListByProcessVersionAndFields(first.getProcessId(),
                        first.getProcessVersion(), fieldIds);
        if (CollUtil.isEmpty(configs)) {
            return;
        }
        // 根据步骤id、fieldId分组
        Map<Long, Map<Long, ProcedureStepConfig>> configMap = getStepFieldConfigMap(configs);
        List<ExecuteException> executeExceptions = new ArrayList<>();
        Plan plan = planService.getById(first.getProductPlanId());
        Map<Long, Long> itemStepMap = getOrderItemStepMap(plan.getProcessId(), plan.getProcessVersion());
        // 处理填报值
        for (ExecuteFormData executeFormData : list) {
            ExecuteException executeException = getExceptionWithBasicInfo(executeFormData, configMap, plan);
            if (executeException == null) continue;
            executeException.setProcedureStepId(currentProcedureStepId);
            executeExceptions.add(executeException);
        }
        // 处理计算值
        for (ExecuteFormData executeFormData : calculateList) {
            ExecuteException executeException = getExceptionWithBasicInfo(executeFormData, configMap, plan);
            if (executeException == null) continue;
            // 计算的异常 在复用时取归档顺序第一个步骤节点
            executeException.setProcedureStepId(!executeFormData.getReuse() ? executeFormData.getProcedureStepId() :
                    itemStepMap.get(executeFormData.getRecordItemId()));
            SysUser user = SysUserHolder.getUser();
            executeException.setRecordUserId(user.getUserId());
            executeException.setRecordUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
            executeExceptions.add(executeException);
        }
        handleExceptionProcedureInfo(plan, executeExceptions);
        exceptionManageService.saveBatch(executeExceptions);
    }

    @NotNull
    private static Map<Long, Map<Long, ProcedureStepConfig>> getStepFieldConfigMap(List<ProcedureStepConfig> configs) {
        Map<Long, Map<Long, ProcedureStepConfig>> configMap =
                configs.stream().collect(Collectors.groupingBy(procedureStepConfig -> procedureStepConfig.getReuse() ?
                                ProcessConstant.REUSE_PROCEDURE_STEP_ID :
                                procedureStepConfig.getProcedureStepId(),
                        Collectors.groupingBy(ProcedureStepConfig::getFieldId, Collectors.collectingAndThen(
                                Collectors.toList(),
                                CollUtil::getFirst))));
        return configMap;
    }

    /**
     * 获取复用的节点 itemId:stepId map
     * @return
     */
    private Map<Long, Long> getOrderItemStepMap(Long processId, String processVersion) {
        ProcessRecordOrderQueryDTO orderQueryDTO = new ProcessRecordOrderQueryDTO();
        orderQueryDTO.setProcessId(processId);
        orderQueryDTO.setProcessVersion(processVersion);
        // 复用的需要使用到归档顺序中的节点进行赋值
        List<ProcessRecordOrderVO> recordOrder = processService.getRecordOrder(orderQueryDTO);
        recordOrder = recordOrder.stream().filter(ProcessRecordOrderVO::getReusable).collect(Collectors.toList());
        Map<Long, Long> itemStepMap = CollectionUtils.convertMap(recordOrder, ProcessRecordOrderVO::getRecordItemId,
                ProcessRecordOrderVO::getProcedureStepId);
        return itemStepMap;
    }

    private static void setProcedureInfo(ExecuteException e, Map<Long, ProcedureStepModel> stepMap, Map<Long, ProcedureModel> procedureModelMap) {
        Long procedureStepId = e.getProcedureStepId();
        ProcedureStepModel procedureStepModel = stepMap.get(procedureStepId);
        ProcedureModel procedureModel = procedureModelMap.get(procedureStepModel.getProcedureModelId());
        e.setProcedureStepModelId(procedureStepModel.getId());
        e.setProcedureStepName(procedureStepModel.getName());
        e.setProcedureId(procedureModel.getProcedureId());
        e.setProcedureModelId(procedureModel.getId());
        e.setProcedureName(procedureModel.getName());
    }

    /**
     * 处理异常记录基础信息
     * @param executeFormData
     * @param configMap
     * @param plan
     * @return
     */

    private ExecuteException getExceptionWithBasicInfo(ExecuteFormData executeFormData, Map<Long, Map<Long, ProcedureStepConfig>> configMap,
                                                              Plan plan) {
        ProcedureStepConfig config = configMap.getOrDefault(executeFormData.getProcedureStepId(), new HashMap<>())
                .get(executeFormData.getFieldId());
        if (config == null) {
            return null;
        }
        String configInfo = config.getConfigInfo();
        ScopeLimitConfig scopeLimitConfig = JsonUtils.parseObject(configInfo, ScopeLimitConfig.class);
        // 未查询到范围限制配置 或者 自动录入为false 则不处理
        if (scopeLimitConfig == null || BooleanUtil.isFalse(scopeLimitConfig.getWaringAutoRecord())) {
            return null;
        }
        String result = scopeLimitConfig.checkScopeLimit(executeFormData.getValue(), executeFormData.getValueExtension());
        if (StrUtil.isBlank(result)) {
            return null;
        }
        return getExecuteException(executeFormData, result, plan);
    }

    @NotNull
    private ExecuteException getExecuteException(ExecuteFormData executeFormData, String result, Plan plan) {
        ExecuteException executeException = new ExecuteException();
        executeException.setExceptionStatus(ExceptionStatusEnum.INVESTIGATING);
        executeException.setExecuteFormDataId(executeFormData.getId());
        executeException.setRecordUserId(executeFormData.getOperationUser());
        BaseUserDO user = UserUtils.getUser(executeFormData.getOperationUser());
        executeException.setRecordUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
        executeException.setRecordMode(ExceptionRecordModeEnum.AUTO_RECORD);
        executeException.setRecordTime(executeFormData.getOperationTime());
        executeException.setExceptionDescription(result);
        // 自动录入异常类型
        executeException.setExceptionType(ExceptionTypeDictEnum.OverLimitException.getName());
        executeException.setExceptionTypeCode(ExceptionTypeDictEnum.OverLimitException.getValue());
        executeException.setProcedureStepId(executeFormData.getProcedureStepId());
        // 生产信息
        if (plan != null) {
            executeException.setProcessName(plan.getProcessName());
            executeException.setProductPlanId(plan.getId());
            executeException.setProductPlanId(plan.getId());
            executeException.setProductId(plan.getProductId());
            executeException.setProductFullName(plan.getProductMergeCode() + StrUtil.DASHED + plan.getProductName());
        }
        return executeException;
    }


    /**
     * 解析业务组件异常记录配置: 暂时只有设备数采组件添加了异常记录配置
     * 产品沟通结果:在设计上 业务组件的阈值设置只会配置在组件最顶层
     * @param componentTree 业务组件树
     * @param parentConfig 顶层组件配置json
     * @return key: fieldId value: 阈值限制配置
     */
    private Map<Long, ScopeLimitConfig> parseComponentExceptionRecordConfig(ComponentListVO componentTree,
                                                                            String parentConfig) {
        HashMap<Long, ScopeLimitConfig> result = new HashMap<>();
        if (StrUtil.isEmpty(parentConfig)) {
            return result;
        }
        JSONObject jsonObject = JSONUtil.parseObj(parentConfig);
        JSONArray jsonArray = jsonObject.getJSONArray(CUSTOM_LIMIT_CONFIG_FIELD);
        if (CollUtil.isEmpty(jsonArray)) {
            return result;
        }
        List<ScopeLimitConfig> configList = jsonArray.toList(ScopeLimitConfig.class);
        HashMap<String, ScopeLimitConfig> codeMap = new HashMap<>();
        // 过滤出需要自动记录的异常配置
        for (ScopeLimitConfig config : configList) {
            if (BooleanUtil.isFalse(config.getWaringAutoRecord())
                    || Objects.isNull(config.getComponentDetail())
                    || StrUtil.isEmpty(config.getComponentDetail().getFieldData())) {
                continue;
            }
            codeMap.put(config.getComponentDetail().getFieldData(), config);
        }
        recHandleComponentScopeConfig(componentTree, codeMap, result);
        return result;
    }

    /**
     * 递归处理子组件配置
     *
     * @param componentNode 组件节点
     * @param codeMap 自定义code与其配置
     * @param result 结果map
     */
    private void recHandleComponentScopeConfig(ComponentListVO componentNode,
                                               HashMap<String, ScopeLimitConfig> codeMap,
                                               HashMap<Long, ScopeLimitConfig> result) {
        // 自定义组件阈值
        if (Objects.equals(componentNode.getComponentType(), BusinessComponentTypeEnum.CUSTOM_FIELD.getValue())) {
            ComponentDetail componentDetail = JsonUtils.parseObject(componentNode.getComponentDetail(),
                    ComponentDetail.class);
            if (componentDetail != null && codeMap.get(componentDetail.getFieldData()) != null) {
                result.put(componentNode.getFieldId(), codeMap.get(componentDetail.getFieldData()));
            }
        }
        if (CollUtil.isNotEmpty(componentNode.getChildren())) {
            componentNode.getChildren().forEach(e -> {
                recHandleComponentScopeConfig(e, codeMap, result);
            });
        }
    }

}
