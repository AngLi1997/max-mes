package com.bmos.mes.service.plan.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 生产计划指令单班组表
*/
@Getter
@Setter
@ApiModel("ProductPlanTeamUpdateDTO:生产计划班组更新")
public class ProductPlanTeamUpdateDTO {
    @NotNull
    @ApiModelProperty("id")
    private Long id;

    @NotEmpty
    @ApiModelProperty("班组名称")
    private String name;

    @NotEmpty
    @ApiModelProperty("班组编码")
    private String code;

    @NotNull
    @ApiModelProperty("班组描述")
    private String description;

    @ApiModelProperty("产线id列表")
    private List<Long> productionLineIds;

    @NotEmpty
    @ApiModelProperty("班组人员")
    private List<String> people;
}
