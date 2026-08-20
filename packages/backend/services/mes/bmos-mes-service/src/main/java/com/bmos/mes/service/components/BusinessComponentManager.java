package com.bmos.mes.service.components;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.components.annotations.BmosComponentDetail;
import com.bmos.mes.service.components.annotations.BmosComponentSummary;
import com.bmos.mes.service.components.annotations.BmosComponentSummaryConfig;
import com.bmos.mes.service.components.annotations.BmosComponentSummaryGroupBy;
import com.bmos.mes.service.components.convert.BusinessComponentInstanceConvert;
import com.bmos.mes.service.components.dto.FormDataOPT;
import com.bmos.mes.service.components.enums.BmosComponentSummaryType;
import com.bmos.mes.service.components.mapper.BusinessComponentInstanceMapper;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.mapper.ProcedureModelMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.record.mapper.BatchRecordComponentDetailMapper;
import com.bmos.mes.service.record.mapper.BatchRecordComponentMapper;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordComponentDetail;
import com.bmos.mes.service.weigh.centre.config.util.BmosTreeUtil;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighInputProcessMapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighInputProcess;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.*;
import static com.bmos.mes.common.utils.TimeUtil.F_DATE;
import static com.bmos.mes.common.utils.TimeUtil.F_DATETIME;

/**
 * 组件管理器
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/16 17:31
 */
@Component
@Slf4j
public class BusinessComponentManager {

    @Resource
    private ProcedureModelMapper procedureModelMapper;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Resource
    private BatchRecordComponentMapper batchRecordComponentMapper;

    @Resource
    private BusinessComponentInstanceMapper businessComponentInstanceMapper;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    @Resource
    private IWeighInputProcessMapper IWeighInputProcessMapper;

    @Resource
    private BatchRecordComponentDetailMapper batchRecordComponentDetailMapper;

    // 需要初始化的组件类型
    private static final BusinessComponentTypeEnum[] INIT_COMPONENT_INSTANCES = new BusinessComponentTypeEnum[]{
            // 物料投入组件
            MATERIAL_INPUT,
            // 物料件组件
            MATERIAL_INFO,
            // 称量数据组件
            WEIGHING_DATA,
            // 请验结果组件
            INSPECTION_RESULTS
    };

    /**
     * 获取组件初始化类型
     *
     * @return 组件类型
     */
    public BusinessComponentTypeEnum[] getInitComponentTypes() {
        return INIT_COMPONENT_INSTANCES;
    }

    /**
     * 根据产品计划id和组件类型查询所有组件实例
     *
     * @param productPlanId 产品计划id
     * @param componentType 组件类型
     * @return 组件信息
     */
    public List<BatchRecordComponent> listAllBatchRecordComponent(Long productPlanId, String componentType) {
        List<ProcedureStepModel> procedureStepModels = listAllProcedureStepModels(productPlanId);
        return queryBatchRecordComponentsByStepModels(componentType, procedureStepModels);
    }

    /**
     * 根据生产计划id初始化指定类型的组件实例
     *
     * @param productPlanId  产品计划id
     * @param componentTypes 组件类型
     * @return 组件信息
     */
    public List<BusinessComponentInstance> initComponentInstance(Long productPlanId, BusinessComponentTypeEnum... componentTypes) {
        if (ArrayUtil.isEmpty(componentTypes)) {
            return new ArrayList<>();
        }
        List<BusinessComponentInstance> result = new ArrayList<>();
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            return result;
        }
        List<BusinessComponentInstance> exist = businessComponentInstanceMapper.listComponentInstanceByProductPlanIdAndComponentType(productPlanId, componentTypes);
        if (CollectionUtil.isNotEmpty(exist)) {
            return exist;
        }
        List<String> componentTypeStrList = Arrays.stream(componentTypes)
                .map(BusinessComponentTypeEnum::getValue)
                .collect(Collectors.toList());
        Long processId = plan.getProcessId();
        String processVersion = plan.getProcessVersion();
        List<ProcedureModel> procedureModels = procedureModelMapper.selectByProcessIdAndVersion(processId, processVersion);
        List<ProcedureStepModel> procedureStepModels = procedureStepModelMapper.selectByProcedureModelIds(procedureModels.stream().map(ProcedureModel::getId).collect(Collectors.toList()));
        Map<String, ProcedureStepConfig> configMap = procedureStepConfigMapper.selectByProcessVersion(processId, processVersion)
                .stream()
                .collect(Collectors.toMap(this::buildProcedureStepConfigUnionKey, Function.identity(), (v1, v2) -> v2));
        Map<String, List<BatchRecordComponent>> components = batchRecordComponentMapper.selectByRecordListAndTypeList(procedureStepModels.stream()
                                .map(ProcedureStepModel::getRecordItemId)
                                .collect(Collectors.toList()),
                        procedureStepModels.stream()
                                .map(ProcedureStepModel::getRecordVersionId)
                                .collect(Collectors.toList()), componentTypeStrList)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRecordItemId() + "_" + item.getRecordVersionId()));
        for (ProcedureStepModel procedureStepModel : procedureStepModels) {
            List<BatchRecordComponent> componentList = components.getOrDefault(procedureStepModel.getRecordItemId() + "_" + procedureStepModel.getRecordVersionId(), new ArrayList<>());
            for (BatchRecordComponent batchRecordComponent : componentList) {
                ProcedureStepConfig procedureStepConfig = configMap.get((procedureStepModel.getReusable() ? 0 : procedureStepModel.getId()) + "_" + batchRecordComponent.getId() + "_" + procedureStepModel.getProcessId() + "_" + procedureStepModel.getProcessVersion());
                BusinessComponentInstance instance = new BusinessComponentInstance();
                instance.setProductPlanId(plan.getId());
                instance.setBatchNo(plan.getBatchNo());
                instance.setProcedureStepModelId(procedureStepModel.getId());
                instance.setProcessId(procedureStepModel.getProcessId());
                instance.setProcessVersion(procedureStepModel.getProcessVersion());
                instance.setRecordItemId(batchRecordComponent.getRecordItemId());
                instance.setRecordVersionId(batchRecordComponent.getRecordVersionId());
                instance.setProcedureStepId(procedureStepModel.getProcedureStepId());
                instance.setReuse(procedureStepModel.getReusable());
                instance.setCopyVersion(0L);
                instance.setComponentId(batchRecordComponent.getId());
                instance.setComponentType(BusinessComponentTypeEnum.getEnumByValue(batchRecordComponent.getComponentType()));
                instance.setComponentName(batchRecordComponent.getComponentName());
                if (procedureStepConfig != null) {
                    instance.setProcedureStepConfigId(procedureStepConfig.getId());
                    instance.setComponentConfigJson(procedureStepConfig.getConfigInfo());
                }
                result.add(instance);
            }
        }
        if (CollectionUtil.isNotEmpty(result)) {
            businessComponentInstanceMapper.insertBatch(result);
        }
        List<WeighInputProcess> weighInputProcesses = new ArrayList<>();
        for (BusinessComponentInstance componentInstance : result) {
            if (componentInstance.getComponentType() == MATERIAL_INPUT) {
                weighInputProcesses.add(new WeighInputProcess(componentInstance.getId(), false));
            }
        }
        if (CollectionUtil.isNotEmpty(weighInputProcesses)) {
            IWeighInputProcessMapper.insertBatch(weighInputProcesses);
        }
        return result;
    }

    /**
     * 根据产品计划id和组件类型查询所有组件配置
     *
     * @param productPlanId 产品计划id
     * @param componentType 组件类型
     * @return 组件配置信息
     */
    public List<ProcedureStepConfig> listComponentConfig(Long productPlanId, String componentType) {
        List<ProcedureStepModel> procedureStepModels = listAllProcedureStepModels(productPlanId);
        List<ProcedureStepConfig> result = new ArrayList<>();
        Set<Long> idSet = new HashSet<>();
        Map<String, List<BatchRecordComponent>> components = batchRecordComponentMapper.selectByRecordListAndTypeList(procedureStepModels.stream()
                                .map(ProcedureStepModel::getRecordItemId)
                                .collect(Collectors.toList()),
                        procedureStepModels.stream()
                                .map(ProcedureStepModel::getRecordVersionId)
                                .collect(Collectors.toList()),
                        componentType == null ? null : Collections.singletonList(componentType))
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRecordItemId() + "_" + item.getRecordVersionId()));
        for (ProcedureStepModel procedureStepModel : procedureStepModels) {
            List<BatchRecordComponent> componentList = components.get(procedureStepModel.getRecordItemId() + "_" + procedureStepModel.getRecordVersionId());
            for (BatchRecordComponent batchRecordComponent : componentList) {
                ProcedureStepConfig procedureStepConfig = procedureStepConfigMapper.selectComponentConfig(procedureStepModel.getId(), batchRecordComponent.getId(), procedureStepModel.getReusable(), procedureStepModel.getProcessId(), procedureStepModel.getProcessVersion());
                if (idSet.contains(procedureStepConfig.getId())) {
                    continue;
                }
                result.add(procedureStepConfig);
                idSet.add(procedureStepConfig.getId());
            }
        }
        return result;
    }

    private List<BatchRecordComponent> listAllBatchRecordComponent(List<ProcedureStepModel> procedureStepModels, String componentType) {
        return queryBatchRecordComponentsByStepModels(componentType, procedureStepModels);
    }

    private List<ProcedureStepModel> listAllProcedureStepModels(Long productPlanId) {
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            return new ArrayList<>();
        }
        Long processId = plan.getProcessId();
        String processVersion = plan.getProcessVersion();
        List<ProcedureModel> procedureModels = procedureModelMapper.selectByProcessIdAndVersion(processId, processVersion);
        return procedureStepModelMapper.selectByProcedureModelIds(procedureModels.stream().map(ProcedureModel::getId).collect(Collectors.toList()));
    }

    private @NotNull List<BatchRecordComponent> queryBatchRecordComponentsByStepModels(String componentType, List<ProcedureStepModel> procedureStepModels) {
        List<BatchRecordComponent> result = new ArrayList<>();
        Map<String, List<BatchRecordComponent>> components = batchRecordComponentMapper.selectByRecordListAndTypeList(procedureStepModels.stream()
                                .map(ProcedureStepModel::getRecordItemId)
                                .collect(Collectors.toList()),
                        procedureStepModels.stream()
                                .map(ProcedureStepModel::getRecordVersionId)
                                .collect(Collectors.toList()),
                        componentType == null ? null :
                                Collections.singletonList(componentType))
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRecordItemId() + "_" + item.getRecordVersionId()));
        for (ProcedureStepModel procedureStepModel : procedureStepModels) {
            List<BatchRecordComponent> componentList = components.get(procedureStepModel.getRecordItemId() + "_" + procedureStepModel.getRecordVersionId());
            result.addAll(componentList);
        }
        return result;
    }

    /**
     * 根据组件实例id查询组件实例
     *
     * @param componentInstanceId 组件实例id
     * @return 组件实例
     */
    @Nullable
    public BusinessComponentInstance getComponentInstanceById(Long componentInstanceId) {
        if (componentInstanceId == null) {
            return null;
        }
        return businessComponentInstanceMapper.selectById(componentInstanceId);
    }

    /**
     * 根据生产计划id和组件类型查询所有组件实例
     *
     * @param productPlanId 生产计划id
     * @param componentType 组件类型
     * @return
     */
    public List<BusinessComponentInstance> getComponentInstancesByProductPlanId(Long productPlanId, String componentType) {
        return businessComponentInstanceMapper.listComponentInstanceByProductPlanIdAndComponentType(productPlanId, BusinessComponentTypeEnum.getEnumByValue(componentType));
    }

    public List<FormDataOPT> getFormDataOPTList(Long componentsInstanceId) {
        BusinessComponentInstance componentInstance = getComponentInstanceById(componentsInstanceId);
        if (componentInstance == null) {
            return null;
        }
        List<BatchRecordComponent> treeList = new ArrayList<>();
        BatchRecordComponent batchRecordComponent = batchRecordComponentMapper.selectById(componentInstance.getComponentId());
        batchRecordComponent.setComponentDetail(Optional.ofNullable(batchRecordComponentDetailMapper.selectById(batchRecordComponent.getId()))
                .map(BatchRecordComponentDetail::getComponentDetail)
                .orElse(null));
        treeList.add(batchRecordComponent);
        Set<Long> processedIds = new HashSet<>();
        List<Long> parentIds = new ArrayList<>();
        parentIds.add(componentInstance.getComponentId());
        List<BatchRecordComponent> componentList = batchRecordComponentMapper.selectComponentList(componentInstance.getRecordItemId(), componentInstance.getRecordVersionId());
        while (!parentIds.isEmpty()) {
            try {
                // 一次性查询所有当前层级的子节点
                List<Long> finalParentIds = parentIds;
                List<BatchRecordComponent> children = componentList.stream()
                        .filter(item -> finalParentIds.contains(item.getParentId()))
                        .sorted(Comparator.comparingLong(BatchRecordComponent::getId))
                        .collect(Collectors.toList());
                for (BatchRecordComponent child : children) {
                    if (processedIds.add(child.getId())) { // 确保不重复处理
                        treeList.add(child);
                    }
                }
                // 更新下一层级的父节点ID列表
                List<Long> nextParentIds = new ArrayList<>();
                for (BatchRecordComponent child : children) {
                    nextParentIds.add(child.getId());
                }
                parentIds = nextParentIds;
            } catch (Exception e) {
                log.error("组件查询异常", e);
            }
        }


        // 查询所有的formData列表 作为操作实例
        List<ExecuteFormData> executeFormData = executeFormDataService.selectByProductPlanIdAndItemIds(componentInstance.getProductPlanId(), treeList.stream().map(BatchRecordComponent::getRecordItemId).collect(Collectors.toList()));
        List<ProcedureStepConfig> configs = procedureStepConfigMapper.selectByProcessVersion(componentInstance.getProcessId(), componentInstance.getProcessVersion());
        List<FormDataOPT> list = BusinessComponentInstanceConvert.INSTANCE.convertToOPT(executeFormData, treeList, configs, componentInstance);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list;
    }


    public FormDataOPT getFormDataOPTTree(Long componentsInstanceId) {
        List<FormDataOPT> list = getFormDataOPTList(componentsInstanceId);
        BmosTreeUtil.buildTree(list, 0L);
        // 只返回单个根节点
        return list.get(0);
    }

    // 保存组件值
    public void saveFormDataOPT(List<FormDataOPT> formDataOPTs, BusinessComponentInstance componentInstance) {
        if (componentInstance == null) {
            return;
        }
        Plan plan = planMapper.selectById(componentInstance.getProductPlanId());
        if (plan == null) {
            return;
        }

        List<FormDataOPT> sameValueList = new ArrayList<>();
        Map<Long, FormDataOPT> optMap = this.getFormDataOPTList(componentInstance.getId())
                .stream()
                .collect(Collectors.toMap(FormDataOPT::getFieldId, item -> item, (v1, v2) -> v1));
        for (FormDataOPT formDataOPT : formDataOPTs) {
            FormDataOPT opt = optMap.get(formDataOPT.getFieldId());
            if (opt != null && Objects.equals(opt.getValue(), formDataOPT.getValue())){
                sameValueList.add(formDataOPT);
            }
        }

        if (CollectionUtil.isNotEmpty(sameValueList)){
            formDataOPTs.removeAll(sameValueList);
        }

        BusinessDataHandleBaseDTO dto = new BusinessDataHandleBaseDTO();
        dto.setComponentId(componentInstance.getComponentId());
        dto.setProductPlanId(plan.getId());
        dto.setBatchNo(plan.getBatchNo());
        dto.setProcessId(plan.getProcessId());
        dto.setProcessVersion(plan.getProcessVersion());
        dto.setRecordItemId(componentInstance.getRecordItemId());
        dto.setRecordVersionId(componentInstance.getRecordVersionId());
        dto.setProcedureStepId(componentInstance.getProcedureStepId());
        dto.setProcedureStepModelId(componentInstance.getProcedureStepModelId());
        dto.setReuse(componentInstance.getReuse());
        dto.setCopyVersion(componentInstance.getCopyVersion());

        List<ExecuteFormData> list = new ArrayList<>();
        for (FormDataOPT formDataOPT : formDataOPTs) {
            ExecuteFormData executeFormData = new ExecuteFormData();
            executeFormData.setValue(formDataOPT.getValue());
            if (TimeUtil.isDateFormat(formDataOPT.getValue(), F_DATE)) {
                Long timestamp = TimeUtil.getStartOfDateTimestamp(LocalDate.parse(formDataOPT.getValue()));
                executeFormData.setExtInfo(timestamp == null ? null : JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(timestamp.toString())));
            } else if (TimeUtil.isDateFormat(formDataOPT.getValue(), F_DATETIME)) {
                Long timestamp = TimeUtil.getTimestamp(LocalDateTime.parse(formDataOPT.getValue(), DateTimeFormatter.ofPattern(F_DATETIME)));
                executeFormData.setExtInfo(timestamp == null ? null : JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(timestamp.toString())));
            }
            executeFormData.setProductPlanId(plan.getId());
            executeFormData.setBatchNo(plan.getBatchNo());
            executeFormData.setProcessId(plan.getProcessId());
            executeFormData.setProcessVersion(plan.getProcessVersion());
            executeFormData.setRecordItemId(formDataOPT.getRecordItemId());
            executeFormData.setFieldId(formDataOPT.getFieldId());
            executeFormData.setComponentType(formDataOPT.getComponentType());
            executeFormData.setProcedureStepId(componentInstance.getProcedureStepId());
            executeFormData.setReuse(componentInstance.getReuse());
            executeFormData.setDiscard(false);
            executeFormData.setSystemCreate(false);
            executeFormData.setCopyVersion(componentInstance.getCopyVersion());
            executeFormData.setOperationType(formDataOPT.getFormDataId() != null ? ExecuteFormDataType.UPDATE.getValue() : ExecuteFormDataType.SAVE.getValue());
            executeFormData.setOperationTime(LocalDateTime.now());
            executeFormData.setOperationUser(SysUserHolder.getUser().getUserId());
            list.add(executeFormData);
        }
        executeFormDataService.saveResultsAndHandleRelationComponentData(list, dto, false);
    }

    private List<BusinessComponentInstance> getComponentInstance(Long productPlanId,
                                                                 Long procedureStepId,
                                                                 Long recordItemId,
                                                                 Long recordVersionId,
                                                                 Boolean reuse, Long copyVersion) {
        return businessComponentInstanceMapper.getComponentInstances(productPlanId, procedureStepId, recordItemId, recordVersionId, reuse, copyVersion);
    }

    public void copyComponentInstance(Long productPlanId, Long procedureStepId, Long recordItemId, Long recordVersionId, Boolean reuse, Long copyVersion, Long newVersion) {
        List<BusinessComponentInstance> exist = getComponentInstance(productPlanId, procedureStepId, recordItemId, recordVersionId, reuse, newVersion);
        if (CollectionUtil.isNotEmpty(exist)){
            log.info("已存在组件版本：{}，不再重复复制", newVersion);
            return;
        }
        List<BusinessComponentInstance> componentInstances = getComponentInstance(productPlanId, procedureStepId, recordItemId, recordVersionId, reuse, copyVersion);
        if (CollectionUtil.isNotEmpty(componentInstances)) {
            componentInstances.forEach(item -> {
                item.setCopyVersion(newVersion);
                item.setId(null);
            });
            log.info("复制组件版本{} -> {}", copyVersion, newVersion);
            businessComponentInstanceMapper.insertBatch(componentInstances);
        }
    }

    /**
     * 填充组件值
     *
     * @param list 组件视图列表
     * @param opts formDataOpt列表
     * @throws IllegalAccessException
     */
    public void fillFormDataOPT(List<?> list, List<FormDataOPT> opts) {

        List<FormDataOPT> excludeOPTs = new ArrayList<>();

        // 详情组件
        List<Field> detailList = new ArrayList<>();

        // 汇总组件
        // 分组字段
        List<Field> groupByList = new ArrayList<>();
        // 回显字段
        List<Field> summaryList = new ArrayList<>();

        // 获取映射信息 统计注解标注的字段
        this.reflectList(list, detailList, summaryList, groupByList);

        // 映射详情组件
        this.fillDetailComponents(list, opts, detailList, excludeOPTs);

        // 映射汇总组件
        this.fillSummaryComponents(list, opts, groupByList, summaryList, excludeOPTs);

        opts.removeAll(excludeOPTs);
    }

    public void fillFormDataOPT(Object obj, List<FormDataOPT> opts) {

        List<FormDataOPT> excludeOPTs = new ArrayList<>();

        // 详情组件
        List<Field> detailList = new ArrayList<>();

        // 获取映射信息 统计注解标注的字段
        this.reflect(obj, detailList);

        // 映射详情组件
        this.fillDetailComponents(obj, opts, detailList, excludeOPTs);

        opts.removeAll(excludeOPTs);
    }

    private void reflect(Object obj, List<Field> detailList) {
        if (obj == null) {
            return;
        }
        Class<?> aClass = obj.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getDeclaredAnnotation(BmosComponentDetail.class) != null) {
                detailList.add(field);
            }
        }
    }

    /**
     * 详情组件
     *
     * @param list       组件视图
     * @param opts       formDataOpt
     * @param detailList 详情组件字段列表
     * @throws IllegalAccessException
     */
    private void fillDetailComponents(List<?> list, List<FormDataOPT> opts, List<Field> detailList, List<FormDataOPT> excludeOPTS) {
        if (CollectionUtil.isEmpty(list)) {
            excludeOPTS.addAll(opts);
            return;
        }
        for (Field field : detailList) {
            BmosComponentDetail detailAnnotation = field.getDeclaredAnnotation(BmosComponentDetail.class);
            // 组件类型
            BusinessComponentTypeEnum componentType = detailAnnotation.value();
            List<FormDataOPT> sameTypeList = opts.stream()
                    .filter(item -> Objects.equals(item.getComponentType(), componentType.getValue()))
                    .collect(Collectors.toList());
            int detailCount = 0;
            for (Object o : list) {
                if (detailCount >= sameTypeList.size()) {
                    continue;
                }
                FormDataOPT opt = sameTypeList.get(detailCount++);
                if (opt != null) {
                    try {
                        String newValue = field.get(o).toString();
                        if (Objects.equals(opt.getValue(), newValue)) {
                            // 相同的值不重复更新
                            excludeOPTS.add(opt);
                        }
                        opt.setValue(newValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * 详情组件
     *
     * @param obj        组件视图
     * @param opts       formDataOpt
     * @param detailList 详情组件字段列表
     * @throws IllegalAccessException
     */
    private void fillDetailComponents(Object obj, List<FormDataOPT> opts, List<Field> detailList, List<FormDataOPT> excludeOPTS) {
        if (obj == null) {
            excludeOPTS.addAll(opts);
            return;
        }
        List<FormDataOPT> clearValueOpts = new ArrayList<>(opts);
        for (Field field : detailList) {
            BmosComponentDetail detailAnnotation = field.getDeclaredAnnotation(BmosComponentDetail.class);
            // 组件类型
            BusinessComponentTypeEnum componentType = detailAnnotation.value();
            List<FormDataOPT> sameTypeList = opts.stream()
                    .filter(item -> Objects.equals(item.getComponentType(), componentType.getValue()))
                    .collect(Collectors.toList());
            FormDataOPT opt = sameTypeList.get(0);
            if (opt != null) {
                try {
                    if (field.get(obj) != null){
                        String newValue = field.get(obj).toString();
                        if (Objects.equals(opt.getValue(), newValue)) {
                            // 相同的值不重复更新
                            excludeOPTS.add(opt);
                        }
                        opt.setValue(newValue);
                        clearValueOpts.remove(opt);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        clearValueOpts.forEach(item -> item.setValue(null));
    }

    /**
     * 汇总组件
     *
     * @param list        组件视图
     * @param opts        formDataOpt
     * @param groupByList 汇总逻辑分组列表
     * @param summaryList 汇总组件字段列表
     */
    private void fillSummaryComponents(List<?> list, List<FormDataOPT> opts, List<Field> groupByList, List<Field> summaryList, List<FormDataOPT> excludeOPTS) {
        if (CollectionUtil.isEmpty(list)) {
            excludeOPTS.addAll(opts);
            return;
        }
        List<Integer> configIndex = new ArrayList<>();
        BmosComponentSummaryConfig summaryConfig = list.get(0).getClass().getDeclaredAnnotation(BmosComponentSummaryConfig.class);
        if (summaryConfig == null){
            return;
        }
        String summaryComponentType = summaryConfig.value().getValue();
        // 配置中的字段名
        Function<String, String[]> function = summaryConfig.filter().getFunction();
        String[] fieldNames = summaryConfig.filter().getFieldNames();
        List<FormDataOPT> summaryOPTs = opts.stream()
                .filter(item -> Objects.equals(item.getComponentType(), summaryComponentType))
                .collect(Collectors.toList());
        LinkedHashMap<Integer, String> configValueMap = new LinkedHashMap<>();
        for (int i = 0; i < summaryOPTs.size(); i++) {
            String configJson = summaryOPTs.get(i).getConfigJson();
            configValueMap.put(i, ArrayUtil.join(function.apply(configJson), "_"));
        }

        // 根据汇总逻辑分组列表进行分组
        LinkedHashMap<String, ? extends List<?>> group = list.stream()
                .collect(Collectors.groupingBy(item -> {
                    List<String> groupKeys = new ArrayList<>();
                    for (Field field : groupByList) {
                        try {
                            groupKeys.add(field.get(item).toString());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return StrUtil.join("_", groupKeys);
                }, LinkedHashMap::new, Collectors.toList()));
        ArrayList<String> groupKeyList = new ArrayList<>(group.keySet());
        for (String s : groupKeyList) {
            Object o = group.get(s).get(0);
            List<String> fieldValueList = new ArrayList<>();
            for (String fieldName : fieldNames) {
                try {
                    Field field = o.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    String value = field.get(o).toString();
                    fieldValueList.add(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (CollectionUtil.isNotEmpty(fieldValueList) && CollectionUtil.isNotEmpty(configValueMap)) {
                String valueKey = StrUtil.join("_", fieldValueList);
                for (Map.Entry<Integer, String> entry : configValueMap.entrySet()) {
                    if (Objects.equals(entry.getValue(), valueKey)) {
                        configIndex.add(entry.getKey());
                    }else {
                        configIndex.add(null);
                    }
                }
                // 空白的索引从小到大填充
                for (int i = 0; i < configIndex.size(); i++) {
                    if (configIndex.get(i) == null) {
                        for (int j = 0; j < configIndex.size(); j++) {
                            if (!configIndex.contains(j)){
                                configIndex.set(i, j);
                                break;
                            }
                        }
                    }
                }
            }
        }

        int groupCount = 0;
        for (Map.Entry<String, ? extends List<?>> entry : group.entrySet()) {
            List<?> valueList = entry.getValue();
            for (Field field : summaryList) {
                // 每个汇总字段
                BmosComponentSummary summaryAnnotation = field.getDeclaredAnnotation(BmosComponentSummary.class);
                BmosComponentSummaryType[] summaryType = summaryAnnotation.summaryType();
                BusinessComponentTypeEnum[] componentType = summaryAnnotation.value();
                for (int i = 0; i < summaryType.length; i++) {
                    // 根据汇总类型计算formData值
                    String value = this.getSummaryValueByType(field, summaryType[i], valueList);
                    int finalI = i;
                    List<FormDataOPT> optList = opts.stream()
                            .filter(item -> Objects.equals(item.getComponentType(), componentType[finalI].getValue()))
                            .collect(Collectors.toList());
                    if (groupCount >= optList.size()) {
                        // 超出配置的组件数量不回填
                        continue;
                    }
                    FormDataOPT opt = optList.get(configIndex.get(groupCount));
                    if (opt != null) {
                        if (Objects.equals(opt.getValue(), value)) {
                            // 相同的值不重复更新
                            excludeOPTS.add(opt);
                        }
                        opt.setValue(value);
                    }
                }
            }
            groupCount++;
        }
    }

    /**
     * 根据组件类型获取汇总数据值
     *
     * @param field       字段
     * @param summaryType 汇总类型
     * @param valueList   小组数据
     * @return formData值
     */
    @Nullable
    private String getSummaryValueByType(Field field, BmosComponentSummaryType summaryType, List<?> valueList) {
        String value = null;
        switch (summaryType) {
            // 求和
            case SUM:
                value = valueList
                        .stream()
                        .map(item -> {
                            try {
                                return field.get(item);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .map(item -> new BigDecimal(item.toString()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .toString();
                break;
            case SIZE:
                // 计数
                value = String.valueOf(valueList.size());
                break;
            case STATIC:
                // 静态值
                try {
                    value = field.get(valueList.get(0)).toString();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 通过反射获取组件注解配置
     *
     * @param list        组件视图列表
     * @param detailList  详情组件字段列表
     * @param summaryList 汇总组件字段列表
     * @param groupByList 汇总逻辑分组字段列表
     */
    private void reflectList(List<?> list, List<Field> detailList, List<Field> summaryList, List<Field> groupByList) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        Object o = list.get(0);
        Class<?> aClass = o.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getDeclaredAnnotation(BmosComponentDetail.class) != null) {
                detailList.add(field);
            }
            if (field.getDeclaredAnnotation(BmosComponentSummary.class) != null) {
                summaryList.add(field);
            }
            if (field.getDeclaredAnnotation(BmosComponentSummaryGroupBy.class) != null) {
                groupByList.add(field);
            }
        }
    }

    private String buildProcedureStepConfigUnionKey(ProcedureStepConfig procedureStepConfig) {
        if (procedureStepConfig == null) {
            return null;
        }
        return procedureStepConfig.getProcedureStepModelId() + "_" +
                procedureStepConfig.getComponentId() + "_" +
                procedureStepConfig.getProcessId() + "_" +
                procedureStepConfig.getVersion();
    }

    public static void main(String[] args) {
        List<Integer> configIndex = new ArrayList<>();
        configIndex.add(1);
        configIndex.add(null);
        configIndex.add(4);
        configIndex.add(5);
        configIndex.add(null);
        configIndex.add(null);
        for (int i = 0; i < configIndex.size(); i++) {
            if (configIndex.get(i) == null) {
                for (int j = 0; j < configIndex.size(); j++) {
                    if (!configIndex.contains(j)){
                        configIndex.set(i, j);
                        break;
                    }
                }
            }
        }
        System.out.println(configIndex);
    }
}
