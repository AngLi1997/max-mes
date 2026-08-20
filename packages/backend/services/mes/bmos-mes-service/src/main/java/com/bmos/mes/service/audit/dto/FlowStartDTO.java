package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "发起流程dto")
public class FlowStartDTO {

    @ApiModelProperty(value = "流程配置分类编码")
    private String code;

    @ApiModelProperty(value = "业务名称")
    private String name;

    @ApiModelProperty(value = "业务id")
    private String businessKey;

    @ApiModelProperty(value = "业务编号")
    private String extField;

    @ApiModelProperty(value = "流程分类编码")
    private String categoryCode;
}
