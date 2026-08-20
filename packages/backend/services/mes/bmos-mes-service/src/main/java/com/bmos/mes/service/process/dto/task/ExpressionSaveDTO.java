package com.bmos.mes.service.process.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "添加表达式dto")
public class ExpressionSaveDTO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("工步或者任务id")
    private Long stepTaskId;

    @ApiModelProperty("表达式")
    private String expression;

    @ApiModelProperty("表达式结果")
    private Boolean result;

    @ApiModelProperty("条件")
    private List<ConditionSaveDTO> conditionList;
}
