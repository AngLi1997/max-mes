package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.enums.RoundingEnum;
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
import com.bmos.mes.service.requisition.vo.RequisitionReceivedBatchInfo;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 领料接收-->物料汇总
 */
@Service(value = "PICKING_RECEIVING_SUMMARY")
public class PickingReceivingMaterialSumComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        // 组件都未绑定配方物料
        if (ObjectUtil.isNotNull(index)) {
            List<RequisitionReceivedBatchInfo> requisitionReceivedBatchList = info.getRequisitionReceivedBatchList();
            List<Long> ids = requisitionReceivedBatchList.stream()
                    .map(RequisitionReceivedBatchInfo::getFormulaMaterialId).distinct().collect(Collectors.toList());
            if (ids.size() <= index) {
                return;
            }
            formulaMaterialId = ids.get(index);
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
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
        List<RequisitionReceivedBatchInfo> requisitionReceivedBatchList = info.getRequisitionReceivedBatchList();
        Map<Long, List<RequisitionReceivedBatchInfo>> formulaReceivedBatchMap = CollectionUtils
                .convertMultiMap(requisitionReceivedBatchList, RequisitionReceivedBatchInfo::getFormulaMaterialId);
        List<RequisitionReceivedBatchInfo> requisitionReceivedBatchInfos = formulaReceivedBatchMap.get(id);
        // 存在汇总组件绑定配方物料但是领料时未领该配方物料的情况
        if (CollUtil.isEmpty(requisitionReceivedBatchInfos)) {
            return null;
        }
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(id);
        String value = null;
        switch (enumByValue) {
            case PICKING_RECEIVING_SUMMARY_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case PICKING_RECEIVING_SUMMARY_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case PICKING_RECEIVING_SUMMARY_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case PICKING_RECEIVING_SUMMARY_PICK:
                RoundingMode roundingMode = RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping();
                BigDecimal reduce = requisitionReceivedBatchInfos.stream()
                        .map(e -> {
                            BigDecimal quantity = e.getReceivedQuantity();
                            return unitCache.convert(quantity, e.getUnitId(), formulaMaterial.getUnitId());
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                value = MaterialQuantityCalculateUtil.roundingOff(reduce, formulaMaterial.getScale(),
                        formulaMaterial.getScaleLength(), roundingMode).toPlainString();
                break;
            case PICKING_RECEIVING_SUMMARY_UNIT:
                value = info.getUnitCache().getGlobalUnitName(formulaMaterial.getUnitId());
                break;
        }
        return value;
    }
}
