package com.bmos.mes.service.output.finished.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("校验成品产出组件")
public class ValidateFinishedProductComponentDTO {

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;


    @ApiModelProperty(value = "工序步骤模型id", required = true)
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty(value = "复制版本",required = true)
    @NotNull
    private Long copyVersion;
}
