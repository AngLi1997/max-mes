package com.bmos.mes.service.trace.material.service.impl;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.info.convert.ProductPlanRelationConverter;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelationTreeNodeVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.trace.material.entity.MaterialTraceHistoryDO;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateDO;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateMaterial;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateProcedureStepDO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceHistoryMapper;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceTemplateMapper;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceTemplateProcedureStepMapper;
import com.bmos.mes.service.trace.material.service.IMaterialTraceService;
import com.bmos.mes.service.trace.material.vo.MaterialTraceMaterialStepView;
import com.bmos.mes.service.trace.material.vo.MaterialTraceMaterialView;
import com.bmos.mes.service.trace.material.vo.MaterialTraceVO;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 物料追溯实现类
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 09:55
 */
@Service
@Slf4j
public class MaterialTraceServiceImpl implements IMaterialTraceService {

    private static final String LOG_PREFIX = "[物料追溯]";

    @Resource
    private IMaterialTraceHistoryMapper materialTraceHistoryMapper;

    @Resource
    private IMaterialTraceTemplateMapper materialTraceTemplateMapper;

    @Resource
    private IMaterialTraceTemplateProcedureStepMapper materialTraceTemplateProcedureStepMapper;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProductPlanRelationService productPlanRelationService;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private UnitCache unitCache;

    @Override
    public MaterialTraceVO traceData(Long productPlanId) {

        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        Long processId = plan.getProcessId();
        MaterialTraceTemplateDO template = materialTraceTemplateMapper.selectEnabledTemplateByProcessId(processId);
        if (template == null) {
            // 无启用的模板
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_PROCESS_NO_TEMPLATE);
        }

        ProductMaterial product = productMaterialMapper.selectById(template.getProductId());
        if (product == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }

        // 查询产线信息
        List<FactoryLineDetailFeignVO> lines = FeignUtils.handleRequest(data -> factoryFeign.getLineDetailByLineIds(data, true), Collections.singleton(plan.getProductionLineId())).getData();
        if (!CollectionUtils.isAnyEmpty(lines)) {
            FactoryLineDetailFeignVO line = lines.get(0);
            if (line != null) {
                plan.setLineName(line.getCode() + StrUtil.DASHED + line.getName());
            }
        }

        List<ProductPlanRelation> list = productPlanRelationService.getList(productPlanId);
        List<ProductPlanRelationTreeNodeVO> relationTree = this.buildRelationTree(list);
        Map<Long, Set<Long>> relationScope = this.flatRelationTree(relationTree);
        // 批次+关联批次id
        // 所有批次的关联批次范围, 每个节点追溯物料的时候不超过这个范围
        Set<Long> planIdList = CollectionUtils.convertSet(list, ProductPlanRelation::getRelationProductPlanId);
        planIdList.add(plan.getId());
        Map<Long, Plan> planMap = CollectionUtils.convertMap(planMapper.selectBatchIds(planIdList), Plan::getId, Function.identity());
        // 配置的物料树
        List<MaterialTraceTemplateMaterial> materialConfigTree = template.getMaterialTree();
        // 平铺
        List<MaterialTraceTemplateMaterial> materialConfigFlatList = new ArrayList<>();
        this.flatMaterialConfigTree(materialConfigTree, materialConfigFlatList);
        Set<Long> materialIds = CollectionUtils.convertSet(materialConfigFlatList, MaterialTraceTemplateMaterial::getMaterialId);
        // 单位信息map
        Map<Long, Long> materialUnitMap = productMaterialMapper.selectBatchIds(materialIds)
                .stream()
                .collect(Collectors.toMap(BaseDO::getId, ProductMaterial::getUnitId, (k1, k2) -> k1));

        // 配置的物料工序步骤列表
        List<MaterialTraceTemplateProcedureStepDO> stepConfigList = materialTraceTemplateProcedureStepMapper.selectByTemplateId(template.getId());
        Map<Long, List<MaterialTraceTemplateProcedureStepDO>> relationStepMap = stepConfigList.stream().collect(Collectors.groupingBy(MaterialTraceTemplateProcedureStepDO::getRelationId));

        // 关联

        // 1.生产计划和关联批次下的数据
        // 2.满足配置的工步范围条件
        // 3.满足配置的物料范围条件
        List<MaterialTraceHistoryDO> histories = materialTraceHistoryMapper.queryTraceHistory(planIdList,
                CollectionUtils.convertSet(stepConfigList, MaterialTraceTemplateProcedureStepDO::getProcedureStepId),
                CollectionUtils.convertSet(materialConfigFlatList, MaterialTraceTemplateMaterial::getMaterialId));

        Map<Long, Plan> sourceProductPlanMap = new HashMap<>();
        List<Long> sourceProductPlanIds = CollectionUtils.convertList(histories, MaterialTraceHistoryDO::getSourceProductPlanId).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isAnyEmpty(sourceProductPlanIds)) {
            sourceProductPlanMap = planMapper.selectBatchIds(sourceProductPlanIds)
                    .stream()
                    .collect(Collectors.toMap(Plan::getId, Function.identity(), (k1, k2) -> k1));
            if (!sourceProductPlanMap.isEmpty()) {
                List<MaterialTraceHistoryDO> materialTraceHistoryDOS = materialTraceHistoryMapper.queryTraceHistory(sourceProductPlanMap.keySet(),
                        CollectionUtils.convertSet(stepConfigList, MaterialTraceTemplateProcedureStepDO::getProcedureStepId),
                        CollectionUtils.convertSet(materialConfigFlatList, MaterialTraceTemplateMaterial::getMaterialId));
                Set<Long> longs = CollectionUtils.convertSet(histories, MaterialTraceHistoryDO::getId);
                for (MaterialTraceHistoryDO materialTraceHistoryDO : materialTraceHistoryDOS) {
                    if (!longs.contains(materialTraceHistoryDO.getId())) {
                        histories.add(materialTraceHistoryDO);
                    }
                }
            }
        }
        // 因为产出批次不受关联批次限制, 原来的批次map不一定包含产出的批次信息 所以需要把所有产出计划都放入 用于查询工艺信息
        sourceProductPlanMap.putAll(planMap);
        List<MaterialTraceMaterialView> tree = this.buildViewTree(materialConfigTree, relationStepMap, planMap, materialUnitMap, sourceProductPlanMap, histories, relationScope, plan, null);
        return this.buildResult(plan, product, tree);
    }

    /**
     * 平铺关联树
     *
     * @param relationTree 关联树
     * @return 节点id和所有子节点id
     */
    private Map<Long, Set<Long>> flatRelationTree(List<ProductPlanRelationTreeNodeVO> relationTree) {
        if (CollectionUtils.isAnyEmpty(relationTree)) {
            return new HashMap<>();
        }
        Map<Long, Set<Long>> result = new HashMap<>();
        for (ProductPlanRelationTreeNodeVO node : relationTree) {
            this.collectChildren(result, node);
        }
        return result;
    }

    private void collectChildren(Map<Long, Set<Long>> result, ProductPlanRelationTreeNodeVO node) {
        result.putIfAbsent(node.getRelationProductPlanId(), new HashSet<>());
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            for (ProductPlanRelationTreeNodeVO child : node.getChildren()) {
                result.get(node.getRelationProductPlanId()).add(child.getRelationProductPlanId());
                collectChildren(result, child);
            }
        }
    }

    /**
     * 构造关联树
     *
     * @param relationList 关联信息
     * @return 关联树
     */
    private List<ProductPlanRelationTreeNodeVO> buildRelationTree(List<ProductPlanRelation> relationList) {
        List<ProductPlanRelationTreeNodeVO> list = ProductPlanRelationConverter.INSTANCE.convertToVO(relationList);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        // 创建一个哈希表用于快速查找
        HashMap<Long, ProductPlanRelationTreeNodeVO> map = new HashMap<>();
        for (ProductPlanRelationTreeNodeVO relation : list) {
            map.put(relation.getProductPlanId(), relation);
            relation.setChildren(new ArrayList<>());
        }
        // 根节点集合
        List<ProductPlanRelationTreeNodeVO> rootNodes = new ArrayList<>();
        for (ProductPlanRelationTreeNodeVO relation : list) {
            ProductPlanRelationTreeNodeVO parent = relation.getRelationProductPlanId() == null ?
                    null :
                    map.get(relation.getRelationProductPlanId());
            if (parent != null) {
                parent.getChildren().add(relation);
            } else {
                rootNodes.add(relation);
            }
        }
        return rootNodes;
    }

    private void flatMaterialConfigTree(List<MaterialTraceTemplateMaterial> materialConfigTree, List<MaterialTraceTemplateMaterial> flatList) {
        if (CollectionUtils.isAnyEmpty(materialConfigTree)) {
            return;
        }
        for (MaterialTraceTemplateMaterial node : materialConfigTree) {
            flatList.add(node);
            if (!CollectionUtils.isAnyEmpty(node.getChildren())) {
                flatMaterialConfigTree(node.getChildren(), flatList);
            }
        }
    }

    /**
     * 构造追溯树
     *
     * @param materialConfigTree     物料追溯配置物料树
     * @param relationStepMap        物料追溯配置工步
     * @param planMap                生产计划map
     * @param sourceProductPlanMap   来源生产计划map
     * @param histories              历史
     * @param relationScope          关联批次范围
     * @param plan                   生产计划id
     * @param storageMaterialBatchId 物料批次id
     * @return 物料追溯树
     */
    private List<MaterialTraceMaterialView> buildViewTree(List<MaterialTraceTemplateMaterial> materialConfigTree,
                                                          Map<Long, List<MaterialTraceTemplateProcedureStepDO>> relationStepMap,
                                                          Map<Long, Plan> planMap,
                                                          Map<Long, Long> materialUnitMap,
                                                          Map<Long, Plan> sourceProductPlanMap,
                                                          List<MaterialTraceHistoryDO> histories,
                                                          Map<Long, Set<Long>> relationScope,
                                                          Plan plan,
                                                          Long storageMaterialBatchId) {
        if (plan == null) {
            // 生产计划查询不到 不继续追溯
            return new ArrayList<>();
        }
        List<MaterialTraceMaterialView> list = new ArrayList<>();

        for (MaterialTraceTemplateMaterial node : materialConfigTree) {

            List<MaterialTraceTemplateProcedureStepDO> steps = relationStepMap.get(node.getId());
            if (CollectionUtils.isAnyEmpty(steps)) {
                continue;
            }
            List<Long> stepIds = CollectionUtils.convertList(steps, MaterialTraceTemplateProcedureStepDO::getProcedureStepId);

            // 根据生产批号、物料批号分组
            Map<Pair<Long, Long>, List<MaterialTraceHistoryDO>> group = new HashMap<>();
            if (Objects.equals(node.getMaterialType(), CategoryInfoTypeEnum.PRODUCTION.getValue())) {
                // 产品层
                List<MaterialTraceHistoryDO> materialHistory = histories.stream()
                        .filter(item -> item.getMaterialCategoryType() == CategoryInfoTypeEnum.PRODUCTION)
                        .filter(item -> stepIds.contains(item.getProcedureStepId()) && Objects.equals(item.getMaterialId(), node.getMaterialId()))
                        .collect(Collectors.toList());
                // 处理单层节点
                this.extractList(relationStepMap, planMap, sourceProductPlanMap, materialUnitMap, histories, node, materialHistory, list, relationScope, plan, storageMaterialBatchId, true);
            } else {
                // 中间品 原辅包层
                List<MaterialTraceHistoryDO> materialHistory = histories.stream()
                        .filter(item -> item.getMaterialCategoryType() != CategoryInfoTypeEnum.PRODUCTION)
                        .filter(item -> stepIds.contains(item.getProcedureStepId()) && Objects.equals(item.getMaterialId(), node.getMaterialId()))
                        .collect(Collectors.toList());
                // 只查询消耗的下级关联
                List<MaterialTraceHistoryDO> groupList = materialHistory.stream()
                        .filter(item -> item.getTraceType() == MaterialTraceType.CONSUME)
                        .collect(Collectors.toList());
                for (MaterialTraceHistoryDO history : groupList) {
                    group.computeIfAbsent(Pair.of(history.getStorageMaterialBatchId(), history.getSourceProductPlanId()), k -> new ArrayList<>());
                    group.get(Pair.of(history.getStorageMaterialBatchId(), history.getSourceProductPlanId())).add(history);
                }
                group.forEach((key, value) -> {
                    // 处理单层节点
                    this.extractList(relationStepMap, planMap, sourceProductPlanMap, materialUnitMap, histories, node, materialHistory.stream()
                            .filter(item -> Objects.equals(item.getStorageMaterialBatchId(), key.getKey()))
                            // 追溯历史 过滤消耗批次或来源批次过滤 消耗的 产出的都要查询出来
                            .filter(item -> Objects.equals(item.getProductPlanId(), key.getValue()) || Objects.equals(item.getSourceProductPlanId(), key.getValue()))
                            .collect(Collectors.toList()), list, relationScope, planMap.get(key.getValue()), storageMaterialBatchId, false);
                });
            }
        }
        // 根据合并编码升序、批次号升序、生产编号排序
        list.sort(Comparator.comparing(MaterialTraceMaterialView::getMergeCode)
                .thenComparing(view -> {
                    if (StringUtils.isBlank(view.getStorageMaterialBatchNo())) {
                        return "";
                    } else {
                        return view.getStorageMaterialBatchNo();
                    }
                })
                .thenComparing(view -> {
                    if (StringUtils.isBlank(view.getBatchNo())) {
                        return "";
                    } else {
                        return view.getBatchNo();
                    }
                })
        );
        return list;
    }

    /**
     * 拼装list(作用是处理节点下的分组合并列表)
     *
     * @param relationStepMap        工步关联map
     * @param planMap                批次map
     * @param sourceProductPlanMap   来源批次map
     * @param histories              所有物料追溯历史（用于追溯子级列表）
     * @param node                   节点
     * @param list                   单个节点物料追溯列表
     * @param resultList             结果集
     * @param relationScope          关联批次范围
     * @param plan                   节点
     * @param storageMaterialBatchId 物料批次id                 节点
     * @param isProduct              是否为产品层
     */
    private void extractList(Map<Long, List<MaterialTraceTemplateProcedureStepDO>> relationStepMap,
                             Map<Long, Plan> planMap,
                             Map<Long, Plan> sourceProductPlanMap,
                             Map<Long, Long> materialUnitMap,
                             List<MaterialTraceHistoryDO> histories,
                             MaterialTraceTemplateMaterial node,
                             List<MaterialTraceHistoryDO> list,
                             List<MaterialTraceMaterialView> resultList,
                             Map<Long, Set<Long>> relationScope,
                             Plan plan,
                             Long storageMaterialBatchId,
                             Boolean isProduct) {

        if (isProduct) {
            // 产品层
            if (plan == null) {
                return;
            }
            MaterialTraceMaterialView item = new MaterialTraceMaterialView();
            item.setMaterialId(node.getMaterialId());
            item.setMaterialCategoryType(CategoryInfoTypeEnum.PRODUCTION);
            item.setMaterialName(node.getMaterialName());
            item.setMergeCode(node.getMergeCode());
            item.setBatchNo(plan.getBatchNo());
            item.setProductPlanId(plan.getId());
            item.setSourceBatchNo(plan.getBatchNo());
            item.setSourceProductPlanId(plan.getId());
            item.setProcessName(plan.getProcessName());
            item.setProcessVersion(plan.getProcessVersion());
            item.setSourceProcessName(plan.getProcessName());
            item.setSourceProcessVersion(plan.getProcessVersion());
            item.setShowPercentYield(node.getShowPercentYield());
            item.setChildren(this.buildViewTree(node.getChildren(), relationStepMap, planMap, materialUnitMap, sourceProductPlanMap, histories, relationScope, plan, null));
            item.setConsumeList(new ArrayList<>());
            item.setOutputList(this.convertList(list.stream()
                    .filter(c -> c.getTraceType() == MaterialTraceType.OUTPUT)
                    .filter(c -> Objects.equals(c.getProductPlanId(), plan.getId()))
                    .collect(Collectors.toList()), node.getCalcFlag()));
            item.setCalcFlag(node.getCalcFlag());
            item.setPercentYieldRange(node.getPercentYieldRange());
            Optional.of(materialUnitMap.get(node.getMaterialId()))
                    .ifPresent(unitId -> {
                        item.setUnitId(unitId);
                        item.setUnit(unitCache.getGlobalUnitName(unitId));
                    });

            resultList.add(item);
        } else {
            // 中间品/原辅包
            if (plan != null) {
                planMap.put(plan.getId(), plan);
            }
            // 消耗list
            List<MaterialTraceHistoryDO> consumeList = list.stream()
                    .filter(item -> item.getTraceType() == MaterialTraceType.CONSUME)
                    .filter(item -> planMap.containsKey(item.getProductPlanId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isAnyEmpty(consumeList)) {
                // 如果没有消耗列表就不继续向下追溯
                return;
            }
            // 产出list
            List<MaterialTraceHistoryDO> outputList = list.stream()
                    .filter(item -> item.getTraceType() == MaterialTraceType.OUTPUT)
                    .filter(item -> sourceProductPlanMap.containsKey(item.getProductPlanId()))
                    .collect(Collectors.toList());
            // 进了这个方法的物料 物料批次和生产批次都是一致的 所以可以取第一个
            MaterialTraceHistoryDO first = list.get(0);
            if (first != null) {
                MaterialTraceMaterialView item = this.transToView(first);
                Optional.ofNullable(first.getSourceProductPlanId())
                        .map(sourceProductPlanMap::get)
                        .ifPresent(p -> {
                            // 从产出工艺中查询工艺信息
                            item.setSourceProductPlanId(p.getId());
                            item.setSourceBatchNo(p.getBatchNo());
                            item.setSourceProcessName(p.getProcessName());
                            item.setSourceProcessVersion(p.getProcessVersion());
                        });
                item.setUnitId(first.getUnitId());
                item.setUnit(first.getUnitName());
                item.setCalcFlag(node.getCalcFlag());
                item.setShowPercentYield(node.getShowPercentYield());
                item.setPercentYieldRange(node.getPercentYieldRange());
                if (!CollectionUtils.isAnyEmpty(node.getChildren())) {
                    item.setChildren(this.buildViewTree(node.getChildren(), relationStepMap,
                            planMap,
                            materialUnitMap,
                            sourceProductPlanMap,
                            histories.stream()
                                    .filter(i -> {
                                        // 根据批次和来源批次过滤追溯下级
                                        if (planMap.containsKey(i.getProductPlanId()) || planMap.containsKey(i.getSourceProductPlanId())) {
                                            // storageMaterialBatchId为空代表是成品产出节点 没有物料件生成 所以取不到物料批次id
                                            // 需求又需要显示 则直接放行
                                            // 否则 根据物料批次id进行过滤追溯下级
                                            return storageMaterialBatchId == null || Objects.equals(i.getStorageMaterialBatchId(), storageMaterialBatchId);
                                        } else {
                                            return false;
                                        }
                                    })
                                    .collect(Collectors.toList()),
                            relationScope,
                            plan,
                            storageMaterialBatchId));
                }
                item.setConsumeList(this.convertList(consumeList, node.getCalcFlag()));
                item.setOutputList(this.convertList(outputList, node.getCalcFlag()));
                resultList.add(item);
            }
        }
    }

    private MaterialTraceVO buildResult(Plan plan, ProductMaterial product, List<MaterialTraceMaterialView> tree) {
        MaterialTraceVO result = new MaterialTraceVO();
        result.setProductPlanId(plan.getId());
        result.setBatchNo(plan.getBatchNo());
        result.setProductName(product.getName());
        result.setMergeCode(product.getMergeCode());
        result.setMaterialSpecification(product.getSpecification());
        result.setProductionLineName(plan.getLineName());
        result.setStartTime(plan.getStartTime());
        result.setEndTime(plan.getEndTime());
        result.setProcessId(plan.getProcessId());
        result.setProcessName(plan.getProcessName());
        result.setProcessVersion(plan.getProcessVersion());
        result.setTraceTree(tree);
        return result;
    }

    private MaterialTraceMaterialView transToView(MaterialTraceHistoryDO first) {
        MaterialTraceMaterialView item = new MaterialTraceMaterialView();
        item.setMaterialId(first.getMaterialId());
        item.setMaterialCategoryType(first.getMaterialCategoryType());
        item.setMaterialName(first.getMaterialName());
        item.setMergeCode(first.getMergeCode());
        item.setStorageMaterialBatchId(first.getStorageMaterialBatchId());
        item.setStorageMaterialBatchNo(first.getStorageMaterialBatchNo());
        item.setBatchNo(first.getBatchNo());
        item.setProductPlanId(first.getProductPlanId());
        item.setProcessName(first.getProcessName());
        item.setProcessVersion(first.getProcessVersion());
        return item;
    }

    private List<MaterialTraceMaterialStepView> convertList(List<MaterialTraceHistoryDO> consumeList, boolean calcFlag) {
        return consumeList.stream().map(m -> MaterialTraceMaterialStepView.builder()
                .id(m.getId())
                .procedureName(m.getProcedureName())
                .procedureStepName(m.getProcedureStepName())
                .processName(m.getProcessName())
                .processVersion(m.getProcessVersion())
                .materialNo(m.getStorageMaterialNo())
                .quantity(PrecisionHelper.precision(m.getQuantity(), m.getUnitId()))
                .batchNo(m.getBatchNo())
                .unitId(m.getUnitId())
                .unit(m.getUnitName())
                .calcFlag(calcFlag)
                .build()).collect(Collectors.toList());
    }
}
