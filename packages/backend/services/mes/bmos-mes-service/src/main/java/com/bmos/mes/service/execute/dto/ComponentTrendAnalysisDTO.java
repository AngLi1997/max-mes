package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@ApiModel("查询记录项最新值DTO")
public class ComponentTrendAnalysisDTO {

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "历史工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty(value = "组件id",required = true)
    @NotNull
    private Long fieldId;

    /**
     * 组件最大最小值
     */
    @ApiModelProperty(value = "组件最大值")
    private BigDecimal max;

    /**
     * 组件最小值
     */
    @ApiModelProperty(value = "组件最小值")
    private BigDecimal min;
}
