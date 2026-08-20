package com.bmos.mes.service.preparation.produce.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 配液产出称量流程表(BmPreparationProduceProgress)实体类
 *
 * @author makejava
 * @since 2024-08-01 12:57:52
 */
@Getter
@Setter
@TableName("bm_preparation_produce_progress")
public class PreparationProduceProgress extends BaseDO {

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
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 配液单id
     */
    private Long preparationPlanId;

    /**
     * 产出人id
     */
    private String producerId;
    /**
     * 复核人id
     */
    private String reCheckerId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 配方物料id
     */
    private Long formulaMaterialId;
    /**
     * 物料批次id
     */
    private Long materialBatchId;
    /**
     * 物料批次编号
     */
    private String materialBatchNo;
    /**
     * 有效期
     */
    private LocalDate expiredDate;

}

