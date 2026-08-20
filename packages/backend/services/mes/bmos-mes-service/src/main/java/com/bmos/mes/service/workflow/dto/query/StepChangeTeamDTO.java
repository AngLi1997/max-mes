package com.bmos.mes.service.workflow.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName StepChangeTeamDTO
 * @Description 查询工步换班信息dto
 * @Author Ren Jin Guang
 * @Date 2024/8/27 10:41
 */
@Setter
@Getter
@ToString
@ApiModel("查询工步换班信息dto")
public class StepChangeTeamDTO {

    @ApiModelProperty("实例id集合")
    private List<String> executionIdList;

    @ApiModelProperty("类型")
    @NotBlank
    private String type;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;
}
