package com.bmos.lims2.server.inspect.scheme.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工序步骤记录项VO")
public class ProcedureStepRecordItemDTO {


    @ApiModelProperty("分析项配置id")
    private Long parameterConfigId;

    @ApiModelProperty("分析项id")
    private Long parameterId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;


    @ApiModelProperty("记录项组件配置")
    private List<ComponentConfigDetailDTO> componentConfigs;
}
