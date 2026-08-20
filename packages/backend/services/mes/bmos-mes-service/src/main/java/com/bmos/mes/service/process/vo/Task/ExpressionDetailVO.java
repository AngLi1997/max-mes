package com.bmos.mes.service.process.vo.Task;

import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "添加表达式dto")
public class ExpressionDetailVO {

    @ApiModelProperty("表达式id")
    private Long id;

    @ApiModelProperty("表达式类型")
    private String expressionType;

    @ApiModelProperty("表达式类型枚举")
    private ExpressionTypeEnum expressionTypeEnum;

    @ApiModelProperty("表达式")
    private String expression;

    @ApiModelProperty("表达式结果")
    private Boolean result;

    @ApiModelProperty("任务或者步骤id")
    private Long stepTaskId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("表达式节点类型")
    private String expressionNodeType;

    @ApiModelProperty("节点类型枚举")
    private NodeTypeEnum nodeTypeEnum;

    @ApiModelProperty("条件")
    private List<ConditionDetailVO> conditionList;

    public ExpressionTypeEnum getExpressionTypeEnum() {
        return ExpressionTypeEnum.getEnumByValue(expressionNodeType);
    }

    public NodeTypeEnum getNodeTypeEnum() {
        return NodeTypeEnum.getEnumByValue(expressionNodeType);
    }
}
