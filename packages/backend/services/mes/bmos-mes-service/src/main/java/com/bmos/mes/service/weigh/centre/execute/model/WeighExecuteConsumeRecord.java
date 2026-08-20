package com.bmos.mes.service.weigh.centre.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 称量执行添加物料记录
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 18:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_weigh_execute_consume_record")
public class WeighExecuteConsumeRecord extends BaseDO {

    /**
     * 称量任务id
     */
    private Long taskId;

    /**
     * 称量需求id
     */
    private Long requirementId;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 消耗量
     */
    private BigDecimal consumeQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 消耗时间
     */
    private LocalDateTime consumeTime;

    /**
     * 称量消耗物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 称量消耗物料件id
     */
    private Long storageMaterialId;

    /**
     * 称量消耗物料件编号
     */
    private String storageMaterialNo;
}
