package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("校验业务组件是否已触发DTO")
@Data
public class BusinessComponentsCheckSavedDTO {

    @NotNull
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty(value = "复制版本", required = true)
    @NotNull
    private Long copyVersion;

    @ApiModelProperty(value = "工序步骤模型id", required = true)
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty(hidden = true)
    private List<Long> fieldIds;
}
