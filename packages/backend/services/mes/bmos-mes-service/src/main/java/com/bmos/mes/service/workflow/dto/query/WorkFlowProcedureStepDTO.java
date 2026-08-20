package com.bmos.mes.service.workflow.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName WorkFlowProcedureStepDTO
 * @Description 生产管理查询工序步骤dto
 * @Author Ren Jin Guang
 * @Date 2024/8/22 11:57
 */
@Setter
@Getter
@ToString
@ApiModel("查询生产管理工步dto")
public class WorkFlowProcedureStepDTO {

    @ApiModelProperty("执行实例id")
    private String executionId;

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("工序换班次数")
    @NotNull
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    @NotNull
    private Integer processChangeNumber;

    @ApiModelProperty("换班类型")
    private String nodeFunction;

    @ApiModelProperty("工序状态")
    @NotNull
    private Integer state;
}
