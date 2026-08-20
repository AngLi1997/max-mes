package com.bmos.platform.service.unit.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@ApiModel(value = "添加标准单位dto")
public class SaveUnitDTO {

    @ApiModelProperty(value = "单位名称")
    @NotNull
    private String unitName;

    @ApiModelProperty(value = "精度")
    @NotNull
    private Long unitPrecision;

    @ApiModelProperty(value = "修约Code")
    @NotBlank
    private String roundCode;

    @ApiModelProperty(value = "备注")
    private String remark;
}
