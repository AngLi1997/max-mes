package com.bmos.mes.service.preparation.measure.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

/**
 * 配液量取组件实例
 */
@Data
@TableName("bm_liquid_preparation_measure_instance")
public class LiquidPreparationMeasureInstance extends BaseDO {

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
     * 配液计划id
     */
    private Long liquidPreparationPlanId;

    /**
     * 量取人id
     */
    private String preMeasurerId;

    /**
     * 复核人id
     */
    private String preReCheckerId;

    /**
     * 备注
     */
    private String remark;

}
