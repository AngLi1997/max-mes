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
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.model.StorageMaterialDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料预定组件-->物料汇总
 */
@Service(value = "MATERIAL_RESERVE_SUMMARY")
public class MaterialReserveSummaryComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long materialId;
        List<StorageMaterialDetailInfo> reservedList = info.getStorageMaterialDetailInfoList();
        Map<Long, List<StorageMaterialDetailInfo>> reservedMap = CollectionUtils.convertMultiMap(reservedList,
                StorageMaterialDetailInfo::getMaterialId);
        List<ComponentListVO> children = component.getChildren();
        Map<Long, String> valueMap = CollectionUtils.convertMap(info.getFormDataCollection(), FormDataItemVO::getFieldId, FormDataItemVO::getValue);
        // 第一次进入处理
        boolean firstTimeHandle = checkFirstTimeHandle(component, info);
        if (ObjectUtil.isNotNull(index)) {
            // 若无配方物料顺序 则按预定顺序进行回填
            List<Long> ids = reservedList.stream()
                    .map(StorageMaterialDetailInfo::getMaterialId).distinct().collect(Collectors.toList());
            if (ids.size() <= index && firstTimeHandle) {
                return;
            }
            materialId = ids.size() <= index ? 0L : ids.get(index);
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            Long formulaMaterialId = configInfo.getFormulaMaterialId();
            Map<Long, ProductFormulaMaterial> formulaMaterialMap = info.getFormulaInfo().getMaterialMap();
            materialId = formulaMaterialMap.get(formulaMaterialId).getMaterialId();
        }
        List<StorageMaterialDetailInfo> storageMaterialDetailInfoList = reservedMap.getOrDefault(materialId, new ArrayList<>());
        if(CollUtil.isEmpty(storageMaterialDetailInfoList) && firstTimeHandle){
            return;
        }
        results.addAll(children
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    String value = this.getValueByType(info, e.getComponentType(),
                            storageMaterialDetailInfoList, materialId);
                    if (ObjectUtil.equal(value, valueMap.get(e.getFieldId()))) {
                        return null;
                    }
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(value);
                    return convert;
                }).filter(e-> e != null && e.getValue() != null).collect(Collectors.toList()));
    }

    private String getValueByType(ProductionDetailInfo info, String type, List<StorageMaterialDetailInfo> list, Long materialId) {
        String value = null;
        Map<Long, ProductFormulaMaterial> materialMap = info.getFormulaInfo().getMaterialIdMap();
        ProductFormulaMaterial formulaMaterial = materialMap.getOrDefault(materialId, new ProductFormulaMaterial());
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        boolean empty = CollUtil.isEmpty(list);
        switch (enumByValue) {
            case MATERIAL_RESERVE_MATERIAL_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case MATERIAL_RESERVE_MATERIAL_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case MATERIAL_RESERVE_MATERIAL_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case MATERIAL_RESERVE_BATCH_NO:
                value = empty ? StrUtil.EMPTY : BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getMaterialBatchNo()) ? StrUtil.DASHED : e.getMaterialBatchNo())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_BATCH_RESERVE_QUANTITY:
                List<String> collect = list.stream().map(e -> {
                    BigDecimal reserveQuantity = e.operateData() ? e.getOperateQuantity() : BigDecimal.ZERO;
                    BigDecimal ext = unitCache.toExt(reserveQuantity, formulaMaterial.getUnitId());
                    return MaterialQuantityCalculateUtil.roundingOff(ext, formulaMaterial).toPlainString();
                }).collect(Collectors.toList());
                value = empty ? BigDecimal.ZERO.toPlainString() : BusinessComponentStrategy.getLFStrings(collect);
                break;
            case MATERIAL_RESERVE_RESERVE_TOTAL_QUANTITY:
                BigDecimal reduce =
                        list.stream().filter(StorageMaterialDetailInfo::operateData).map(StorageMaterialDetailInfo::getOperateQuantity).reduce(BigDecimal.ZERO,
                                BigDecimal::add);
                value = empty ? BigDecimal.ZERO.toPlainString() :
                        MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(reduce,
                                formulaMaterial.getUnitId()), formulaMaterial).toPlainString();
                break;
            case MATERIAL_RESERVE_BATCH_CURRENT_RESERVE_QUANTITY:
                List<String> remainList = list.stream().map(e -> {
                    BigDecimal remainingQuantity = e.getRemainingQuantity();
                    BigDecimal ext = unitCache.toExt(remainingQuantity, formulaMaterial.getUnitId());
                    return MaterialQuantityCalculateUtil.roundingOff(ext, formulaMaterial).toPlainString();
                }).collect(Collectors.toList());
                value = empty ? BigDecimal.ZERO.toPlainString() : BusinessComponentStrategy.getLFStrings(remainList);
                break;
            case MATERIAL_RESERVE_CURRENT_RESERVE_TOTAL_QUANTITY:
                BigDecimal reduceRemain =
                        list.stream().map(StorageMaterialDetailInfo::getRemainingQuantity).reduce(BigDecimal.ZERO,
                                BigDecimal::add);
                value = empty ? BigDecimal.ZERO.toPlainString() :
                        MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(reduceRemain,
                                formulaMaterial.getUnitId()), formulaMaterial).toPlainString();
                break;
            case MATERIAL_RESERVE_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case MATERIAL_RESERVE_MOISTURE:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getHydration()) ? StrUtil.DASHED : e.getHydration())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_CONTENT:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getNoHydrationContent()) ? StrUtil.DASHED : e.getNoHydrationContent())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_SUPPLIER:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getSupplier()) ? StrUtil.DASHED : e.getSupplier())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_MANUFACTURER:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getProducer()) ? StrUtil.DASHED : e.getProducer())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_ORIGINAL_BATCH_NO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getFactoryBatchNo()) ? StrUtil.DASHED : e.getFactoryBatchNo())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_ORIGINAL_CODE:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getOriginalBatchNo()) ? StrUtil.DASHED : e.getOriginalBatchNo())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_REPORT_NO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getReportNo()) ? StrUtil.DASHED : e.getReportNo())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_RELEASE_NO:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e->  StrUtil.isEmpty(e.getLicenceNo()) ? StrUtil.DASHED : e.getLicenceNo())
                        .collect(Collectors.toList()));
                break;
            case MATERIAL_RESERVE_EXPIRATION_DATE:
                value = BusinessComponentStrategy.getLFStrings(list.stream()
                        .map(e-> StrUtil.isEmpty(e.getExpiredDate()) ? StrUtil.DASHED : e.getExpiredDate())
                        .collect(Collectors.toList()));
                break;
        }
        return value;
    }

    boolean checkFirstTimeHandle(ComponentListVO component, ProductionDetailInfo info) {
        Collection<FormDataItemVO> formDataCollection = info.getFormDataCollection();
        Map<Long, String> valueMap = CollectionUtils.convertMap(formDataCollection, FormDataItemVO::getFieldId, FormDataItemVO::getValue);
        List<ComponentListVO> children = component.getChildren();
        // 过滤出当前层级汇总下存在值的字段
        List<Long> existedFields = children.stream().map(e -> {
            Long fieldId = e.getFieldId();
            String value = valueMap.get(fieldId);
            if (value != null) {
                return e.getFieldId();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // 第一次进入处理
        return CollUtil.isEmpty(existedFields);
    }
}
