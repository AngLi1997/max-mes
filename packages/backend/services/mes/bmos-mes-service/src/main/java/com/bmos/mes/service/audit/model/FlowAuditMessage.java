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
@TableName(value = "bm_flow_audit_message")
public class FlowAuditMessage extends BaseDO {

    @ApiModelProperty(value = "节点id")
    private String nodeId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "流程id")
    private String deploymentId;

    @ApiModelProperty(value = "消息类型")
    private String messageType;
}
