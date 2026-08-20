package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@ApiModel("组件唯一查询DTO")
@Builder
@Getter
@Setter
public class UniqueComponentQueryDTO {

    @ApiModelProperty("组件id")
    private Long componentId;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("复制版本")
    private Long copyVersion;

    @ApiModelProperty("是否复用")
    private Boolean reuse;

    @ApiModelProperty("步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

}
