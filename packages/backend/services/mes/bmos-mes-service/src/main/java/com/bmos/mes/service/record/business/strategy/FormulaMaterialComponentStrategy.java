package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 配方信息物料业务组件处理
 */
@Service(value = "BUSINESS_FORMULA_INFO_MATERIAL")
@Slf4j
public class FormulaMaterialComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        if (ObjectUtil.isNotNull(index)) {
            List<ProductFormulaMaterial> materials = info.getFormulaInfo().getMaterials();
            if (materials.size() <= index) {
                return;
            }
            formulaMaterialId = materials.get(index).getId();
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if(ObjectUtil.isNull(config)){
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(), ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
        }
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> ObjectUtil.isNotNull(e.getFieldId()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(this.getValueByType(info, e.getComponentType(), formulaMaterialId));
                    return convert;
                })
                .collect(Collectors.toList()));
    }

    private String getValueByType(ProductionDetailInfo info, String type, Long id) {
        String value = null;
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        Plan plan = Optional.ofNullable(info.getPlan()).orElse(new Plan());
        ProductFormulaInfo productFormulaInfo =
                Optional.ofNullable(info.getFormulaInfo()).orElse(new ProductFormulaInfo());
        Map<Long, ProductFormulaMaterial> materialMap = productFormulaInfo.getMaterialMap();
        ProductFormulaMaterial formulaMaterial = materialMap.get(id);
        switch (enumByValue){
            case BUSINESS_FORMULA_INFO_MATERIAL_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case BUSINESS_FORMULA_INFO_MATERIAL_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case BUSINESS_FORMULA_INFO_MATERIAL_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case BUSINESS_FORMULA_INFO_THEORETICAL_QUANTITY:
                try {
                    value = BusinessComponentStrategy.calculateQuantity(plan.getBatchQuantity(), productFormulaInfo.getBatchQuantity(), formulaMaterial);
                } catch (Exception e) {
                    e.printStackTrace();
                    // todo 异常计算值处理
                    value = "error";
                }
                break;
            case BUSINESS_FORMULA_INFO_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
        }
        return value;
    }
}
