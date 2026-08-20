package com.bmos.lims2.server.audit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SendMessageDTO
 * @Author Ren Jin Guang
 * @Date 2025/1/10 10:14
 */
@Setter
@Getter
@ToString
public class SendMessageDTO {

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("流程配置id")
    private String deploymentId;

    @ApiModelProperty("业务id")
    @NotNull
    private Long businessId;

    @ApiModelProperty("审批意见")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("审核分类code")
    @NotBlank
    private String auditCategoryCode;

    @ApiModelProperty("是否是发起审核")
    private Boolean isStart;
}
