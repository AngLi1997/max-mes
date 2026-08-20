package com.bmos.mes.service.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ProcedureRestartDTO
 * @Description 工序换班or工序重做处理任务dto
 * @Author Ren Jin Guang
 * @Date 2024/9/9 14:40
 */
@Setter
@Getter
@ToString
@ApiModel("工序换班or工序重做处理任务dto")
public class ProcedureRestartDTO {

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班or工序重做")
    private Boolean isChangeTeam;
}
