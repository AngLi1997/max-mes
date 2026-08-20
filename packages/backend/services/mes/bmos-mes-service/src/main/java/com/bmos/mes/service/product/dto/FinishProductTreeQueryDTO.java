package com.bmos.mes.service.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel
public class FinishProductTreeQueryDTO {

    @ApiModelProperty("分类信息类型")
    @NotNull
    private Integer categoryType;

    @ApiModelProperty("是否成品")
    @NotNull
    private Boolean isFinishedProduct;

}
