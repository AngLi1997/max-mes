package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.model.StorageMaterialDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物料预定组件 - 批次
 */
@Service(value = "MATERIAL_RESERVE_BATCH")
public class MaterialReserveBatchComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;


    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        // 过滤出操作值非null的
        List<StorageMaterialDetailInfo> reservedList = info.getStorageMaterialDetailInfoList().stream()
                .filter(StorageMaterialDetailInfo::operateData).collect(Collectors.toList());
        Map<Long, String> valueMap = CollectionUtils.convertMap(info.getFormDataCollection(),
                FormDataItemVO::getFieldId, FormDataItemVO::getValue);
        List<ComponentListVO> children = component.getChildren();
        // 判断是否是取消预订后的数据
        BusinessComponentConfigDetailVO configDetailVO = configMap.getOrDefault(component.getId(), new BusinessComponentConfigDetailVO());
        StorageMaterialDetailInfo currentBatch;
        ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(configDetailVO.getConfigInfo(),
                ProcedureStepConfigInfo.class);
        if (Objects.isNull(configInfo) || Objects.isNull(configInfo.getFormulaMaterialId())) {
            if (reservedList.size() <= index) {
                return;
            }
            currentBatch = reservedList.get(index);
        } else {
            ProductFormulaMaterial formulaMaterial = info.getFormulaInfo().getMaterialMap().get(configInfo.getFormulaMaterialId());
            List<StorageMaterialDetailInfo> list = reservedList.stream().filter(e -> {
                return Objects.equals(e.getMaterialId(), formulaMaterial.getMaterialId());
            }).collect(Collectors.toList());
            if (list.size() <= index) {
                return;
            }
            currentBatch = list.get(index);
        }

        results.addAll(children.stream()
                .map(e -> {
                    BusinessComponentTypeEnum typeEnum = BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    String value = this.getValueByType(info, typeEnum, currentBatch);
                    if (StrUtil.equals(value, valueMap.get(e.getFieldId()))) {
                        return null;
                    }
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    buildExtensionInfo(typeEnum, value, convert);
                    convert.setValue(value);
                    return convert;
                })
                .filter(e -> e != null && e.getValue() != null)
                .collect(Collectors.toList()));
    }

    private static void buildExtensionInfo(BusinessComponentTypeEnum typeEnum, String value, ExecuteFormData convert) {
        String extStr = null;
        switch (typeEnum) {
            case MATERIAL_RESERVE_BATCH_EXPIRATION_DATE:
                if (StrUtil.isNotEmpty(value)) {
                    Long timestamp = TimeUtil.getTimestamp(LocalDateTimeUtil.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
                    executeFormDataBaseExtInfo.setTimeStamp(String.valueOf(timestamp));
                    extStr = JsonUtils.toJsonString(executeFormDataBaseExtInfo);
                }
        }
        convert.setExtInfo(extStr);
        convert.setValueExtension(extStr);
    }

    private String getValueByType(ProductionDetailInfo info, BusinessComponentTypeEnum componentType,
                                  StorageMaterialDetailInfo currentBatch) {
        // 被删除的预定无法查出批次 但是需要将他的填入的量置为0
        boolean deleteBatch = Objects.isNull(currentBatch.getMaterialId());
        ProductFormulaMaterial formulaMaterial = deleteBatch ?
                new ProductFormulaMaterial() :
                info.getFormulaInfo().getMaterialIdMap().get(currentBatch.getMaterialId());
        switch (componentType) {
            case MATERIAL_RESERVE_BATCH_MATERIAL_NAME:
                return formulaMaterial.getMaterialName();
            case MATERIAL_RESERVE_BATCH_MATERIAL_CODE:
                return formulaMaterial.getMaterialMergeCode();
            case MATERIAL_RESERVE_BATCH_MATERIAL_SPECIFICATION:
                return formulaMaterial.getMaterialSpecification();
            case MATERIAL_RESERVE_BATCH_BATCH_NO:
                return currentBatch.getMaterialBatchNo();
            case MATERIAL_RESERVE_BATCH_BATCH_RESERVE_QUANTITY:
                return deleteBatch ? BigDecimal.ZERO.toPlainString() :
                        MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(currentBatch.getOperateQuantity(),
                                formulaMaterial.getUnitId()), formulaMaterial).toPlainString();
            case MATERIAL_RESERVE_BATCH_UNIT:
                return unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
            case MATERIAL_RESERVE_BATCH_MOISTURE:
                return currentBatch.getHydration();
            case MATERIAL_RESERVE_BATCH_CONTENT:
                return currentBatch.getNoHydrationContent();
            case MATERIAL_RESERVE_BATCH_SUPPLIER:
                return currentBatch.getSupplier();
            case MATERIAL_RESERVE_BATCH_MANUFACTURER:
                return currentBatch.getProducer();
            case MATERIAL_RESERVE_BATCH_ORIGINAL_BATCH_NO:
                return currentBatch.getFactoryBatchNo();
            case MATERIAL_RESERVE_BATCH_ORIGINAL_CODE:
                return currentBatch.getOriginalBatchNo();
            case MATERIAL_RESERVE_BATCH_REPORT_NO:
                return currentBatch.getReportNo();
            case MATERIAL_RESERVE_BATCH_RELEASE_NO:
                return currentBatch.getLicenceNo();
            case MATERIAL_RESERVE_BATCH_EXPIRATION_DATE:
                return currentBatch.getExpiredDate();
            case MATERIAL_RESERVE_BATCH_CURRENT_BATCH_RESERVE_QUANTITY:
                return deleteBatch ? BigDecimal.ZERO.toPlainString() :
                        MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(currentBatch.getRemainingQuantity(),
                                formulaMaterial.getUnitId()), formulaMaterial).toPlainString();
            default:
                return null;
        }
    }


}
