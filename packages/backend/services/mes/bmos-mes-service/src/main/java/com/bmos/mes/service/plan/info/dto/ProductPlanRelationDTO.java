package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 生产计划新建/更新关联批次
*/
@Getter
@Setter
@ApiModel("DTO")
public class ProductPlanRelationDTO {
    @NotNull
    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("生产计划Id列表")
    private List<Long> planIds;
}
