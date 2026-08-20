package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@ApiModel("查看工序VO")
@Data
@Builder
public class ProcedureViewVO {

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序节点名称")
    private String procedureName;

    @ApiModelProperty("工步列表")
    private List<ProcedureStepViewVO> procedureStepViewList;

}
