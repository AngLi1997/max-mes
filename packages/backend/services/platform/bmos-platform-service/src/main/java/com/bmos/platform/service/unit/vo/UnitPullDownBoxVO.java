package com.bmos.platform.service.unit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "标准单位下拉框vo")
public class UnitPullDownBoxVO {

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("标准单位id")
    private Long unitId;
}
