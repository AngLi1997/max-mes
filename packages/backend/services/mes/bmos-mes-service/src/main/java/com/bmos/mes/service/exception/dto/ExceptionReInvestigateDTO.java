package com.bmos.mes.service.exception.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("重新调查DTO")
@Data
public class ExceptionReInvestigateDTO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("重新调查原因")
    @NotBlank
    private String reInvestigateReason;

    @ApiModelProperty("重新调查人")
    @NotBlank
    private String reInvestigateUserId;

}
