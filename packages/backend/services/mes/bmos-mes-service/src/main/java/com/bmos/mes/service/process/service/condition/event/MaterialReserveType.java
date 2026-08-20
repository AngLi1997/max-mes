package com.bmos.mes.service.process.service.condition.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.common.enums.process.task.MaterialCheckEnum;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物料预定变化事件
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
@Slf4j
public class MaterialReserveType extends ConditionChangeType {
    private List<Long> materialIds;

    public MaterialReserveType(Long planId, List<Long> materialId) {
        super(planId);
        this.materialIds = materialId;
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.MATERIAL_RESERVE_NUMBER;
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
        ProductFormulaInfo productFormulaInfo = conditionCalculateContext.getProductFormulaInfo();
        changeConditionInstances.forEach(instance -> {
            if(!StrUtil.equals(instance.getConditionType(),ConditionTypeEnum.MATERIAL_RESERVE_NUMBER.getValue())){
                return;
            }
            ConditionDetailVO conditionDetailVO = JSONUtil.toBean(instance.getConditionDetails(),
                    ConditionDetailVO.class);
            ProductFormulaMaterial productFormulaMaterial = productFormulaInfo.getMaterialMap().get(conditionDetailVO.getMaterialId());
            if (ObjectUtil.isEmpty(productFormulaMaterial)){
                return;
            }
            Boolean result = this.compareQuantity(instance,conditionCalculateContext, conditionDetailVO,
                    productFormulaMaterial.getId());
            instance.setTaskResult(result);
        });
    }

    private Boolean compareQuantity(ProcedureConditionInstance instance, ConditionCalculateContext conditionCalculateContext,ConditionDetailVO conditionDetailVO,
                                    Long formulaMartialId) {
        ProductFormulaInfo productFormulaInfo = conditionCalculateContext.getProductFormulaInfo();
        if (productFormulaInfo == null) {
            return true;
        }
        ProductFormulaMaterial productFormulaMaterial = productFormulaInfo.getMaterialMap().get(formulaMartialId);
        if (productFormulaMaterial == null) {
            return true;
        }
        List<BatchReservedMaterialVO> filter =
                conditionCalculateContext.getReserveList().stream()
                        .filter(e-> e.isAvailable() && Objects.equals(e.getMaterialId(), productFormulaMaterial.getMaterialId())).collect(Collectors.toList());
        // 根据配方修约与水分含量计算理论量
        filter.forEach(e -> {
            BigDecimal quantity =
                    MaterialQuantityCalculateUtil.roundingOff(conditionCalculateContext.getUnitCache().toExt(e.getReserveQuantity(),
                            productFormulaMaterial.getUnitId()),
                            productFormulaMaterial);
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(quantity,
                    e.getHydration(), e.getNoHydrationContent(), productFormulaMaterial));
        });
        BigDecimal reduce =
                filter.stream().map(BatchReservedMaterialVO::getTheoreticalQuantity).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        productFormulaMaterial.setQuantity(conditionDetailVO.getNumber());
        BigDecimal bigDecimal =
                MaterialQuantityCalculateUtil.calculateQuantity(conditionCalculateContext.getPlan().getBatchQuantity(),
                        productFormulaInfo.getBatchQuantity(), productFormulaMaterial);
        log.info("计划id【{}】步骤模型id:【{}】条件【{}】配置的物料预定量发生变更，预期【{}】,实际【{}】", this.planId,instance.getProcedureStepModelId(),
                instance.getName(), conditionDetailVO.getNumber(), bigDecimal);
        //根据物料配置符号返回比较结果集
        List<Integer> resultList = MaterialCheckEnum.getResultList(conditionDetailVO.getCheckRule());
        return resultList.contains(reduce.compareTo(bigDecimal));
    }

}
