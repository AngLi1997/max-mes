package com.bmos.mes.service.plan.instruction.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @ClassName TeamDetailQueryDTO
 * @Description 查询班组详情dto
 * @Author Ren Jin Guang
 * @Date 2024/8/19 17:49
 */
@Setter
@Getter
@ToString
@ApiModel("查询班组详情dto")
public class TeamDetailQueryDTO {

    @ApiModelProperty("换班类型")
    @NotNull
    private String nodeFunction;

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("换班次数，默认为0次")
    @NotNull
    private Integer changeTeamNumber;
}
