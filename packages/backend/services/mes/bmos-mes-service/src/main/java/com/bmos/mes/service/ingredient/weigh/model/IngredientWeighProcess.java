package com.bmos.mes.service.ingredient.weigh.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配料称量(业务组件)
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_ingredient_weigh_process")
public class IngredientWeighProcess extends BaseDO {

    /**
     * 生产计划id
     */
    private Long productPlanId;

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
     * 配料计划id
     */
    private Long ingredientPlanId;

    /**
     * 称量人id
     */
    private String preWeigherId;

    /**
     * 复核人id
     */
    private String preReCheckerId;

    /**
     * 备注
     */
    private String remark;
}
