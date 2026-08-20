package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("组件值列表查询")
public class FormDataListQueryDTO {

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long inspectionOrderId;

    @ApiModelProperty(value = "历史工序步骤id",required = true)
    @NotNull
    private Long parameterConfigId;

    @ApiModelProperty(value = "组件id",required = true)
    @NotNull
    private Long fieldId;

    @ApiModelProperty(value = "任务id", required = true)
    @NotNull
    private Long taskId;

    @ApiModelProperty("是否查询作废数据")
    private Boolean discard;
}
