package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
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
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.manage.model.StorageMaterialChargeRecycle;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 投料回收-->详情
 */
@Service(value = "FEED_RECYCLE_FEEDING_DETAILS")
public class ChargeRecycleDetailComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<StorageMaterialChargeRecycle> chargeRecycleList = info.getChargeRecycleList();
        Collection<FormDataItemVO> formDataCollection = info.getFormDataCollection();
        Collection<Long> existedFieldIdList = CollectionUtils.convertSet(formDataCollection, FormDataItemVO::getFieldId);
        if(chargeRecycleList.size() <= index){
            return;
        }
        StorageMaterialChargeRecycle storageMaterialChargeRecycle = chargeRecycleList.get(index);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> !CollUtil.contains(existedFieldIdList, e.getFieldId()) && BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(getValueByType(info, enumByValue, storageMaterialChargeRecycle.getId()));
                    convert.setExtInfo(this.buildExtInfo(enumByValue, convert.getValue()));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String buildExtInfo(BusinessComponentTypeEnum componentType, String value) {
        ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
        switch (componentType) {
            case FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TIME:
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
        LocalDateTime parse = LocalDateTimeUtil.parse(value, DatePattern.NORM_DATETIME_PATTERN);
        ZonedDateTime zonedDateTime = parse.atZone(ZoneId.systemDefault());
        return String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }

    private String getValueByType(ProductionDetailInfo info, BusinessComponentTypeEnum enumByValue, Long id) {
        List<StorageMaterialChargeRecycle> chargeRecycleList = info.getChargeRecycleList();
        Map<Long, StorageMaterialChargeRecycle> chargeRecycleMap = CollectionUtils.convertMap(chargeRecycleList, StorageMaterialChargeRecycle::getId);
        StorageMaterialChargeRecycle chargeRecycle = chargeRecycleMap.get(id);
        Long materialId = chargeRecycle.getMaterialId();
        List<ProductFormulaMaterial> materials = info.getFormulaInfo().getMaterials();
        Map<Long, ProductFormulaMaterial> materialMap = CollectionUtils.convertMap(materials, ProductFormulaMaterial::getMaterialId);
        ProductFormulaMaterial formulaMaterial = materialMap.get(materialId);
        String value = null;
        switch (enumByValue) {
            case FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_NAME:
                value = chargeRecycle.getMaterialName();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_CODE:
                value = chargeRecycle.getMaterialMergeCode();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_SPECIFICATION:
                value = chargeRecycle.getSpecification();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_BATCHNO:
                value = chargeRecycle.getMaterialBatchNo();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_PARTNO:
                value = chargeRecycle.getStorageMaterialNo();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_MATERIAL_QUANTITY:
                BigDecimal ext = unitCache.toExt(chargeRecycle.getQuantity(), formulaMaterial.getUnitId());
                value = MaterialQuantityCalculateUtil.roundingOff(ext, formulaMaterial).toPlainString();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TYPE:
                value = chargeRecycle.getOperationType().getName();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_OPERATOR:
                value = UserUtils.getUser(chargeRecycle.getOperatorId()).getUserName();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TIME:
                value = DateUtil.format(chargeRecycle.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN);
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_DEVICE_NAME:
                value = chargeRecycle.getEquipmentName();
                break;
            case FEED_RECYCLE_FEEDING_DETAILS_DEVICE_CODE:
                value = chargeRecycle.getEquipmentCode();
                break;
        }
        return value;
    }
}
