package com.bmos.mes.service.output.weigh.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 产出称量组件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 09:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_output_weigh_process")
public class OutputWeighProcess extends BaseDO {

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 工序步骤id
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
     * 物料id
     */
    private Long materialId;

    /**
     * 物料批次编号
     */
    private String storageMaterialBatchNo;

    /**
     * 有效期
     */
    private LocalDate expiredDate;

    /**
     * 关联物料id
     */
    private Long relevanceMaterialId;

    /**
     * 关联物料批次id
     */
    private Long relevanceStorageMaterialBatchId;
}
