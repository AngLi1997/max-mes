package com.bmos.mes.service.preparation.measure.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.preparation.MeasureStageEnum;
import com.bmos.mes.common.enums.preparation.MeasureStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 配液量取称量批次
 */
@Data
@TableName("bm_liquid_preparation_measure_batch")
public class LiquidPreparationMeasureBatch extends BaseDO {

    /**
     * 配液批次记录主键id
     */
    private Long liquidPreparationPlanBatchId;

    /**
     * 量取组件实例id
     */
    private Long measureInstanceId;

    /**
     * 配液计划id
     */
    private Long liquidPreparationPlanId;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 量取状态
     */
    private MeasureStatusEnum measureStatus;

    /**
     * 量取阶段
     */
    private MeasureStageEnum measureStage;

    /**
     * 投入物料量 按照配方物料修约后量
     */
    private BigDecimal putQuantity = BigDecimal.ZERO;

    /**
     * 量取人id
     */
    private String measurerId;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 备注
     */
    private String remark;

    public void putQuantity(BigDecimal quantity) {
        this.putQuantity = this.putQuantity.add(quantity);
    }

}
