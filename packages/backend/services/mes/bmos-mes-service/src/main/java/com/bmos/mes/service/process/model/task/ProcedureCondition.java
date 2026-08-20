package com.bmos.mes.service.process.model.task;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "bm_procedure_condition")
public class ProcedureCondition extends BaseDO {

    @ApiModelProperty("表达式id")
    private Long expressionId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("任务详情json数据")
    private String conditionDetails;

    @ApiModelProperty("任务类型")
    private String conditionType;

    @ApiModelProperty("默认条件")
    private Boolean defaultResult;

    @ApiModelProperty("节点类型")
    private String conditionNodeType;

    @ApiModelProperty("工序模型id")
    @TableField(exist = false)
    private Long procedureModelId;

}
