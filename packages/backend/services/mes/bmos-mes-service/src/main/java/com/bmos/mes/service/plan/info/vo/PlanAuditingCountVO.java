package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("生产审核中计数VO")
@Data
public class PlanAuditingCountVO {

    @ApiModelProperty("生产计划id")
    private Long id;

    @ApiModelProperty("审核中数量")
    private int auditingCount;

}
