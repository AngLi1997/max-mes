package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.requisition.vo.RequisitionReceivedBatchInfo;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 领料接收-->物料批次
 */
@Service(value = "PICKING_RECEIVING_BATCH")
public class PickingReceivingBatchComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Collection<FormDataItemVO> formDataCollection = info.getFormDataCollection();
        Map<Long, String> valueMap = CollectionUtils.convertMap(formDataCollection, FormDataItemVO::getFieldId, FormDataItemVO::getValue);
        List<RequisitionReceivedBatchInfo> requisitionReceivedBatchList = info.getRequisitionReceivedBatchList();
        if (index >= requisitionReceivedBatchList.size()) {
            return;
        }
        RequisitionReceivedBatchInfo receivedBatchInfo = requisitionReceivedBatchList.get(index);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    String value = getValueByType(info, enumByValue, receivedBatchInfo.getId());
                    if(ObjectUtil.equal(value, valueMap.get(e.getFieldId()))){
                        return null;
                    }
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(value);
                    convert.setExtInfo(this.buildExtInfo(enumByValue, convert.getValue()));
                    return convert;
                }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private String buildExtInfo(BusinessComponentTypeEnum componentType, String value) {
        ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
        switch (componentType) {
            case PICKING_RECEIVING_BATCH_EXPIRATION_DATE:
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

    private String getValueByType(ProductionDetailInfo info,  BusinessComponentTypeEnum enumByValue, Long id) {
        List<RequisitionReceivedBatchInfo> requisitionReceivedBatchList = info.getRequisitionReceivedBatchList();
        Map<Long, RequisitionReceivedBatchInfo> receivedBatchIdMap = CollectionUtils
                .convertMap(requisitionReceivedBatchList, RequisitionReceivedBatchInfo::getId);
        RequisitionReceivedBatchInfo currentBatch = receivedBatchIdMap.get(id);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(currentBatch.getFormulaMaterialId());
        String value = null;
        switch (enumByValue) {
            case PICKING_RECEIVING_BATCH_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case PICKING_RECEIVING_BATCH_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case PICKING_RECEIVING_BATCH_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case PICKING_RECEIVING_BATCH_BATCHNO:
                value = currentBatch.getInventoryBatchNo();
                break;
            case PICKING_RECEIVING_BATCH_PICK:
                if (currentBatch.getReceivedQuantity() == null) {
                    value = null;
                    break;
                }
                Long unitId = currentBatch.getUnitId();
                RoundingMode roundingMode = RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping();
                value = MaterialQuantityCalculateUtil.roundingOff(unitCache.convert(currentBatch.getReceivedQuantity(), unitId,
                        formulaMaterial.getUnitId()), formulaMaterial.getScale(), formulaMaterial.getScaleLength(),
                        roundingMode).toPlainString();
                break;
            case PICKING_RECEIVING_BATCH_UNIT:
                value = info.getUnitCache().getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case PICKING_RECEIVING_BATCH_SUPPLIER:
                value = currentBatch.getSupplier();
                break;
            case PICKING_RECEIVING_BATCH_PRODUCER:
                value = currentBatch.getProducer();
                break;
            case PICKING_RECEIVING_BATCH_ORIGINAL_BATCHNO:
                value = currentBatch.getFactoryBatchNo();
                break;
            case PICKING_RECEIVING_BATCH_ORIGINAL_CODE:
                value = currentBatch.getCargoMergeCode();
                break;
            case PICKING_RECEIVING_BATCH_EXPIRATION_DATE:
                value = String.valueOf(currentBatch.getExpiredDate());
                break;
        }
        return value;
    }
}
