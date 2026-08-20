package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatchDetailInfo;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配液计划 -->物料汇总策略
 */
@Service
public class PreparationPlanSumComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<LiquidPreparationMaterialBatchDetailInfo> list = info.getLiquidPreparationBatchList();
        Map<Long, List<LiquidPreparationMaterialBatchDetailInfo>> map = CollectionUtils.convertMultiMap(list,
                LiquidPreparationMaterialBatchDetailInfo::getFormulaMaterialId);
        Long formulaMaterialId;
        // 若全无配置则按照配液顺序
        if (ObjectUtil.isNotNull(index)) {
            List<Long> idList = list.stream()
                    .sorted(Comparator.comparing(LiquidPreparationMaterialBatchDetailInfo::getOrder))
                    .map(LiquidPreparationMaterialBatchDetailInfo::getFormulaMaterialId).distinct()
                    .collect(Collectors.toList());
            if (idList.size() <= index) {
                return;
            }
            formulaMaterialId = idList.get(index);
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
        }
        List<LiquidPreparationMaterialBatchDetailInfo> infos = map.get(formulaMaterialId);
        if (CollUtil.isEmpty(infos)) {
            return;
        }
        ProductFormulaMaterial formulaMaterial = info.getFormulaInfo().getMaterialMap().get(formulaMaterialId);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    BusinessComponentTypeEnum enumByValue =
                            BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(this.getValueByType(formulaMaterial, enumByValue, infos));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(ProductFormulaMaterial formulaMaterial, BusinessComponentTypeEnum type,
                                  List<LiquidPreparationMaterialBatchDetailInfo> list) {
        String value = null;
        switch (type) {
            case LIQUID_PREPARATION_PLAN_SUMMARY_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_BATCHNO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getMaterialBatchNo() == null ? StrUtil.DASHED : e.getMaterialBatchNo())
                        .collect(Collectors.toList()));
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_QUANTITY:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getPreparationQuantity() == null ? StrUtil.DASHED :
                                e.getPreparationQuantity().toString())
                        .collect(Collectors.toList()));
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_TOTAL_QUANTITY:
                value = list.stream()
                        .map(LiquidPreparationMaterialBatchDetailInfo::getPreparationQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .toPlainString();
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_EXPIRY_DATE:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getExpiredDate() == null ? StrUtil.DASHED : e.getExpiredDate())
                        .collect(Collectors.toList()));

                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_SUPPLIER:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getSupplier() == null ? StrUtil.DASHED : e.getSupplier())
                        .collect(Collectors.toList()));
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_MANUFACTURER:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getProducer() == null ? StrUtil.DASHED : e.getProducer())
                        .collect(Collectors.toList()));

                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_ORIGINAL_BATCHNO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getFactoryBatchNo() == null ? StrUtil.DASHED : e.getFactoryBatchNo())
                        .collect(Collectors.toList()));
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_ORIGINAL_CODE:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getOriginalBatchNo() == null ? StrUtil.DASHED : e.getOriginalBatchNo())
                        .collect(Collectors.toList()));
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_REPORT_NO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getReportNo() == null ? StrUtil.DASHED : e.getReportNo())
                        .collect(Collectors.toList()));
                break;
            case LIQUID_PREPARATION_PLAN_SUMMARY_RELEASE_NO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e -> e.getLicenceNo() == null ? StrUtil.DASHED : e.getLicenceNo())
                        .collect(Collectors.toList()));
                break;
        }
        return value;
    }
}
