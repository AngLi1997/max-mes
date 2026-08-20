package com.bmos.mes.service.process.vo;

import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工步详情VO")
public class ProcedureStepModelDetailVO {

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("工步模型id")
    private Long id;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 节点功能
     */
    private String nodeFunction;


    /**
     * 是否可复用
     */
    private Boolean reusable;
    /**
     * 工序id
     */
    private Long procedureId;


    /**
     * 工艺版本号
     */
    private String processVersion;

    /**
     * 工序步骤名称
     */
    private String name;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 单位
     */
    private String timeUnit;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 操作规程
     */
    private String operationSop;

    private Long delIdFlag;


    private String area;

    private String equipment;

    private StepTaskTypeEnum stepType;


}
