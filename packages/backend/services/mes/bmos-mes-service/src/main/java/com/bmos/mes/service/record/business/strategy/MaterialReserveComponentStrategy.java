package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物料预定组件
 */
@Service(value = "MATERIAL_RESERVE")
public class MaterialReserveComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private MaterialReserveSummaryComponentStrategy summaryComponentStrategy;

    @Autowired
    private MaterialReserveBatchComponentStrategy batchComponentStrategy;


    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        Map<String, List<ComponentListVO>> childMap = CollectionUtils.convertMultiMap(children,
                ComponentListVO::getComponentType);
        List<ComponentListVO> batch = childMap.get(BusinessComponentTypeEnum.MATERIAL_RESERVE_BATCH.getValue());
        handleBatch(results, info, configMap, batch, batchComponentStrategy);
        List<ComponentListVO> summary = childMap.get(BusinessComponentTypeEnum.MATERIAL_RESERVE_SUMMARY.getValue());
        handleSummary(results, info, configMap, summary, summaryComponentStrategy);
    }


    private void handleBatch(List<ExecuteFormData> results, ProductionDetailInfo info, Map<Long,
            BusinessComponentConfigDetailVO> configMap, List<ComponentListVO> children,
                             BusinessComponentStrategy componentStrategy) {
        if (CollUtil.isEmpty(children)) {
            return;
        }
        List<ComponentListVO> batchCollect =
                children.stream().filter(c -> {
                    BusinessComponentConfigDetailVO configDetail = configMap.get(c.getId());
                    if (Objects.isNull(configDetail)) {
                        return false;
                    }
                    ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(configDetail.getConfigInfo(),
                            ProcedureStepConfigInfo.class);
                    if (Objects.isNull(configInfo) || Objects.isNull(configInfo.getFormulaMaterialId())) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
        if (CollUtil.isEmpty(batchCollect)) {
            handleNoConfig(results, info, configMap, children, componentStrategy);
        } else {
            handleBatchFillSort(results, info, configMap, componentStrategy, batchCollect);
        }
    }

    /**
     * 处理批次填入顺序
     *
     * @param results
     * @param info
     * @param configMap
     * @param componentStrategy
     * @param batchCollect
     */
    private static void handleBatchFillSort(List<ExecuteFormData> results, ProductionDetailInfo info, Map<Long,
            BusinessComponentConfigDetailVO> configMap, BusinessComponentStrategy componentStrategy,
                                            List<ComponentListVO> batchCollect) {
        Map<Long, Integer> materialCountMap = new HashMap<>();
        for (ComponentListVO child : batchCollect) {
            BusinessComponentConfigDetailVO config = configMap.get(child.getId());
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            Long formulaMaterialId = configInfo.getFormulaMaterialId();
            materialCountMap.compute(formulaMaterialId, (k, v) -> (v == null) ? 0 : v + 1);
            componentStrategy.handleBusinessComponent(results, child, info, configMap,
                    materialCountMap.get(formulaMaterialId));
        }
    }

    private void handleSummary(List<ExecuteFormData> results, ProductionDetailInfo info,
                               Map<Long, BusinessComponentConfigDetailVO> configMap,
                               List<ComponentListVO> children, BusinessComponentStrategy componentStrategy) {
        if (CollUtil.isEmpty(children)) {
            return;
        }
        List<ComponentListVO> summaryCollect =
                children.stream().filter(c -> {
                    BusinessComponentConfigDetailVO configDetail = configMap.get(c.getId());
                    if (Objects.isNull(configDetail)) {
                        return false;
                    }
                    ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(configDetail.getConfigInfo(),
                            ProcedureStepConfigInfo.class);
                    if (Objects.isNull(configInfo) || Objects.isNull(configInfo.getFormulaMaterialId())) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
        if (CollUtil.isEmpty(summaryCollect)) {
            handleNoConfig(results, info, configMap, children, componentStrategy);
        } else {
            children.forEach(child -> {
                this.summaryComponentStrategy.handleBusinessComponent(results, child, info, configMap, null);
            });
        }
    }

    /**
     * 处理完全无配置的填入
     *
     * @param results
     * @param info
     * @param configMap
     * @param children
     * @param componentStrategy
     */
    private static void handleNoConfig(List<ExecuteFormData> results, ProductionDetailInfo info, Map<Long,
            BusinessComponentConfigDetailVO> configMap, List<ComponentListVO> children,
                                       BusinessComponentStrategy componentStrategy) {
        for (int i = 0; i < children.size(); i++) {
            ComponentListVO child = children.get(i);
            componentStrategy.handleBusinessComponent(results, child, info, configMap, i);
        }
    }
}
