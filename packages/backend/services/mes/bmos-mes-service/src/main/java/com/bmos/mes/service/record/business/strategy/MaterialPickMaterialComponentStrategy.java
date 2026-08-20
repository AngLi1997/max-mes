package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.requisition.model.RequisitionMaterialReserved;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 按物料量领料-》物料
 */
@Service(value = "MATERIAL_QUANTITY_PICK_MATERIAL")
public class MaterialPickMaterialComponentStrategy implements BusinessComponentStrategy {

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        if (ObjectUtil.isNotNull(index)) {
            List<RequisitionMaterialReserved> repositoryReservedList = info.getRepositoryReservedList();
            repositoryReservedList.sort(Comparator.comparing(RequisitionMaterialReserved::getFormulaMaterialId));
            if (repositoryReservedList.size() <= index) {
                return;
            }
            formulaMaterialId = repositoryReservedList.get(index).getFormulaMaterialId();
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
        }
        Map<Long, RequisitionMaterialReserved> formulaReservedMap = CollectionUtils.convertMap(info.getRepositoryReservedList()
                , RequisitionMaterialReserved::getFormulaMaterialId);
        RequisitionMaterialReserved requisitionMaterialReserved = formulaReservedMap.get(formulaMaterialId);
        if(requisitionMaterialReserved == null){
            return;
        }
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> ObjectUtil.isNotNull(e.getFieldId()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(this.getValueByType(info, e.getComponentType(), requisitionMaterialReserved));
                    return convert;
                })
                .collect(Collectors.toList()));
    }

    private String getValueByType(ProductionDetailInfo info, String type, RequisitionMaterialReserved requisitionMaterialReserved) {
        String value = null;
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        UnitCache unitCache = info.getUnitCache();
        switch (enumByValue) {
            case MATERIAL_QUANTITY_PICK_MATERIAL_NAME:
                value = requisitionMaterialReserved.getMaterialName();
                break;
            case MATERIAL_QUANTITY_PICK_MATERIAL_CODE:
                value = requisitionMaterialReserved.getMergeCode();
                break;
            case MATERIAL_QUANTITY_PICK_MATERIAL_SPECIFICATION:
                value = requisitionMaterialReserved.getSpecification();
                break;
            case MATERIAL_QUANTITY_PICK_MATERIAL_PICK:
                value = BusinessComponentStrategy.getDecimalStripString(requisitionMaterialReserved.getPlannedQuantity());
                break;
            case MATERIAL_QUANTITY_PICK_MATERIAL_UNIT:
                value = unitCache.getGlobalUnitName(requisitionMaterialReserved.getUnitId());
                break;
            case MATERIAL_QUANTITY_PICK_MATERIAL_SUPPLIER:
                value = requisitionMaterialReserved.getSupplier();
                break;
            case MATERIAL_QUANTITY_PICK_MATERIAL_MANUFACTURER:
                value = requisitionMaterialReserved.getProducer();
                break;

        }
        return value;
    }


}
