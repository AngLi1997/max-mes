package com.bmos.mes.service.plan.production.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel("生产计划日历调整DTO")
public class ProductionPlanCalendarChangeDTO {

    @ApiModelProperty("计划开始日期")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("计划结束日期")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty("生产计划下生产itemId")
    @NotNull
    private Long productionPlanItemId;

    @ApiModelProperty("工序id")
    private Long procedureId;

}
