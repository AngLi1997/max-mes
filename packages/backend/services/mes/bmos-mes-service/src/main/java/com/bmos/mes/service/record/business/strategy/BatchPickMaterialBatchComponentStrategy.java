package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.requisition.model.RequisitionMaterialReserved;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 按批次量领料-->物料批次
 */
@Service(value = "BATCH_QUANTITY_PICK_BATCH")
public class BatchPickMaterialBatchComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long reservedId;
        if (ObjectUtil.isNotNull(index)) {
            List<RequisitionMaterialReserved> repositoryReservedList = info.getRepositoryReservedList();
            List<RequisitionMaterialReserved> sorted = repositoryReservedList.stream()
                    .sorted(Comparator.comparing(RequisitionMaterialReserved::getFormulaMaterialId)
                            .thenComparing(RequisitionMaterialReserved::getCreateTime)).collect(Collectors.toList());
            if (sorted.size() <= index) {
                return;
            }
            reservedId = sorted.get(index).getId();
        } else {
            return;
        }
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    BusinessComponentTypeEnum componentType = BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    convert.setValue(this.getValueByType(info, componentType, reservedId));
                    convert.setExtInfo(this.buildExtInfo(componentType, convert.getValue()));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String buildExtInfo(BusinessComponentTypeEnum componentType, String value) {
        ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
        switch (componentType) {
            case BATCH_QUANTITY_PICK_EXPIRATION_DATE:
                if (ObjectUtil.isNull(value)) {
                    break;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(value));
            default:
                break;
        }
        return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
    }

    private String convertToTimeStamp(String value) {
        LocalDateTime parse = LocalDateTimeUtil.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime zonedDateTime = parse.atZone(ZoneId.systemDefault());
        return String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }

    private String getValueByType(ProductionDetailInfo info, BusinessComponentTypeEnum componentType, Long id) {
        String value = null;
        Map<Long, RequisitionMaterialReserved> reservedMap =
                CollectionUtils.convertMap(info.getRepositoryReservedList(),
                RequisitionMaterialReserved::getId);
        RequisitionMaterialReserved requisitionMaterialReserved = reservedMap.get(id);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial =
                formulaMaterialMap.get(requisitionMaterialReserved.getFormulaMaterialId());
        switch (componentType) {
            case BATCH_QUANTITY_PICK_BATCH_NAME:
                value = requisitionMaterialReserved.getMaterialName();
                break;
            case BATCH_QUANTITY_PICK_BATCH_CODE:
                value = requisitionMaterialReserved.getMergeCode();
                break;
            case BATCH_QUANTITY_PICK_BATCH_SPECIFICATION:
                value = requisitionMaterialReserved.getSpecification();
                break;
            case BATCH_QUANTITY_PICK_BATCH_BATCHNO:
                value = requisitionMaterialReserved.getMaterialBatchNo();
                break;
            case BATCH_QUANTITY_PICK_PLAN_PICK:
                value = MaterialQuantityCalculateUtil
                        .roundingOff(requisitionMaterialReserved.getPlannedQuantity(), formulaMaterial).toPlainString();
                break;
            case BATCH_QUANTITY_PICK_THEORY_MATERIAL:
                value = MaterialQuantityCalculateUtil
                        .roundingOff(requisitionMaterialReserved.getTheoreticalQuantity(), formulaMaterial).toPlainString();
                break;
            case BATCH_QUANTITY_PICK_UNIT:
                value = unitCache.getGlobalUnitName(requisitionMaterialReserved.getUnitId());
                break;
            case BATCH_QUANTITY_PICK_MOISTURE:
                value = BusinessComponentStrategy.getDecimalStripString(requisitionMaterialReserved.getHydration());
                break;
            case BATCH_QUANTITY_PICK_CONTENT:
                value = BusinessComponentStrategy.getDecimalStripString(requisitionMaterialReserved.getNoHydrationContent());
                break;
            case BATCH_QUANTITY_PICK_SUPPLIER:
                value = requisitionMaterialReserved.getSupplier();
                break;
            case BATCH_QUANTITY_PICK_PRODUCER:
                value = requisitionMaterialReserved.getProducer();
                break;
            case BATCH_QUANTITY_PICK_ORIGIN_BATCHNO:
                value = requisitionMaterialReserved.getOriginBatchNo();
                break;
            case BATCH_QUANTITY_PICK_ORIGIN_CODE:
                value = requisitionMaterialReserved.getMergeCode();
                break;
            case BATCH_QUANTITY_PICK_EXPIRATION_DATE:
                value = requisitionMaterialReserved.getExpiredDate().toString();
                break;
        }
        return value;
    }
}
