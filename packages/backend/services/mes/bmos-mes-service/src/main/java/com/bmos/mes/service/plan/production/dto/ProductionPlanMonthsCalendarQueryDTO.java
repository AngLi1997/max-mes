package com.bmos.mes.service.plan.production.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel("生产计划多月份日历查询DTO")
@Data
public class ProductionPlanMonthsCalendarQueryDTO {

    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

    @ApiModelProperty(value = "开始月份", example = "2025-01")
    @NotBlank
    private String startMonth;

    @ApiModelProperty(value = "结束月份", example = "2025-12")
    @NotBlank
    private String endMonth;

    @ApiModelProperty("计划详情id")
    private Long productionPlanItemId;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;

    @ApiModelProperty("工艺名称")
    private Long processId;

}
