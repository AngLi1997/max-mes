package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("查询记录项最新值DTO")
public class RecordItemLatestDataQueryDTO {

    @ApiModelProperty(value = "检验单id",required = true)
    @NotNull
    private Long inspectionOrderId;

    @ApiModelProperty(value = "分析项id",required = true)
    @NotNull
    private Long parameterConfigId;

    @ApiModelProperty(value = "任务id",required = true)
    @NotNull
    private Long taskId;

    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty("是否查询废弃值")
    private Boolean discard;

    @ApiModelProperty(hidden = true)
    private List<Long> fieldIdList;
}
