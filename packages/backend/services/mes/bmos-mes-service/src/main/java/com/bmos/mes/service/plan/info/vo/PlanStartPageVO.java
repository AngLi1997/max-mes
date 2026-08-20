package com.bmos.mes.service.plan.info.vo;

import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel("PlanStartPageVO:生产计划生产前确认分页VO")
public class PlanStartPageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("生产批号")
    private String batchNo;
}
