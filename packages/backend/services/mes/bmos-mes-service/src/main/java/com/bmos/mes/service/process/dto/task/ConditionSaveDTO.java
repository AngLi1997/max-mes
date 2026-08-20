package com.bmos.mes.service.process.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "添加条件dto")
public class ConditionSaveDTO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("默认条件")
    private Boolean defaultResult;

    @ApiModelProperty("条件名称")
    private String name;

    @ApiModelProperty("条件编码")
    private String code;

    @ApiModelProperty("条件类型")
    private String conditionType;

    @ApiModelProperty("工序节点id")
    private Long procedureId;

    @ApiModelProperty("工序节点名称")
    private String procedureName;

    @ApiModelProperty("房间ID")
    private Long roomId;

    @ApiModelProperty("房间名称")
    private String roomName;

    @ApiModelProperty("设备id")
    private Long equipmentId;

    @ApiModelProperty("设备名称")
    private String equipmentName;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("任务节点id")
    private Long taskNodeId;

    @ApiModelProperty("任务节点名称")
    private String taskNodeName;

    @ApiModelProperty("步骤id")
    private Long stepId;

    @ApiModelProperty("步骤名称")
    private String stepName;

    @ApiModelProperty("房间状态")
    private String roomState;

    @ApiModelProperty("设备状态")
    private String deviceState;

    @ApiModelProperty("校验规则")
    private String checkRule;

    @ApiModelProperty("物料量")
    private BigDecimal number;

    @ApiModelProperty("单位")
    private String unit;


}
