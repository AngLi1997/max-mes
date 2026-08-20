package com.bmos.lims2.web.unit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "标准单位列表返回vo")
public class UnitVO {

    @ApiModelProperty(value = "标准单位id")
    private Long id;

    @ApiModelProperty(value = "标准单位名称")
    private String unitName;

    @ApiModelProperty(value = "精度")
    private Long unitPrecision;

    @ApiModelProperty(value = "修约规则code")
    private String roundCode;

    @ApiModelProperty(value = "状态")
    private Boolean state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "修约规则名称")
    private String roundName;
}
