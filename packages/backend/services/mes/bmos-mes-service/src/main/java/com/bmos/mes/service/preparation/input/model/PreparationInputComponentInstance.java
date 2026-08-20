package com.bmos.mes.service.preparation.input.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * (BmPreparationInputComponentInstance)实体类
 *
 * @author makejava
 * @since 2024-08-01 12:55:24
 */
@Getter
@Setter
@TableName("bm_preparation_input_component_instance")
public class PreparationInputComponentInstance extends BaseDO {

    /**
     * 生产计划id
     */
    private Long productPlanId;
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
     * 是否复用
     */
    private Boolean reuse;
    /**
     * 绑定的配液单id
     */
    private Long preparationPlanId;
    /**
     * 记录项id
     */
    private Long recordItemId;
    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 是否完成
     */
    private Boolean complete;

}

