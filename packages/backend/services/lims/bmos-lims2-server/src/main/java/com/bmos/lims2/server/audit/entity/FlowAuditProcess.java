package com.bmos.lims2.server.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程工艺绑定关系表(BmFlowAuditProcess)实体类
 *
 * @author makejava
 * @since 2024-09-05 10:08:08
 */
@Getter
@Setter
@TableName("lm_flow_audit_process")
public class FlowAuditProcess {

    /**
     * 方案id 对应lm_process的主键id
     */
    private Long processId;
    /**
     * 流程编码 对应lm_flow_audit的code字段
     */
    private String code;
    /**
     * 对应的分类code
     */
    private String categoryCode;

}

