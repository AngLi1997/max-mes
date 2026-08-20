package com.bmos.mes.service.plan.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("班组绑定产线DTO")
public class TeamBoundProductionLineDTO {

    @ApiModelProperty("班组id")
    @NotNull
    private Long id;

    @ApiModelProperty("产线id列表")
    private List<Long> productionLineIds;

}
