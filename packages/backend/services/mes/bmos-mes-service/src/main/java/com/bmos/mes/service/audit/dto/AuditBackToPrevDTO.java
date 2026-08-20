package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@ApiModel(value = "流程回退dto")
public class AuditBackToPrevDTO {

    @ApiModelProperty("流程实例id")
    @NotBlank
    private String executionId;

    @ApiModelProperty("回退原因")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;

}
