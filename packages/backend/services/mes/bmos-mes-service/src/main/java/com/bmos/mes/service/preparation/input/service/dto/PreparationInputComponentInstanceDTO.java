package com.bmos.mes.service.preparation.input.service.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 配液投入组件实例查询DTO
 */
@Getter
@Setter
@ApiModel("配液投入组件实例查询DTO")
public class PreparationInputComponentInstanceDTO {

    @ApiModelProperty(value = "生产指令单id", required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤模型id", required = true)
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty(value = "组件id", required = true)
    @NotNull
    private Long componentId;

    @ApiModelProperty(value = "复制版本", required = true)
    @NotNull
    private Long copyVersion;

    @ApiModelProperty(value = "是否复用", required = true)
    @NotNull
    private Boolean reuse;

}
