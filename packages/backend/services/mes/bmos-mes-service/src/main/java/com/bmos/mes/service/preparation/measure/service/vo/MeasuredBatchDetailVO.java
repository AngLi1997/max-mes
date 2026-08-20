package com.bmos.mes.service.preparation.measure.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("已量取批次详情VO")
@Data
public class MeasuredBatchDetailVO {

    @ApiModelProperty("量取批次id")
    private Long id;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("配液计划id")
    private Long liquidPreparationPlanId;

}
