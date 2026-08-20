package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.preparation.measure.service.vo.MeasuredBatchDetailVO;
import com.bmos.mes.service.preparation.measure.vo.MeasureResultRecordVO;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationMaterialBatchMapper;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatch;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 配液量取组件->物料汇总
 */
@Service(value = "LIQUID_PREPARATION_MEASURE_SUMMARY")
public class LiquidMeasureSummaryComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Resource
    private LiquidPreparationMaterialBatchMapper preparationBatchMapper;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        Map<Long, List<MeasureResultRecordVO>> map = CollectionUtils.convertMultiMap(info.getMeasureResultRecordList(),
                MeasureResultRecordVO::getFormulaMaterialId);
        List<MeasuredBatchDetailVO> measuredBatchDetailVOS = info.getMeasuredBatchDetailVOS();
        MeasuredBatchDetailVO first = CollUtil.getFirst(measuredBatchDetailVOS);
        List<Long> ids = measuredBatchDetailVOS.stream()
                        .map(MeasuredBatchDetailVO::getFormulaMaterialId)
                        .distinct()
                        .collect(Collectors.toList());
        if (ObjectUtil.isNotNull(index)) {
            if (ids.size() <= index) {
                return;
            }
            formulaMaterialId = ids.get(index);
            // 若物料只有一个配液批次且直接完成了物料批次的配液量取（未产生配液量取物料件），该物料汇总数据的物料量填0到批记录中
            List<MeasureResultRecordVO> recordVOS = map.get(formulaMaterialId);
            if (CollUtil.isEmpty(recordVOS) && preparationBatchMapper.selectCount(new LambdaQueryWrapperX<LiquidPreparationMaterialBatch>()
                    .eq(LiquidPreparationMaterialBatch::getFormulaMaterialId, formulaMaterialId)
                    .eq(LiquidPreparationMaterialBatch::getLiquidPreparationPlanId, first.getLiquidPreparationPlanId())) > 1) {
                return;
            }
        } else {
                BusinessComponentConfigDetailVO config = configMap.get(component.getId());
                if (ObjectUtil.isNull(config)) {
                    return;
                }
                ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                        ProcedureStepConfigInfo.class);
                formulaMaterialId = configInfo.getFormulaMaterialId();
            }
        if (info.getCurrentMeasureFormulaMaterialId() != null
                && !Objects.equals(formulaMaterialId, info.getCurrentMeasureFormulaMaterialId())) {
            return;
        }

        List<MeasureResultRecordVO> recordVOS = map.getOrDefault(formulaMaterialId, new ArrayList<>());
        ProductFormulaMaterial formulaMaterial = info.getFormulaInfo().getMaterialMap().get(formulaMaterialId);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(this.getValueByType(recordVOS, e.getComponentType(),
                            formulaMaterial));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(List<MeasureResultRecordVO> recordVOS, String type,
                                  ProductFormulaMaterial formulaMaterial) {
        String value = null;
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        switch (enumByValue) {
            case LIQUID_PREPARATION_MEASURE_SUMMARY_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case LIQUID_PREPARATION_MEASURE_SUMMARY_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case LIQUID_PREPARATION_MEASURE_SUMMARY_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case LIQUID_PREPARATION_MEASURE_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case LIQUID_PREPARATION_MEASURE_SUMMARY_TOTAL_COUNT:
                value = String.valueOf(recordVOS.size());
                break;
            case LIQUID_PREPARATION_MEASURE_SUMMARY_TOTAL_QUANTITY:
                value = recordVOS.stream().map(MeasureResultRecordVO::getQuantity).reduce(BigDecimal.ZERO,
                        BigDecimal::add).toPlainString();
                break;
        }
        return value;
    }
}
