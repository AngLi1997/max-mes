package com.bmos.mes.service.preparation.input.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("配液投入组件实例VO")
@Data
public class PreparationInputComponentInstanceVO {

    @ApiModelProperty("配液计划id")
    private Long planId;

    @ApiModelProperty("组件实例id")
    private Long componentInstanceId;

    @ApiModelProperty("配液计划名称")
    private String planName;

    @ApiModelProperty("投入组件是否已完成投入")
    private Boolean complete;

    @ApiModelProperty("配液单存在未完成量取的批次")
    private Boolean hasUnmeasured;

}
