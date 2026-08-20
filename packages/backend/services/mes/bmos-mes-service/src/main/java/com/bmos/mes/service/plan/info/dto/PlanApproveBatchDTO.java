package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel("生产计划批量审批DTO")
@Data
public class PlanApproveBatchDTO {

    @NotEmpty
    @ApiModelProperty("生产计划id列表")
    private List<Long> planIdList;

}
