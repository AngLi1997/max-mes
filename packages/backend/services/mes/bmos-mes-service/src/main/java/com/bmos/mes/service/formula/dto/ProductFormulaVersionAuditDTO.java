package com.bmos.mes.service.formula.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("产品配方版本审核DTO")
@Data
public class ProductFormulaVersionAuditDTO {

    @ApiModelProperty("版本id")
    @NotNull
    private Long versionId;

}
