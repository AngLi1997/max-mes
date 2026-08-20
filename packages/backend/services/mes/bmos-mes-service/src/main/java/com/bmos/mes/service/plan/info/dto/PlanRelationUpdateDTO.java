package com.bmos.mes.service.plan.info.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("生产批次关联关系更新DTO")
@Data
public class PlanRelationUpdateDTO {

    @ApiModelProperty("生产指令单id")
    private Long productPlanId;

    @ApiModelProperty("关联生产计划")
    private List<ProductPlanRelationDTO> relationPlanList = new ArrayList<>();

}
