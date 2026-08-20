package com.bmos.mes.service.process.model.task;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_procedure_condition_instance_history")
public class ProcedureConditionInstanceHistory extends BaseDO {

    @ApiModelProperty("表达式id")
    private Long expressionId;

    @ApiModelProperty("模型id")
    private Long procedureModelId;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("任务详情json数据")
    private String conditionDetails;

    @ApiModelProperty("条件类型")
    private String conditionType;

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("执行结果")
    private Boolean taskResult;

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("默认结果")
    private Boolean defaultResult;

}
