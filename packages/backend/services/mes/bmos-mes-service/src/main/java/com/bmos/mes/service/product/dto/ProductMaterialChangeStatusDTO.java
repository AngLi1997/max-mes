package com.bmos.mes.service.product.dto;

import com.bmos.mes.common.enums.StateEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("改变物料状态DTO")
public class ProductMaterialChangeStatusDTO {
    @ApiModelProperty(value = "id",required = true)
    @NotNull
    private Long id;

    @ApiModelEnumProperty(value = "启停",enumClass = StateEnum.class,required = true)
    @NotNull
    private Boolean status;
}
