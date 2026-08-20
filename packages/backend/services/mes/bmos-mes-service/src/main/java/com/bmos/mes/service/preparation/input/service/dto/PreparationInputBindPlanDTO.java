package com.bmos.mes.service.preparation.input.service.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 绑定配液单实例DTO
 */
@Getter
@Setter
@ApiModel("配液投入组件实例查询DTO")
public class PreparationInputBindPlanDTO extends PreparationInputComponentInstanceDTO {

    /**
     * 配液单id
     */
    @ApiModelProperty(value = "配液单id", required = true)
    @NotNull
    private Long preparationPlanId;

}
