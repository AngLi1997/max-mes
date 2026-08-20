package com.bmos.mes.service.ingredient.weigh.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.ingredient.IngredientWeighStatus;
import com.bmos.mes.common.enums.ingredient.WeighProcess;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 配料称量批次
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_ingredient_weigh_batch_process")
public class IngredientWeighBatchProcess extends BaseDO {

    /**
     * 生产计划id
     */
    private Long ingredientWeighProcessId;

    /**
     * 配料单id
     */
    private Long ingredientPlanId;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    private Long copyVersion;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 暂存物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 称量状态
     */
    private IngredientWeighStatus weighStatus;

    /**
     * 已投物料量
     */
    private BigDecimal putInQuantity = BigDecimal.ZERO;

    /**
     * 称量人id
     */
    private String weigherId;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 称量阶段
     */
    private WeighProcess weighProcess;

    /**
     * 添加投料
     *
     * @param quantity
     */
    public void addPuts(BigDecimal quantity) {
        this.putInQuantity = this.putInQuantity.add(quantity);
    }
}
