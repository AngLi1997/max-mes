package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ApiModel("工序模型VO")
@ToString
public class ProcedureModelDetailVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("基础工序id")
    private Long procedureId;

    @ApiModelProperty("流程模型id")
    private String processModelId;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("工序名称")
    private String name;

    @ApiModelProperty("阶段编码")
    private String stageCode;

    @ApiModelProperty("班组集合")
    private List<Long> groupIds;

    @ApiModelProperty("工序步骤集合")
    private List<ProcedureStepModelVO> steps;


}
