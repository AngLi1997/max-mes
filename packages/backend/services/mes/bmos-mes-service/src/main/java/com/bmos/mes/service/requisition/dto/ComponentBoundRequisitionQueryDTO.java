package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("领料接收组件绑定领料单查询DTO")
@Data
public class ComponentBoundRequisitionQueryDTO {

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤模型id", required = true)
    @NotNull
    private Long procedureStepModelId;

    /**
     * 复制版本（默认0）
     */
    @ApiModelProperty(value = "复制版本号", required = true)
    @NotNull
    private Long copyVersion;

}
