package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.output.weigh.vo.OutputWeighRecordComponentView;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产出称量汇总组件
 * @author liang
 * @version 1.0.0
 * @date 2024/5/13 14:17
 */
@Service(value = "OUTPUT_WEIGHING_SUMMARY")
public class OutputWeighSummaryComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long materialId;
        Map<Long, ProductFormulaMaterial> materialMap =
                CollectionUtils.convertMap(info.getFormulaInfo().getMaterials(), ProductFormulaMaterial::getMaterialId);
        List<OutputWeighRecordComponentView> outputWeighRecords = info.getOutputWeighRecords();
        Map<Long, List<OutputWeighRecordComponentView>> detailMap = CollectionUtils.convertMultiMap(outputWeighRecords,
                OutputWeighRecordComponentView::getMaterialId);
        List<OutputWeighRecordComponentView> detailList;
        ProductFormulaMaterial formulaMaterial;
        if (ObjectUtil.isNotNull(index)) {
            List<Long> ids = outputWeighRecords.stream()
                    .map(OutputWeighRecordComponentView::getMaterialId).distinct().collect(Collectors.toList());
            if (ids.size() <= index) {
                return;
            }
            materialId = ids.get(index);
            formulaMaterial = materialMap.get(materialId);
            detailList = detailMap.get(materialId);
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterial = info.getFormulaInfo().getMaterialMap().get(configInfo.getFormulaMaterialId());
            detailList = detailMap.get(formulaMaterial.getMaterialId());
        }
        if (detailList == null){
            detailList = new ArrayList<>();
        }
        ProductFormulaMaterial finalFormulaMaterial = formulaMaterial;
        List<OutputWeighRecordComponentView> finalDetailList = detailList;
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(getValueByType(finalFormulaMaterial, e.getComponentType(), finalDetailList));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(ProductFormulaMaterial productFormulaMaterial, String type, List<OutputWeighRecordComponentView> views) {
        if (CollectionUtil.isEmpty(views)){
            return null;
        }
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        String value = null;
        switch (enumByValue) {
            case OUTPUT_WEIGHING_SUMMARY_NAME:
                value = productFormulaMaterial.getMaterialName();
                break;
            case OUTPUT_WEIGHING_SUMMARY_CODE:
                value = productFormulaMaterial.getMaterialMergeCode();
                break;
            case OUTPUT_WEIGHING_SUMMARY_SPECIFICATION:
                value = productFormulaMaterial.getMaterialSpecification();
                break;
            case OUTPUT_WEIGHING_SUMMARY_TOTAL_QUANTITY:
                value = renderValue(views.stream()
                        .filter(e -> !Objects.equals(e.getWeighSignStatus(), WeighSignStatus.SCRAPED))
                        .map(item -> Optional.ofNullable(item.getQuantity()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add), productFormulaMaterial);
                break;
            case OUTPUT_WEIGHING_SUMMARY_NET_WEIGHT:
                value = renderValue(views.stream()
                        .filter(e -> !Objects.equals(e.getWeighSignStatus(), WeighSignStatus.SCRAPED))
                        .map(item -> Optional.ofNullable(item.getNetWeight()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add), productFormulaMaterial);
                break;
            case OUTPUT_WEIGHING_SUMMARY_TARE_WEIGHT:
                value = renderValue(views.stream()
                        .filter(e -> !Objects.equals(e.getWeighSignStatus(), WeighSignStatus.SCRAPED))
                        .map(item -> Optional.ofNullable(item.getTareWeight()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add), productFormulaMaterial);
                break;
            case OUTPUT_WEIGHING_SUMMARY_GROSS_WEIGHT:
                value = renderValue(views.stream()
                        .filter(e -> !Objects.equals(e.getWeighSignStatus(), WeighSignStatus.SCRAPED))
                        .map(item -> Optional.ofNullable(item.getGrossWeight()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add), productFormulaMaterial);
                break;
            case OUTPUT_WEIGHING_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(productFormulaMaterial.getUnitId());
                break;
            case OUTPUT_WEIGHING_SUMMARY_TOTAL_NUMBER:
                value = String.valueOf((int) views.stream().filter(e -> !Objects.equals(e.getWeighSignStatus(), WeighSignStatus.SCRAPED)).count());
                break;
        }
        return value;
    }

    private String renderValue(BigDecimal value, ProductFormulaMaterial productFormulaMaterial) {
        if (value == null){
            return null;
        }
        if (productFormulaMaterial == null){
            return null;
        }
        return unitCache.toExt(value, productFormulaMaterial.getUnitId()).stripTrailingZeros().toPlainString();
    }
}
