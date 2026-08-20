package com.bmos.mes.service.ingredient.input.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配料投入组件实例
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_ingredient_input_component_instance")
public class IngredientInputComponentInstance extends BaseDO {


    /**
     * 生产计划id
     */
    private Long productPlanId;


    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * 复制版本
     */
    private Long copyVersion;

    /**
     * 确认投入的配料单id
     */
    private Long ingredientPlanId;
}
