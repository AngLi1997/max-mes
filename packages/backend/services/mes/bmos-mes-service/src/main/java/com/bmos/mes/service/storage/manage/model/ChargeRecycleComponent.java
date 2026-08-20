package com.bmos.mes.service.storage.manage.model;


import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("bm_charge_recycle")
public class ChargeRecycleComponent extends BaseDO {

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
