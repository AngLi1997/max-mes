package com.bmos.wms.service.inspect.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("请验单字段VO")
public class InspectConfigDataVO {

    @ApiModelProperty("请验单字段id")
    private Long id;

    @ApiModelProperty("字段 code")
    private String code;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("数据名称")
    private String dataName;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("排序")
    private Integer sort;
}
