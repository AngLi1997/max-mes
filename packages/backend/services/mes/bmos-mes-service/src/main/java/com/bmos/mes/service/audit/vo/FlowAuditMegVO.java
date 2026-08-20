package com.bmos.mes.service.audit.vo;

import com.bmos.mes.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "通知配置VO")
public class FlowAuditMegVO {

    @ApiModelProperty(value = "消息通知id")
    private Long id;

    @ApiModelProperty(value = "节点id")
    private String nodeId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "消息类型")
    private String messageType;

    @ApiModelProperty(value = "流程定义id")
    private String deploymentId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    public String getUserName() {
        return UserUtils.getUsername(userId);
    }
}
