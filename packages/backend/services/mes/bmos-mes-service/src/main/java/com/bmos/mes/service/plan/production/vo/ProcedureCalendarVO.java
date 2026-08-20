package com.bmos.mes.service.plan.production.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@ApiModel("生产计划工序日历VO")
@Data
public class ProcedureCalendarVO {

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("工序id")
    private Long procedureId;

}
