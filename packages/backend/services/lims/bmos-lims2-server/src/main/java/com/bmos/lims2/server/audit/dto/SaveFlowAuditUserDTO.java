package com.bmos.lims2.server.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "审核配置表dto")
public class SaveFlowAuditUserDTO {

    @ApiModelProperty(value = "处理人id")
    private Long id;

    @ApiModelProperty(value = "处理人")
    @NotNull
    private Long assignee;

    @ApiModelProperty(value = "处理人类型")
    @NotBlank
    private String assigneeType;

    @ApiModelProperty(value = "节点key")
    @NotBlank
    private String nodeId;

    @ApiModelProperty(value = "流程定义id")
    private String deploymentId;
}
