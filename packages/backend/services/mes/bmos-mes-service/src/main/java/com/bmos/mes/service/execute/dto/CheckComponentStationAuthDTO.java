package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("校验组件工位权限DTO")
@Data
public class CheckComponentStationAuthDTO {

    @ApiModelProperty("组件id")
    private Long ComponentId;

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

}
