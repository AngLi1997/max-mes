package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * 辅助记录节点查询DTO
 */
@Builder
@Getter
public class SubRecordNodeQueryDTO {

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;



}
