package com.bmos.mes.service.audit.dto;

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
public class CheckoutFlowAuditUserDTO {

    @ApiModelProperty(value = "处理人")
    private Long assignee;

    @ApiModelProperty(value = "处理人类型")
    private String assigneeType;

    @ApiModelProperty(value = "节点key")
    private String nodeId;

}
