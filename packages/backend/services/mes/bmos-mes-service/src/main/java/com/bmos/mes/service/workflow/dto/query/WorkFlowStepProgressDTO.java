package com.bmos.mes.service.workflow.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName WorkFlowStepProgressDTO
 * @Description 查询工序步骤生产进度dto
 * @Author Ren Jin Guang
 * @Date 2024/8/26 10:41
 */
@Setter
@Getter
@ToString
@ApiModel("查询生产进度dto")
public class WorkFlowStepProgressDTO {

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("最新流程实例id")
    private String freshExecutionId;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("实例id集合")
    private List<String> executionIdList;

    @ApiModelProperty("流程状态")
    @NotNull
    private Integer state;
}
