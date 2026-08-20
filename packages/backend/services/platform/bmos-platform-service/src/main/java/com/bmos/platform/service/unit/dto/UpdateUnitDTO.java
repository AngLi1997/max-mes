package com.bmos.platform.service.unit.dto;

import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel(value = "编辑标准单位dto")
public class UpdateUnitDTO {

    @ApiModelProperty(value = "标准单位id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "标准单位名称")
    @NotNull
    private String unitName;

    @ApiModelProperty(value = "精度")
    @NotNull
    private Long unitPrecision;

    @ApiModelProperty(value = "修约规则Code")
    @NotBlank
    private String roundCode;

    @ApiModelProperty(value = "状态")
    @ApiModelEnumProperty(value = "启停",enumClass = StatusEnum.class,required = true)
    private Boolean state;

    @ApiModelProperty(value = "备注")
    private String remark;
}
