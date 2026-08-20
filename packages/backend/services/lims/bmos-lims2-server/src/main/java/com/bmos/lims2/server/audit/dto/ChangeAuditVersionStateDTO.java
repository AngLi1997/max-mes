package com.bmos.lims2.server.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("修改审核流程版本DTO")
public class ChangeAuditVersionStateDTO {

    @NotNull
    @ApiModelProperty(value = "审核流程版本id")
    private Long id;

    @ApiModelProperty(value = "启用为true,停用为false")
    @NotNull
    private Boolean enable;

}
