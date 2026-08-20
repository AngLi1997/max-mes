package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.requisition.model.RequisitionMaterialReserved;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 按批次量领料-->物料汇总
 */
@Service(value = "BATCH_QUANTITY_PICK_SUMMARY")
public class BatchPickMaterialSumComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        List<RequisitionMaterialReserved> reservedList = info.getRepositoryReservedList();
        Map<Long, List<RequisitionMaterialReserved>> reservedMap = CollectionUtils.convertMultiMap(reservedList,
                RequisitionMaterialReserved::getFormulaMaterialId);
        if (ObjectUtil.isNotNull(index)) {
            // 按照配方物料进行排序
            List<Long> ids = reservedList.stream()
                    .sorted(Comparator.comparing(RequisitionMaterialReserved::getFormulaMaterialId))
                    .map(RequisitionMaterialReserved::getFormulaMaterialId)
                    .distinct()
                    .collect(Collectors.toList());
            if (ids.size() <= index) {
                return;
            }
            formulaMaterialId = ids.get(index);
            if (CollUtil.isEmpty(reservedMap.get(formulaMaterialId))) {
                return;
            }
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
            if (CollUtil.isEmpty(reservedMap.get(formulaMaterialId))) {
                return;
            }
        }
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(this.getValueByType(info, e.getComponentType(),
                            formulaMaterialId));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(ProductionDetailInfo info, String type, Long id) {
        String value = null;
        Map<Long, ProductFormulaMaterial> materialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial = materialMap.get(id);
        Map<Long, List<RequisitionMaterialReserved>> reservedFormulaMap =
                CollectionUtils.convertMultiMap(info.getRepositoryReservedList(),
                        RequisitionMaterialReserved::getFormulaMaterialId);
        List<RequisitionMaterialReserved> reserveds = reservedFormulaMap.get(id);
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        switch (enumByValue) {
            case BATCH_QUANTITY_PICK_BATCH_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case BATCH_QUANTITY_PICK_BATCH_SUMMARY_PLAN_PICK:
                if (CollUtil.isNotEmpty(reserveds)) {
                    value = reserveds.stream()
                            .map(RequisitionMaterialReserved::getPlannedQuantity)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .stripTrailingZeros()
                            .toPlainString();
                }
                break;
            case BATCH_QUANTITY_PICK_BATCH_SUMMARY_THEORY_MATERIAL:
                if (CollUtil.isNotEmpty(reserveds)) {
                    value = reserveds.stream()
                            .map(RequisitionMaterialReserved::getTheoreticalQuantity)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .stripTrailingZeros()
                            .toPlainString();
                }
                break;
            case BATCH_QUANTITY_PICK_BATCH_SUMMARY_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case BATCH_QUANTITY_PICK_BATCH_SUMMARY_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case BATCH_QUANTITY_PICK_BATCH_SUMMARY_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
        }
        return value;
    }
}
