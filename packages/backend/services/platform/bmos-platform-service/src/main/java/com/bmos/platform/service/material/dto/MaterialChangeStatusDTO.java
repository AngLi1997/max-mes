package com.bmos.platform.service.material.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.common.enums.StatusEnum;
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
public class MaterialChangeStatusDTO {
    @ApiModelProperty(value = "id",required = true)
    @NotNull
    private Long id;

    @ApiModelEnumProperty(value = "启停",enumClass = StatusEnum.class,required = true)
    @EnumValidate(value = StatusEnum.class)
    private Boolean status;
}
