package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@ApiModel("复制版本列表查询DTO")
@Getter
public class CopiesQueryDTO {

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("是否复用")
    private Boolean reuse;

}
