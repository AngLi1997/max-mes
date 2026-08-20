package com.bmos.mes.service.inspect.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("请验单信息详情VO")
public class InspectInfoVO {

    /**
     * 请验单信息详情VO
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 展示名称
     */
    @ApiModelProperty("展示名称")
    private String showName;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Boolean required;

}
