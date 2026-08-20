package com.bmos.mes.service.output.finished.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("bm_output_finished_product")
public class FinishedProductOutput extends BaseDO {

    /**
     * 成品id
     */
    private Long productId;

    /**
     * 成品编码
     */
    private String productMergeCode;

    /**
     * 成品名称
     */
    private String productName;

    /**
     * 成品批号
     */
    private String productBatchNo;

    /**
     * 成品规格
     */
    private String specification;

    /**
     * 组件id
     */
    private Long componentId;

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
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    private Long copyVersion;

}
