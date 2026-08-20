package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("根据设备编码查询设备DTO")
public class ExecuteEquipmentCodeQueryDTO {

    @ApiModelProperty(value = "设备编码", required = true)
    @NotEmpty
    private String code;

    @ApiModelProperty("生产计划id(需要校验组件配置时必传)")
    private Long productPlanId;

    @ApiModelProperty("组件id(需要校验组件配置时必传)")
    private Long componentId;

    @ApiModelProperty("工步模型id(需要校验组件配置时必传)")
    private Long procedureStepModelId;

    /**
     * 是否需要校验组件上的工位配置
     * @return
     */
    public boolean validConfig() {
        return componentId != null && productPlanId != null && procedureStepModelId != null;
    }

}
