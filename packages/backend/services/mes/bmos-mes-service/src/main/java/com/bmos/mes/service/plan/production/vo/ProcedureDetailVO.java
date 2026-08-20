package com.bmos.mes.service.plan.production.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName 生产计划工序详情vo
 * @Description 生产计划详情vo
 * @Author Ren Jin Guang
 * @Date 2024/8/27 18:46
 */
@Setter
@Getter
@ToString
@ApiModel("生产计划工序详情vo")
public class ProcedureDetailVO {

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("计划开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("计划结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
