package com.bmos.mes.service.process.vo;

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
public class ProcedureStepRecordItemVO {

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("历史工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("流程模型节点id")
    private String nodeId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;

    @ApiModelProperty("是否复用")
    private Boolean reusable;

    @ApiModelProperty("记录项组件配置")
    private List<ComponentConfigDetailVO> componentConfigs;
}
