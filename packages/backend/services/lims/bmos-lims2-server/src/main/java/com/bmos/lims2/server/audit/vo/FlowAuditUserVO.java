package com.bmos.lims2.server.audit.vo;

import cn.hutool.core.util.StrUtil;

import com.bmos.lims2.server.platform.util.UserUtils;
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
@ApiModel(value = "审核配置表VO")
public class FlowAuditUserVO {

    @ApiModelProperty(value = "处理人id")
    private Long id;

    @ApiModelProperty(value = "处理人")
    private String assignee;

    @ApiModelProperty(value = "处理人类型")
    private String assigneeType;

    @ApiModelProperty(value = "节点key")
    private String nodeId;

    @ApiModelProperty(value = "流程定义id")
    private String deploymentId;

    @ApiModelProperty(value = "处理人名称")
    private String assigneeName;

    public String getAssigneeName() {
        return StrUtil.isBlank(UserUtils.getUsername(assignee)) ? "角色名称" : UserUtils.getUsername(assignee);
    }
}
