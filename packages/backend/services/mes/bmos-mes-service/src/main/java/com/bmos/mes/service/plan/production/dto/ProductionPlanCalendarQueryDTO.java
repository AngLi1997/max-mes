package com.bmos.mes.service.plan.production.dto;

import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("生产计划日历查询DTO")
@Data
public class ProductionPlanCalendarQueryDTO {

    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

    @ApiModelProperty("年")
    @NotNull
    private Integer year;

    @ApiModelProperty("月")
    @Max(12)
    @Min(1)
    private Integer month;

    @ApiModelProperty("计划详情id")
    private Long productionPlanItemId;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;

    @ApiModelProperty("工艺名称")
    private Long processId;

}
