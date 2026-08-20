package com.bmos.mes.service.process.model.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_procedure_expression")
public class ProcedureExpression extends BaseDO {

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("条件最终结果默认false")
    private Boolean result;

    @ApiModelProperty("条件类型")
    private String expressionType;

    @ApiModelProperty("流程节点id")
    private String nodeId;

    @ApiModelProperty("表达式")
    private String expression;

    @ApiModelProperty("工艺模型id")
    private Long procedureModelId;

    @ApiModelProperty("表达式节点类型")
    private String expressionNodeType;

    @TableField(exist = false)
    private Long planId;

    @TableField(exist = false)
    private Boolean results;

    @ApiModelProperty("条件")
    @TableField(exist = false)
    private List<ProcedureCondition> conditions;

    @ApiModelProperty("条件实例")
    @TableField(exist = false)
    private List<ProcedureConditionInstance> conditionInstances;


}
