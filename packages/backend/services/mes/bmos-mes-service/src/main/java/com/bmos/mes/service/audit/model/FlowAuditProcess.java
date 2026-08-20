package com.bmos.mes.service.audit.model;

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
@TableName("bm_flow_audit_process")
public class FlowAuditProcess {

    /**
     * 工艺id 对应bm_process的主键id
     */
    private Long processId;
    /**
     * 流程编码 对应bm_flow_audit的code字段
     */
    private String code;
    /**
     * 对应的分类code
     */
    private String categoryCode;

}

