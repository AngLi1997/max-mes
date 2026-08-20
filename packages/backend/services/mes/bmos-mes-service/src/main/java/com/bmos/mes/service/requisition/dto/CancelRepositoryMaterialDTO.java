package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelRepositoryMaterialDTO {

    @ApiModelProperty("主键id")
    @NotNull
    private Long id;

}
