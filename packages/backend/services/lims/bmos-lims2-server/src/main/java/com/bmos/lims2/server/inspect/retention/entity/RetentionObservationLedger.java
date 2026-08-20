package com.bmos.lims2.server.inspect.retention.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Description: 留样观察台账实体
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_retention_observation_ledger")
public class RetentionObservationLedger extends BaseDO {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料规格
     */
    private String materialSpec;

    /**
     * 样品数量
     */
    private String quantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 观察结果（true-符合，false-不符合）
     */
    private Boolean observationResult;

    /**
     * 备注
     */
    private String observationRemark;

    /**
     * 观察人ID
     */
    private String observerId;

    /**
     * 观察人名称
     */
    private String observerName;

    /**
     * 观察时间
     */
    private LocalDateTime observationTime;
}
