package com.bmos.mes.service.process.vo;

import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工序步骤VO")
public class ProcedureStepVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("历史工序步骤id")
    private Long procedureStepId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty("历史名称")
    private String historicalName;

    /**
     * 节点功能
     */
    @ApiModelProperty("工序功能")
    private ProcedureStepNodeFunctionEnum nodeFunction;


    /**
     * 是否可复用
     */
    @ApiModelProperty("是否可复用")
    private Boolean reusable;

    /**
     * 时长
     */
    @ApiModelProperty("时长")
    private Long duration;

    /**
     * 流程节点id
     */
    @ApiModelProperty(value = "流程节点id", required = true)
    private String nodeId;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String timeUnit;


    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("批记录版本id")
    private Long recordVersionId;

    private List<Long> operationSopId;

    @ApiModelProperty("执行岗")
    private List<Long> roles;

    @ApiModelProperty("区域")
    private List<String> areaList;

    @ApiModelProperty("设备类")
    private List<String> equipmentTypeList;

    @ApiModelProperty("执行条件")
    private ExpressionDetailVO executeCondition;

    @ApiModelProperty("完成条件")
    private ExpressionDetailVO completeCondition;

    @JsonIgnore
    private StepTaskTypeEnum stepTaskType;

    @ApiModelProperty("排序号")
    private Integer sort;

}
