package com.bmos.mes.service.formula.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("产品配方版本启停状态修改DTO")
public class ProductFormulaVersionChangeStateDTO {

    @ApiModelProperty("配方版本id")
    @NotNull
    private Long id;

    @ApiModelProperty("启停")
    @NotNull
    private Boolean state;

}
