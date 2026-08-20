package com.bmos.mes.service.facotry.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("产线VO")
public class FactoryLineInfoVO {

    /**
     * 产线id
     */
    @ApiModelProperty("产线id")
    private Long id;

    /**
     * 产线编码
     */
    @ApiModelProperty("产线编码")
    private String code;

    /**
     * 产线名称
     */
    @ApiModelProperty("产线名称")
    private String name;

    /**
     * 前端显示名称 code-name
     */
    @ApiModelProperty("前端显示名称")
    private String showName;

}
