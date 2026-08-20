package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ApiModel("工序步骤模型VO")
@ToString
public class ProcedureStepModelVO {

    @ApiModelProperty("工序模型id:procedureStepModelId")
    private Long id;

    @ApiModelProperty("历史工序id")
    private Long procedureStepId;

    @ApiModelProperty("流程节点Id")
    private String nodeId;

    @ApiModelProperty("工序步骤名称")
    private String name;

    /**
     * 时长
     */
    @ApiModelProperty("执行时长")
    private Long duration;

    /**
     * 单位
     */
    @ApiModelProperty("执行时长单位")
    private String timeUnit;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序步骤班组集合")
    private List<Long> groupIds;

    @ApiModelProperty("排序号")
    private Integer sort;
}
