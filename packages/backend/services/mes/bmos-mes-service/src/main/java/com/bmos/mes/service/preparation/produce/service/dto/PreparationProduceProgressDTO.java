package com.bmos.mes.service.preparation.produce.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 配液产出流程DTO
 */
@Getter
@Setter
@ApiModel("配液产出流程DTO")
public class PreparationProduceProgressDTO {

    @ApiModelProperty("生产指令单id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("工序步骤模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("复制版本")
    @NotNull
    private Long copyVersion;

    @ApiModelProperty("是否复用")
    @NotNull
    private Boolean reuse;

}
