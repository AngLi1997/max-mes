package com.bmos.mes.service.plan.production.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ApiModel("工序执行日期相关信息")
@Data
public class ProcedureDetailDTO {


    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("计划开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("计划结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

}
