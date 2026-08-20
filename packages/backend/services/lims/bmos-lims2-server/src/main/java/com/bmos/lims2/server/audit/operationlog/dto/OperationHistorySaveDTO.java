package com.bmos.lims2.server.audit.operationlog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("保存日志DTO")
public class OperationHistorySaveDTO {

    @ApiModelProperty(value = "请验单id",required = true)
    @NotNull
    private Long businessId;

    @ApiModelProperty(value = "操作类型:",required = true)
    @NotEmpty
    private String type;
}
