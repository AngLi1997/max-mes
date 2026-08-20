package com.bmos.wms.service.inspect.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("请验单字段详情VO")
public class InspectInfoVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("字段 code")
    private String code;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("排序")
    private Integer sort;
}
