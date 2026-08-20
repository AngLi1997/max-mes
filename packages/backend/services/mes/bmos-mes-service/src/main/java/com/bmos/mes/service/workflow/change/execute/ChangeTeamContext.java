package com.bmos.mes.service.workflow.change.execute;

import com.bmos.mes.service.plan.info.model.Plan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ChangeTeamContext
 * @Description 换班参数
 * @Author Ren Jin Guang
 * @Date 2024/8/16 15:27
 */
@Setter
@Getter
@ToString
public class ChangeTeamContext {

    @ApiModelProperty("计划信息")
    private Plan plan;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("执行实例id")
    @NotEmpty
    private String executionId;

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("工序步骤id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("流程实例id")
    @NotNull
    private String processInstanceId;
}
