package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.component.PreparationSummaryBasicComponentConfig;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationInputDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationInputMaterialInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 配液投入汇总组件
 */
@Component("LIQUID_PREPARATION_INPUT_MATERIAL")
public class PreparationInputSummaryComponentStrategy implements BusinessComponentStrategy {
    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        PreparationInputDetailInfo detailInfo = (PreparationInputDetailInfo) info;
        List<PreparationInputMaterialInfo> allPreparationInputMaterialInfo = Lists.newArrayList(detailInfo.getPreInputStorageMaterialList());
        allPreparationInputMaterialInfo.addAll(detailInfo.getCurrentInputStoratageMaterialList());
        // 根据物料id进行分类
        Map<Long, List<PreparationInputMaterialInfo>> materialMap = allPreparationInputMaterialInfo.stream()
                .collect(Collectors.groupingBy(PreparationInputMaterialInfo::getFormulaMaterialId));
        // 获取每个物料id的排序
        Map<Long, Integer> materialSort = new HashMap<>();
        for (Long formulaMaterialId : materialMap.keySet()) {
            Integer minSort = Integer.MAX_VALUE;
            for (PreparationInputMaterialInfo preparationInputMaterialInfo : materialMap.get(formulaMaterialId)) {
                if (preparationInputMaterialInfo.getSort() < minSort) {
                    minSort = preparationInputMaterialInfo.getSort();
                }
            }
            materialSort.put(formulaMaterialId, minSort);
        }
        List<List<PreparationInputMaterialInfo>> materialList = materialMap.entrySet().stream()
                .sorted((o1, o2) -> {
                    Integer sort1 = materialSort.get(o1.getKey());
                    Integer sort2 = materialSort.get(o2.getKey());
                    return sort1.compareTo(sort2);
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        BusinessComponentConfigDetailVO businessComponentConfigDetailVO = configMap.get(component.getId());

        PreparationSummaryBasicComponentConfig config = null;
        if (Objects.nonNull(businessComponentConfigDetailVO) && StrUtil.isNotEmpty(businessComponentConfigDetailVO.getConfigInfo())){
            config = JSON.parseObject(businessComponentConfigDetailVO.getConfigInfo(), PreparationSummaryBasicComponentConfig.class);
        }
        if (Objects.nonNull(config)){
            if (Objects.nonNull(config.getFormulaMaterialId()) && !materialMap.containsKey(config.getFormulaMaterialId())){
                // 代表不存在
                return ;
            } else if (Objects.nonNull(config.getFormulaMaterialId())){
                List<PreparationInputMaterialInfo> preparationInputMaterialInfos = materialMap.get(config.getFormulaMaterialId());
                summary(preparationInputMaterialInfos, results, component, info.getDto());
            }
        } else {
            if (index > materialList.size() - 1){
                return ;
            }
            summary(materialList.get(index), results, component, info.getDto());
        }
    }


    private void summary(List<PreparationInputMaterialInfo> preparationInputMaterialInfos, List<ExecuteFormData> results,
                         ComponentListVO componentListVO, BusinessComponentBatchSaveDTO dto){
        for (ComponentListVO child : componentListVO.getChildren()) {
            ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(dto);
            formData.setFieldId(child.getFieldId());
            formData.setComponentType(child.getComponentType());
            formData.setDiscard(false);
            formData.setValue(this.getValueByType(child.getComponentType(), preparationInputMaterialInfos));
            formData.setProductPlanId(dto.getProductPlanId());
            results.add(formData);
        }
    }

    private String getValueByType(String componentType, List<PreparationInputMaterialInfo> preparationInputMaterialInfos) {
        // 计算总量
        BigDecimal quantity = BigDecimal.ZERO;
        for (PreparationInputMaterialInfo preparationInputMaterialInfo : preparationInputMaterialInfos) {
            quantity = quantity.add(preparationInputMaterialInfo.getQuantity());
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_SUMMARY_NAME.getValue().equals(componentType)){
            return preparationInputMaterialInfos.get(0).getMaterialName();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_SUMMARY_CODE.getValue().equals(componentType)){
            return preparationInputMaterialInfos.get(0).getMaterialMergeCode();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_SUMMARY_SPECIFICATION.getValue().equals(componentType)){
            return preparationInputMaterialInfos.get(0).getSpecification();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_SUMMARY_TOTAL_QUANTITY.getValue().equals(componentType)){
            return String.valueOf(quantity);
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_SUMMARY_UNIT.getValue().equals(componentType)){
            return preparationInputMaterialInfos.get(0).getUnit();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_SUMMARY_TOTAL_COUNT.getValue().equals(componentType)){
            return String.valueOf(preparationInputMaterialInfos.size());
        }
        return null;
    }

}
