package com.bmos.mes.service.exception.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("异常作废DTO")
public class ExceptionCancelDTO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("作废原因")
    @NotBlank
    private String cancelReason;

    @ApiModelProperty("作废人id")
    @NotBlank
    private String cancelUserId;

}
