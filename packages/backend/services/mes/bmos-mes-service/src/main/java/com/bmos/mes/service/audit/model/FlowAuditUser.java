package com.bmos.mes.service.audit.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@TableName(value = "bm_flow_audit_user")
public class FlowAuditUser extends BaseDO {

    @ApiModelProperty(value = "流程定义id")
    private String deploymentId;

    @ApiModelProperty(value = "处理人")
    private Long assignee;

    @ApiModelProperty(value = "处理人类型")
    private String assigneeType;

    @ApiModelProperty(value = "节点key")
    private String nodeId;
}
