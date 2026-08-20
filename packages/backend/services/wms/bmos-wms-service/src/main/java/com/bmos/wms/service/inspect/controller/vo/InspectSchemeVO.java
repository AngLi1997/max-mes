package com.bmos.wms.service.inspect.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("检验方案下拉VO")
public class InspectSchemeVO {

    @ApiModelProperty("检验方案ID")
    private Long schemeId;

    @ApiModelProperty("生效版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("方案名称")
    private String name;

    @ApiModelProperty("显示名称（方案名-版本号）")
    private String displayName;
}
