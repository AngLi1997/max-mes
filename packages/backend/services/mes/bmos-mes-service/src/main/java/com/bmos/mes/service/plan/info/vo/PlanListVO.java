package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("生产计划列表VO")
public class PlanListVO {

    @ApiModelProperty("生产计划id")
    private Long id;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

}
