package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.enums.storage.ChargeRecycleTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.manage.model.StorageMaterialChargeRecycle;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 投料回收-->物料汇总
 */
@Service(value = "FEED_RECYCLE_SUMMARY")
public class ChargeRecycleSumComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        List<StorageMaterialChargeRecycle> chargeRecycleList = info.getChargeRecycleList();
        Map<Long, ProductFormulaMaterial> materialMap =
                CollectionUtils.convertMap(info.getFormulaInfo().getMaterials(), ProductFormulaMaterial::getMaterialId);
        if (ObjectUtil.isNotNull(index)) {
            List<Long> ids = chargeRecycleList.stream()
                    .map(StorageMaterialChargeRecycle::getMaterialId).distinct().collect(Collectors.toList());
            if (ids.size() <= index) {
                return;
            }
            Long materialId = ids.get(index);
            formulaMaterialId = materialMap.get(materialId).getId();
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
        }
        ProductFormulaMaterial formulaMaterial = info.getFormulaInfo().getMaterialMap().get(formulaMaterialId);
        Map<Long, List<StorageMaterialChargeRecycle>> chargeRecycleMaterialMap = CollectionUtils.convertMultiMap(chargeRecycleList
                , StorageMaterialChargeRecycle::getMaterialId);
        List<StorageMaterialChargeRecycle> list = chargeRecycleMaterialMap.get(formulaMaterial.getMaterialId());
        if(CollUtil.isEmpty(list)){
            return;
        }
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(getValueByType(info, e.getComponentType(), formulaMaterialId));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(ProductionDetailInfo info, String type, Long id) {
        List<StorageMaterialChargeRecycle> chargeRecycleList = info.getChargeRecycleList();
        Map<Long, List<StorageMaterialChargeRecycle>> materialMap = CollectionUtils.convertMultiMap(chargeRecycleList
                , StorageMaterialChargeRecycle::getMaterialId);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(id);
        List<StorageMaterialChargeRecycle> list = materialMap.get(formulaMaterial.getMaterialId());
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        String value = null;
        switch (enumByValue) {
            case FEED_RECYCLE_SUMMARY_MATERIAL_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case FEED_RECYCLE_SUMMARY_MATERIAL_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case FEED_RECYCLE_SUMMARY_MATERIAL_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case FEED_RECYCLE_SUMMARY_TOTAL_QUANTITY:
                if(CollUtil.isNotEmpty(list)){
                    BigDecimal total = list.stream()
                            .filter(e -> ChargeRecycleTypeEnum.CHARGE.equals(e.getOperationType()))
                            .map(e-> unitCache.convert(e.getQuantity(), e.getUnitId(), formulaMaterial.getUnitId()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    value = MaterialQuantityCalculateUtil.roundingOff(total, formulaMaterial).toPlainString();
                }
                break;
            case FEED_RECYCLE_SUMMARY_RECYCLE_TOTAL_QUANTITY:
                if(CollUtil.isNotEmpty(list)){
                    BigDecimal total = list.stream()
                            .filter(e -> ChargeRecycleTypeEnum.RECYCLE.equals(e.getOperationType()))
                            .map(e -> unitCache.convert(e.getQuantity(), e.getUnitId(), formulaMaterial.getUnitId()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    value = MaterialQuantityCalculateUtil.roundingOff(total, formulaMaterial).toPlainString();
                }
                break;
            case FEED_RECYCLE_SUMMARY_USE_TOTAL_QUANTITY:
                if(CollUtil.isNotEmpty(list)){
                    BigDecimal chargeTotal =  list.stream()
                            .filter(e -> ChargeRecycleTypeEnum.CHARGE.equals(e.getOperationType()))
                            .map(e-> unitCache.convert(e.getQuantity(), e.getUnitId(), formulaMaterial.getUnitId()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal recycleTotal = list.stream()
                            .filter(e -> ChargeRecycleTypeEnum.RECYCLE.equals(e.getOperationType()))
                            .map(e-> unitCache.convert(e.getQuantity(), e.getUnitId(), formulaMaterial.getUnitId()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    value = MaterialQuantityCalculateUtil.roundingOff(chargeTotal.subtract(recycleTotal), formulaMaterial).toPlainString();
                }
                break;
            case FEED_RECYCLE_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case FEED_RECYCLE_SUMMARY_TOTAL_NUMBER:
                if(CollUtil.isEmpty(list)){
                    value = "0";
                    break;
                }
                value = String.valueOf(list.stream()
                        .filter(e -> ChargeRecycleTypeEnum.CHARGE.equals(e.getOperationType())).count());
                break;
            case FEED_RECYCLE_SUMMARY_RECYCLE_TOTAL_NUMBER:
                if(CollUtil.isEmpty(list)){
                    value = "0";
                    break;
                }
                value = String.valueOf(list.stream()
                        .filter(e -> ChargeRecycleTypeEnum.RECYCLE.equals(e.getOperationType())).count());
                break;
        }
        return value;
    }
}
