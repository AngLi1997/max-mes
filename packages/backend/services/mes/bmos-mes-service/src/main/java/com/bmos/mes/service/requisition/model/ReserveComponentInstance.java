package com.bmos.mes.service.requisition.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 预定物料组件实例
 */
@Getter
@Setter
@TableName("bm_reserve_component_instance")
public class ReserveComponentInstance extends BaseDO {

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 生产计划批号
     */
    private String batchNo;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 复制版本
     */
    private Long copyVersion;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

}
