package com.bmos.mes.service.platform.parameter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BusinessParameterDetailVO:参数配置详情VO")
public class BusinessParameterDetailVO {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("值")
    private String value;
}
